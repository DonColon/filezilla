package com.dardan.rrafshi.filezilla.model;

import com.dardan.rrafshi.filezilla.FileTransferMode;
import com.dardan.rrafshi.filezilla.FileType;

public interface FilezillaSessionBuilder
{
	FilezillaSessionBuilder login(String username, String password);

	FilezillaSessionBuilder type(FileType fileType);

	FilezillaSessionBuilder transferMode(FileTransferMode mode);

	FilezillaSession build();

	String getHost();

	String getPort();

	String getUsername();

	String getPassword();

	FileType getFileType();

	FileTransferMode getFileTransferMode();
}
