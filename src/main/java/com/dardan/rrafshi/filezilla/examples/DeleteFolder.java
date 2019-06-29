package com.dardan.rrafshi.filezilla.examples;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dardan.rrafshi.commons.crypto.Passwords;
import com.dardan.rrafshi.filezilla.Constants;
import com.dardan.rrafshi.filezilla.FilezillaManager;
import com.dardan.rrafshi.filezilla.model.FilezillaSession;


public final class DeleteFolder
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFile.class);


	public static void main(final String[] args)
	{
		final FilezillaSession session = FilezillaSession.builder()
				.login(Constants.FTP_USERNAME, Passwords.getPasswordFromFile("filezilla"))
				.build();

		try(FilezillaManager filezillaManager = new FilezillaManager(session)) {
			filezillaManager.deleteFolder("/artists");

		} catch (final Exception exception) {

			LOGGER.error("Delete failed: ", exception);
		}

		LOGGER.info("Finished at " + LocalDateTime.now());
	}
}
