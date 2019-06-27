package com.dardan.rrafshi.filezilla;

public interface FilezillaSessionBuilder
{
	FilezillaSessionBuilder login(String username, String password);

	FilezillaSession build();

	String getHost();

	String getPort();

	String getUsername();

	String getPassword();
}
