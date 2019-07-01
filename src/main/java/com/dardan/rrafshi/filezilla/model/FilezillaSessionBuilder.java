package com.dardan.rrafshi.filezilla.model;

import com.dardan.rrafshi.filezilla.FileTransferMode;
import com.dardan.rrafshi.filezilla.FileType;

public interface FilezillaSessionBuilder
{
	FilezillaSessionBuilder login(String username, String password);

	FilezillaSessionBuilder fileType(FileType fileType);

	FilezillaSessionBuilder transferMode(FileTransferMode fileTransferMode);

	FilezillaSession build();

	String getHost();

	String getPort();

	String getUsername();

	String getPassword();

	FileType getFileType();

	FileTransferMode getFileTransferMode();
}
