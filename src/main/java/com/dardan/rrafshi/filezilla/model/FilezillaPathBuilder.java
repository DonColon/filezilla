package com.dardan.rrafshi.filezilla.model;

import java.util.List;

public interface FilezillaPathBuilder
{
	FilezillaPathBuilder addRootSegment(String rootSegment);

	FilezillaPathBuilder addPathSegment(String pathSegment);

	FilezillaPathBuilder addPathSegments(String pathSegments);

	FilezillaPath build();

	List<String> getPathSegments();

	String getRootSegment();
}
