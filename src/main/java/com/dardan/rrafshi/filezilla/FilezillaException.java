package com.dardan.rrafshi.filezilla;

import com.dardan.rrafshi.commons.exceptions.ApplicationException;


public final class FilezillaException
{
	private FilezillaException() {}


	public static class NoConnection extends ApplicationException
	{
		private static final long serialVersionUID = 1L;


		public NoConnection(final String message, final Throwable cause)
		{
			super(message, cause);
		}

		public NoConnection(final String message)
		{
			super(message);
		}
	}

	public static class DownloadFailed extends ApplicationException
	{
		private static final long serialVersionUID = 1L;


		public DownloadFailed(final String message, final Throwable cause)
		{
			super(message, cause);
		}

		public DownloadFailed(final String message)
		{
			super(message);
		}
	}

	public static class UploadFailed extends ApplicationException
	{
		private static final long serialVersionUID = 1L;


		public UploadFailed(final String message, final Throwable cause)
		{
			super(message, cause);
		}

		public UploadFailed(final String message)
		{
			super(message);
		}
	}

	public static class CreateFailed extends ApplicationException
	{
		private static final long serialVersionUID = 1L;


		public CreateFailed(final String message, final Throwable cause)
		{
			super(message, cause);
		}

		public CreateFailed(final String message)
		{
			super(message);
		}
	}

	public static class DeleteFailed extends ApplicationException
	{
		private static final long serialVersionUID = 1L;


		public DeleteFailed(final String message, final Throwable cause)
		{
			super(message, cause);
		}

		public DeleteFailed(final String message)
		{
			super(message);
		}
	}

	public static class ListingFailed extends ApplicationException
	{
		private static final long serialVersionUID = 1L;


		public ListingFailed(final String message, final Throwable cause)
		{
			super(message, cause);
		}

		public ListingFailed(final String message)
		{
			super(message);
		}
	}
}
