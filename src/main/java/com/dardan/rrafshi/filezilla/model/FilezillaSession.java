package com.dardan.rrafshi.filezilla.model;

import com.dardan.rrafshi.filezilla.Constants;

public final class FilezillaSession
{
	private final String host;
	private final String port;
	private final String username;
	private final String password;


	public FilezillaSession(final FilezillaSessionBuilder builder) {
		this.host = builder.getHost();
		this.port = builder.getPort();
		this.username = builder.getUsername();
		this.password = builder.getPassword();
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


	public static FilezillaSessionBuilder builder(final String host, final String port)
	{
		return new Builder(host, port);
	}

	public static FilezillaSessionBuilder builder()
	{
		return new Builder();
	}


	public static class Builder implements FilezillaSessionBuilder
	{
		private final String host;
		private final String port;
		private String username;
		private String password;


		public Builder(final String host, final String port)
		{
			this.host = host;
			this.port = port;
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
	}
}
