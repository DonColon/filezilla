package com.dardan.rrafshi.filezilla;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.dardan.rrafshi.filezilla.model.FilezillaPath;
import com.dardan.rrafshi.filezilla.model.FilezillaSession;


public final class FilezillaManager implements AutoCloseable
{
	private final FilezillaSession session;
	private final FTPClient client;


	public FilezillaManager(final FilezillaSession session)
		throws FilezillaException.NoConnection
	{
		this.session = session;

		this.client = new FTPClient();
		this.connect();
	}


	private void connect()
		throws FilezillaException.NoConnection
	{
		try {
			this.client.connect(this.session.getHost(), Integer.parseInt(this.session.getPort()));

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.NoConnection("Failed to connect to server " + this.session.getHost() + ":" + this.session.getPort() + " with " + message);

			this.client.login(this.session.getUsername(), this.session.getPassword());

			this.client.setFileType(this.session.getFileType().getType());
			this.client.setFileTransferMode(this.session.getFileTransferMode().getMode());

		} catch (final IOException exception) {

			throw new FilezillaException.NoConnection("Failed to connect to server " + this.session.getHost() + ":" + this.session.getPort(), exception);
		}
	}


	public void downloadFile(final FilezillaPath originPath, final Path targetPath)
		throws FilezillaException.DownloadFailed
	{
		try(OutputStream dataOut = Files.newOutputStream(targetPath)) {
			this.client.retrieveFile(originPath.toString(), dataOut);

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.DownloadFailed("Failed to download file '" + originPath + "' to path '" + targetPath + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.DownloadFailed("Failed to download file '" + originPath + "' to path '" + targetPath + "'", exception);
		}
	}

	public void uploadFile(final Path originPath, final FilezillaPath targetPath)
		throws FilezillaException.UploadFailed
	{
		final FilezillaPath rootPath = targetPath.resolve(originPath.getFileName().toString());

		try(InputStream dataIn = Files.newInputStream(originPath)) {
			this.client.storeFile(rootPath.toString(), dataIn);

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.UploadFailed("Failed to upload file '" + originPath + "' to path '" + targetPath + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.UploadFailed("Failed to upload file '" + originPath + "' to path '" + targetPath + "'", exception);
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

	public void deleteFolder(final FilezillaPath pathToFolder)
		throws FilezillaException.DeleteFailed
	{
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

	@Override
	public void close()
		throws Exception
	{
		if(this.client != null)
			this.client.disconnect();
	}
}
