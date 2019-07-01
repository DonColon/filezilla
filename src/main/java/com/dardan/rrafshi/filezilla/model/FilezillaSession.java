package com.dardan.rrafshi.filezilla.model;

import com.dardan.rrafshi.filezilla.Constants;
import com.dardan.rrafshi.filezilla.FileTransferMode;
import com.dardan.rrafshi.filezilla.FileType;


public final class FilezillaSession
{
	private final String host;
	private final String port;
	private final String username;
	private final String password;

	private final FileType fileType;
	private final FileTransferMode fileTransferMode;


	private FilezillaSession(final FilezillaSessionBuilder builder)
	{
		this.host = builder.getHost();
		this.port = builder.getPort();
		this.username = builder.getUsername();
		this.password = builder.getPassword();
		this.fileType = builder.getFileType();
		this.fileTransferMode = builder.getFileTransferMode();
	}


	public FilezillaSessionBuilder newBuilder()
	{
		final Builder builder = new Builder();

		builder.host = this.host;
		builder.port = this.port;
		builder.username = this.username;
		builder.password = this.password;
		builder.fileType = this.fileType;
		builder.fileTransferMode = this.fileTransferMode;

		return builder;
	}

	public String getHost()
	{
		return this.host;
	}

	public String getPort()
	{
		return this.port;
	}

	public String getUsername()
	{
		return this.username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public FileTransferMode getFileTransferMode()
	{
		return this.fileTransferMode;
	}

	public FileType getFileType()
	{
		return this.fileType;
	}


	public static FilezillaSessionBuilder builder(final String host, final String port)
	{
		return new Builder(host, port);
	}

	public static FilezillaSessionBuilder builder()
	{
		return new Builder();
	}


	private static class Builder implements FilezillaSessionBuilder
	{
		private String host;
		private String port;
		private String username;
		private String password;

		private FileType fileType;
		private FileTransferMode fileTransferMode;


		public Builder(final String host, final String port)
		{
			this.host = host;
			this.port = port;
			this.fileType = FileType.BINARY;
			this.fileTransferMode = FileTransferMode.STREAM;
		}

		public Builder()
		{
			this(Constants.DEFAULT_FTP_HOST, Constants.DEFAULT_FTP_PORT);
		}


		@Override
		public FilezillaSessionBuilder login(final String username, final String password)
		{
			this.username = username;
			this.password = password;

			return this;
		}

		@Override
		public FilezillaSessionBuilder fileType(final FileType fileType)
		{
			this.fileType = fileType;
			return this;
		}

		@Override
		public FilezillaSessionBuilder transferMode(final FileTransferMode fileTransferMode)
		{
			this.fileTransferMode = fileTransferMode;
			return this;
		}

		@Override
		public FilezillaSession build()
		{
			return new FilezillaSession(this);
		}

		@Override
		public String getHost()
		{
			return this.host;
		}

		@Override
		public String getPort()
		{
			return this.port;
		}

		@Override
		public String getUsername()
		{
			return this.username;
		}

		@Override
		public String getPassword()
		{
			return this.password;
		}

		@Override
		public FileType getFileType()
		{
			return this.fileType;
		}

		@Override
		public FileTransferMode getFileTransferMode()
		{
			return this.fileTransferMode;
		}
	}
}
