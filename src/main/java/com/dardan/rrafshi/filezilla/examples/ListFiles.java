package com.dardan.rrafshi.filezilla.examples;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dardan.rrafshi.commons.crypto.Passwords;
import com.dardan.rrafshi.filezilla.Constants;
import com.dardan.rrafshi.filezilla.FilezillaManager;
import com.dardan.rrafshi.filezilla.model.FilezillaPath;
import com.dardan.rrafshi.filezilla.model.FilezillaSession;

public final class ListFiles
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFile.class);


	public static void main(final String[] args)
	{
		final FilezillaSession session = FilezillaSession.builder()
				.login(Constants.FTP_USERNAME, Passwords.getPasswordFromFile("filezilla"))
				.build();

		try(FilezillaManager filezillaManager = new FilezillaManager(session)) {
			final FilezillaPath targetPath = FilezillaPath.parse("/artists/all time low/future hearts");

			final List<FilezillaPath> paths = filezillaManager.listFiles(targetPath);

			for(final FilezillaPath path : paths)
				LOGGER.info(path.toString());

		} catch (final Exception exception) {

			LOGGER.error("Delete failed: ", exception);
		}

		LOGGER.info("Finished at " + LocalDateTime.now());
	}
}
