package com.dardan.rrafshi.filezilla.examples;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dardan.rrafshi.commons.security.Passwords;
import com.dardan.rrafshi.filezilla.Constants;
import com.dardan.rrafshi.filezilla.FilezillaManager;
import com.dardan.rrafshi.filezilla.model.FilezillaPath;
import com.dardan.rrafshi.filezilla.model.FilezillaSession;


public final class CreateFolder
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFile.class);


	public static void main(final String[] args)
	{
		final FilezillaSession session = FilezillaSession.builder()
				.login(Constants.DEFAULT_FTP_USERNAME, Passwords.getPasswordFromFile("filezilla"))
				.build();

		try(FilezillaManager filezillaManager = new FilezillaManager(session)) {
			final FilezillaPath path = FilezillaPath.parse("/artists/all time low/future hearts");

			filezillaManager.createFolders(path);

		} catch (final Exception exception) {

			LOGGER.error("Create failed: ", exception);
		}

		LOGGER.info("Finished at " + LocalDateTime.now());
	}
}
