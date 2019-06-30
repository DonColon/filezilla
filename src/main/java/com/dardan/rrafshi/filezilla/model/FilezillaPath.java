package com.dardan.rrafshi.filezilla.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.dardan.rrafshi.commons.Strings;


public final class FilezillaPath
{
	private final List<String> pathSegments;
	private final String rootSegment;


	private FilezillaPath(final FilezillaPathBuilder builder)
	{
		this.pathSegments = builder.getPathSegments();
		this.rootSegment = builder.getRootSegment();
	}


	@Override
	public String toString()
	{
		final String path = Strings.valueOf(this.pathSegments, "/");

		if(Strings.isNotBlank(this.rootSegment))
			return this.rootSegment + path;
		else
			return path;
	}

	@Override
	public boolean equals(final Object object)
	{
		if (this == object) return true;
		if (object == null) return false;

		if (this.getClass() != object.getClass())
			return false;

		final FilezillaPath other = (FilezillaPath) object;
		return Objects.equals(this.pathSegments, other.pathSegments)
			&& Objects.equals(this.rootSegment, other.rootSegment);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.pathSegments, this.rootSegment);
	}


	public FilezillaPath resolve(final FilezillaPath other)
	{
		final FilezillaPathBuilder builder = builder()
				.addRootSegment(this.rootSegment);

		for(final String pathSegment : this.pathSegments)
			builder.addPathSegment(pathSegment);

		for(final String pathSegment : other.pathSegments)
			builder.addPathSegment(pathSegment);

		return builder.build();
	}

	public FilezillaPath subpath(final int start, final int end)
	{
		if(this.pathSegments.isEmpty())
			return null;

		if(start < 0 || start > this.pathSegments.size())
			throw new IllegalArgumentException("start index should be in range");

		if(end < 0 || end > this.pathSegments.size())
			throw new IllegalArgumentException("end index should be in range");

		if(end < start)
			throw new IllegalArgumentException("end index should be greater than start index");

		final FilezillaPathBuilder pathBuilder = builder();
		for(int i = start; i < end; i++)
			pathBuilder.addPathSegment(this.pathSegments.get(i));

		return pathBuilder.build();
	}

	public FilezillaPath getRoot()
	{
		if(Strings.isBlank(this.rootSegment))
			return null;

		return builder()
				.addRootSegment(this.rootSegment)
				.build();
	}

	public FilezillaPath getFileName()
	{
		return this.getName(this.pathSegments.size() - 1);
	}

	public FilezillaPath getName(final int index)
	{
		if(index < 0 || index > this.pathSegments.size())
			return null;

		return builder()
				.addPathSegment(this.pathSegments.get(index))
				.build();
	}

	public FilezillaPath getParent()
	{
		final FilezillaPathBuilder pathBuilder = builder()
				.addRootSegment(this.rootSegment);

		for(int i = 0; i < this.pathSegments.size() - 1; i++)
			pathBuilder.addPathSegment(this.pathSegments.get(i));

		return pathBuilder.build();
	}

	public int getNameCount()
	{
		return this.pathSegments.size();
	}


	public static FilezillaPath parse(final String path)
	{
		final FilezillaPathBuilder builder = builder()
				.addPathSegments(path);

		if(Strings.indexOf(path, "/") == 0)
			builder.addRootSegment("/");

		return builder.build();
	}

	public static FilezillaPathBuilder builder()
	{
		return new Builder();
	}


	private static class Builder implements FilezillaPathBuilder
	{
		private final List<String> pathSegments = new ArrayList<>();
		private String rootSegment;


		@Override
		public FilezillaPathBuilder addRootSegment(final String rootSegment)
		{
			this.rootSegment = rootSegment;
			return this;
		}

		@Override
		public FilezillaPathBuilder addPathSegment(final String pathSegment)
		{
			if(Strings.isBlank(pathSegment))
				return this;

			if(Strings.containsAny(pathSegment, '\\', '/', ':', '*', '?', '"', '<', '>', '|'))
				throw new IllegalArgumentException("pathSegment shoul not contain \\ / : * ? \" < > |");

			this.pathSegments.add(pathSegment);
			return this;
		}

		@Override
		public FilezillaPathBuilder addPathSegments(final String pathSegments)
		{
			String path = pathSegments;

			if(Strings.indexOf(pathSegments, '/') == 0)
				path = Strings.substring(path, 1);

			final String[] segments = Strings.split(path, '/');

			for(final String segment : segments)
				this.addPathSegment(segment);

			return this;
		}

		@Override
		public FilezillaPath build()
		{
			return new FilezillaPath(this);
		}

		@Override
		public List<String> getPathSegments()
		{
			return Collections.unmodifiableList(this.pathSegments);
		}

		@Override
		public String getRootSegment()
		{
			return this.rootSegment;
		}
	}
}
