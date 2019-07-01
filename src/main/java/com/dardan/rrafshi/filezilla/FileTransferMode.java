package com.dardan.rrafshi.filezilla;

import org.apache.commons.net.ftp.FTP;


public enum FileTransferMode
{
	STREAM(FTP.STREAM_TRANSFER_MODE),

	BLOCK(FTP.BLOCK_TRANSFER_MODE),

	COMPRESSED(FTP.COMPRESSED_TRANSFER_MODE),
	;

	private final int mode;


	private FileTransferMode(final int mode)
	{
		this.mode = mode;
	}


	public int getMode()
	{
		return this.mode;
	}
}
