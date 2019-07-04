package com.dardan.rrafshi.filezilla;

import org.apache.commons.net.ftp.FTP;


public enum FileType
{
	ASCII("ascii", FTP.ASCII_FILE_TYPE),

	EBCDIC("ebcdic", FTP.EBCDIC_FILE_TYPE),

	BINARY("binary", FTP.BINARY_FILE_TYPE),

	LOCAL("local", FTP.LOCAL_FILE_TYPE),
	;

	private final String name;
	private final int type;


	FileType(final String name, final int type)
	{
		this.name = name;
		this.type = type;
	}


	public String getName()
	{
		return this.name;
	}

	public int getType()
	{
		return this.type;
	}
}
