package com.dardan.rrafshi.filezilla;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.dardan.rrafshi.filezilla.model.FilezillaPath;
import com.dardan.rrafshi.filezilla.model.FilezillaSession;


public final class FilezillaManager implements AutoCloseable
{
	private final FilezillaSession session;
	private final FTPClient client;


	public FilezillaManager(final FilezillaSession session)
		throws FilezillaException.NoConnection, FilezillaException.LoginDenied
	{
		this.session = session;

		this.client = new FTPClient();
		this.connect();
	}


	private void connect()
		throws FilezillaException.NoConnection, FilezillaException.LoginDenied
	{
		try {
			this.client.connect(this.session.getHost(), Integer.parseInt(this.session.getPort()));

			String message = this.client.getReplyString();
			int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.NoConnection("Failed to connect to server " + this.session.getHost() + ":" + this.session.getPort() + " with " + message);

			this.client.login(this.session.getUsername(), this.session.getPassword());

			message = this.client.getReplyString();
			code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.LoginDenied("Failed to login to server " + this.session.getHost() + ":" + this.session.getPort() + " with " + message);

			this.client.setFileType(this.session.getFileType().getType());
			this.client.setFileTransferMode(this.session.getFileTransferMode().getMode());

		} catch (final IOException exception) {

			throw new FilezillaException.NoConnection("Failed to connect to server " + this.session.getHost() + ":" + this.session.getPort(), exception);
		}
	}


	public void uploadFile(final Path originPath, final FilezillaPath targetPath)
		throws FilezillaException.UploadFailed
	{
		final FilezillaPath pathToFile = targetPath.resolve(originPath.getFileName().toString());

		try(InputStream dataIn = Files.newInputStream(originPath)) {
			this.client.storeFile(pathToFile.toString(), dataIn);

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.UploadFailed("Failed to upload file '" + originPath + "' to path '" + targetPath + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.UploadFailed("Failed to upload file '" + originPath + "' to path '" + targetPath + "'", exception);
		}
	}

	public void downloadFile(final FilezillaPath originPath, final Path targetPath)
		throws FilezillaException.DownloadFailed
	{
		final Path pathToFile = targetPath.resolve(originPath.getFileName().toString());

		try(OutputStream dataOut = Files.newOutputStream(pathToFile)) {
			this.client.retrieveFile(originPath.toString(), dataOut);

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.DownloadFailed("Failed to download file '" + originPath + "' to path '" + targetPath + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.DownloadFailed("Failed to download file '" + originPath + "' to path '" + targetPath + "'", exception);
		}
	}

	public void deleteFile(final FilezillaPath pathToFile)
		throws FilezillaException.DeleteFailed
	{
		try {
			this.client.deleteFile(pathToFile.toString());

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.DeleteFailed("Failed to delete file '" + pathToFile + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.DeleteFailed("Failed to delete file '" + pathToFile + "'", exception);
		}
	}

	public List<FilezillaPath> listFiles(final FilezillaPath parentPath, final Predicate<FilezillaPath> filter)
		throws FilezillaException.ListingFailed
	{
		final List<FilezillaPath> filePaths = new ArrayList<>();

		try {
			final FTPFile[] files = this.client.listFiles(parentPath.toString());

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.ListingFailed("Failed to list content of path '" + parentPath + "' with " + message);

			for(final FTPFile file : files) {
				if(file.isFile()) {
					final FilezillaPath pathToFile = parentPath.resolve(file.getName());
					if(filter == null || filter.test(pathToFile)) {
						filePaths.add(pathToFile);
					}
				}
			}

		} catch (final IOException exception) {

			throw new FilezillaException.ListingFailed("Failed to list content of path '" + parentPath + "'", exception);
		}

		return Collections.unmodifiableList(filePaths);
	}

	public List<FilezillaPath> listFiles(final FilezillaPath parentPath)
		throws FilezillaException.ListingFailed
	{
		return this.listFiles(parentPath, null);
	}

	public void createFolder(final FilezillaPath pathToFolder)
		throws FilezillaException.CreateFailed
	{
		try {
			this.client.makeDirectory(pathToFolder.toString());

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.CreateFailed("Failed to create folder '" + pathToFolder + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.CreateFailed("Failed to create folder '" + pathToFolder + "'", exception);
		}
	}

	public void deleteFolder(final FilezillaPath pathToFolder, final boolean forceDelete)
		throws FilezillaException.DeleteFailed, FilezillaException.ListingFailed
	{
		if(forceDelete) {
			final List<FilezillaPath> filePaths = this.listFiles(pathToFolder);

			for(final FilezillaPath filePath : filePaths)
				this.deleteFile(filePath);

			final List<FilezillaPath> folderPaths = this.listFolders(pathToFolder);

			for(final FilezillaPath folderPath : folderPaths)
				this.deleteFolder(folderPath, forceDelete);
		}

		try {
			this.client.removeDirectory(pathToFolder.toString());

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.DeleteFailed("Failed to delete folder '" + pathToFolder + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.DeleteFailed("Failed to delete folder '" + pathToFolder + "'", exception);
		}
	}

	public void deleteFolder(final FilezillaPath pathToFolder)
		throws FilezillaException.DeleteFailed, FilezillaException.ListingFailed
	{
		this.deleteFolder(pathToFolder, false);
	}

	public List<FilezillaPath> listFolders(final FilezillaPath parentPath, final Predicate<FilezillaPath> filter)
			throws FilezillaException.ListingFailed
	{
		final List<FilezillaPath> folderPaths = new ArrayList<>();

		try {
			final FTPFile[] folders = this.client.listFiles(parentPath.toString());

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.ListingFailed("Failed to list content of path '" + parentPath + "' with " + message);

			for(final FTPFile folder : folders) {
				if(folder.isDirectory()) {
					final FilezillaPath pathToFile = parentPath.resolve(folder.getName());
					if(filter == null || filter.test(pathToFile)) {
						folderPaths.add(pathToFile);
					}
				}
			}

		} catch (final IOException exception) {

			throw new FilezillaException.ListingFailed("Failed to list content of path '" + parentPath + "'", exception);
		}

		return Collections.unmodifiableList(folderPaths);
	}

	public List<FilezillaPath> listFolders(final FilezillaPath parentPath)
		throws FilezillaException.ListingFailed
	{
		return this.listFolders(parentPath, null);
	}

	public void move(final FilezillaPath from, final FilezillaPath to)
		throws FilezillaException.MovingFailed
	{
		try {
			this.client.rename(from.toString(), to.toString());

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.MovingFailed("Failed to move file from '" + from + "' to '" + to + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.MovingFailed("Failed to move file from '" + from + "' to '" + to + "'", exception);
		}
	}

	public void rename(final FilezillaPath path, final String newName)
		throws FilezillaException.RenamingFailed
	{
		final FilezillaPath parentPath = path.getParent();
		final FilezillaPath newPath = parentPath.resolve(newName);

		try {
			this.client.rename(path.toString(), newPath.toString());

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.RenamingFailed("Failed to rename file from '" + path + "' to '" + newPath + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.RenamingFailed("Failed to rename file from '" + path + "' to '" + newPath + "'", exception);
		}
	}

	@Override
	public void close()
		throws Exception
	{
		if(this.client != null) {
			this.client.logout();
			this.client.disconnect();
		}
	}
}
