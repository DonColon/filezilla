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


public final class UploadFolder
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadFile.class);


	public static void main(final String[] args)
	{
		final FilezillaSession session = FilezillaSession.builder()
				.login(Constants.DEFAULT_FTP_USERNAME, Passwords.getPasswordFromFile("filezilla"))
				.build();

		try(FilezillaManager filezillaManager = new FilezillaManager(session)) {
			final Path originPath = Paths.get("C:\\Users\\drraf\\Music\\Temp\\all time low");
			final FilezillaPath targetPath = FilezillaPath.parse("/artists");

			filezillaManager.uploadFolder(originPath, targetPath);

		} catch (final Exception exception) {

			LOGGER.error("Upload failed: ", exception);
		}

		LOGGER.info("Finished at " + LocalDateTime.now());
	}
}
