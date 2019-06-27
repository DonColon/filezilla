package com.dardan.rrafshi.filezilla;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public final class FilezillaManager implements AutoCloseable
{
	private final String host;
	private final String port;
	private final String username;
	private final String password;

	private FTPClient client;


	public FilezillaManager(final FilezillaSession session)
		throws FilezillaException.NoConnection
	{
		this.host = session.getHost();
		this.port = session.getPort();
		this.username = session.getUsername();
		this.password = session.getPassword();

		this.connect();
	}


	private void connect()
		throws FilezillaException.NoConnection
	{
		try {
			this.client = new FTPClient();
			this.client.connect(this.host, Integer.parseInt(this.port));

			final int code = this.client.getReplyCode();
			if(!FTPReply.isPositiveCompletion(code))
				this.client.disconnect();

			this.client.login(this.username, this.password);

		} catch (final IOException exception) {

			throw new FilezillaException.NoConnection("Failed to connect to server " + this.host + ":" + this.port, exception);
		}
	}


	public void downloadFile(final String originPath, final Path targetPath)
		throws FilezillaException.DownloadFailed
	{
		try(OutputStream dataOut = Files.newOutputStream(targetPath)) {
			this.client.retrieveFile(originPath, dataOut);

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.DownloadFailed("Failed to download file '" + originPath + "' to path '" + targetPath + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.DownloadFailed("Failed to download file '" + originPath + "' to path '" + targetPath + "'", exception);
		}
	}

	public void uploadFile(final Path originPath, final String targetPath)
		throws FilezillaException.UploadFailed
	{
		final String rootPath = targetPath + originPath.getFileName().toString();

		try(InputStream dataIn = Files.newInputStream(originPath)) {
			this.client.storeFile(rootPath, dataIn);

			final String message = this.client.getReplyString();
			final int code = this.client.getReplyCode();

			if(!FTPReply.isPositiveCompletion(code))
				throw new FilezillaException.UploadFailed("Failed to upload file '" + originPath + "' to path '" + targetPath + "' with " + message);

		} catch (final IOException exception) {

			throw new FilezillaException.UploadFailed("Failed to upload file '" + originPath + "' to path '" + targetPath + "'", exception);
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