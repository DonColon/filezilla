package com.dardan.rrafshi.filezilla.examples;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dardan.rrafshi.commons.crypto.Passwords;
import com.dardan.rrafshi.filezilla.Constants;
import com.dardan.rrafshi.filezilla.FilezillaManager;
import com.dardan.rrafshi.filezilla.model.FilezillaPath;
import com.dardan.rrafshi.filezilla.model.FilezillaSession;


public final class UploadFile
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadFile.class);


	public static void main(final String[] args)
	{
		final FilezillaSession session = FilezillaSession.builder()
				.login(Constants.FTP_USERNAME, Passwords.getPasswordFromFile("filezilla"))
				.build();

		try(FilezillaManager filezillaManager = new FilezillaManager(session)) {
			final Path originPath = Paths.get("C:\\Users\\drraf\\Music\\Artists\\All Time Low\\Future Hearts\\Something's Gotta Give.mp3");
			final FilezillaPath targetPath = FilezillaPath.parse("/artists/all time low/future hearts");

			filezillaManager.uploadFile(originPath, targetPath);

		} catch (final Exception exception) {

			LOGGER.error("Download failed: ", exception);
		}

		LOGGER.info("Finished at " + LocalDateTime.now());
	}
}
