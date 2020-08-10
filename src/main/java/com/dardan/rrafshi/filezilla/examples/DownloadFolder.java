package com.dardan.rrafshi.filezilla.examples;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dardan.rrafshi.commons.security.Passwords;
import com.dardan.rrafshi.filezilla.Constants;
import com.dardan.rrafshi.filezilla.FilezillaManager;
import com.dardan.rrafshi.filezilla.model.FilezillaPath;
import com.dardan.rrafshi.filezilla.model.FilezillaSession;


public final class DownloadFolder
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadFile.class);


	public static void main(final String[] args)
	{
		final FilezillaSession session = FilezillaSession.builder()
				.login(Constants.DEFAULT_FTP_USERNAME, Passwords.getPasswordFromFile("filezilla"))
				.build();

		try(FilezillaManager filezillaManager = new FilezillaManager(session)) {
			final Path targetPath = Paths.get("C:\\Users\\drraf\\Music\\Temp\\");
			final FilezillaPath originPath = FilezillaPath.parse("/artists/all time low");

			filezillaManager.downloadFolder(originPath, targetPath);

		} catch (final Exception exception) {

			LOGGER.error("Download failed: ", exception);
		}

		LOGGER.info("Finished at " + LocalDateTime.now());
	}
}
