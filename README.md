# filezilla

filezilla is a ftp client written in java inspired by the popular http client okhttp. It uses the builder design pattern to easily construct a path on the server or the configuration of a session. In the core it uses the apache commons net ftp client and wraps the methods in a simple and clean API. Currently it only supports ftp, but in the future it will support ftps and sftp.

## Installation

To use the FilezillaManager, you only have to add the artifact to your dependencies section inside your pom.xml and then you are ready to go.

```xml
	<dependency>
		<groupId>com.dardan.rrafshi</groupId>
		<artifactId>filezilla</artifactId>
		<version>1.0.0</version>
	</dependency>
```

To create a <code>FilezillaManager</code> object you need to specify the session data first. Therefore you have to a <code>FilezillaSession</code> object. Then you can use the use the methods to operate CRUDL operations on files and folders on the file server. The default file type for a session is <code>FileType.BINARY</code> and the default transfer mode is <code>FileTransferMode.STREAM</code>.

```java
	final FilezillaSession session = FilezillaSession.builder()
				.login(Constants.DEFAULT_FTP_USERNAME, Passwords.getPasswordFromFile("filezilla"))
				.fileType(FileType.ASCII)
				.transferMode(FileTransferMode.COMPRESSED)
				.build();
				
	FilezillaManager filezillaManager = new FilezillaManager(session);
```

## Download a file/folder

```java
	try(FilezillaManager filezillaManager = new FilezillaManager(session)) {
		// Download a file
		FilezillaPath originPath = FilezillaPath.parse("/artists/all time low/future hearts/Something's Gotta Give.mp3");
		Path targetPath = Paths.get("C:\\Users\\{username}\\Music\\Temp\\Something's Gotta Give.mp3");
		
		filezillaManager.downloadFile(originPath, targetPath);
		
		// Download a folder
		originPath = FilezillaPath.parse("/artists/all time low");
		targetPath = Paths.get("C:\\Users\\drraf\\Music\\Temp\\");
		
		filezillaManager.downloadFolder(originPath, targetPath);
	}
```

## Upload a file/folder

```java
	try(FilezillaManager filezillaManager = new FilezillaManager(session)) {
		// Upload a file
		Path originPath = Paths.get("C:\\Users\\{username}\\Music\\Artists\\All Time Low\\Future Hearts\\Something's Gotta Give.mp3");
		FilezillaPath targetPath = FilezillaPath.parse("/artists/all time low/future hearts");

		filezillaManager.uploadFile(originPath, targetPath);
		
		// Upload a folder
		originPath = Paths.get("C:\\Users\\drraf\\Music\\Temp\\all time low");
		targetPath = FilezillaPath.parse("/artists");

		filezillaManager.uploadFolder(originPath, targetPath);
	}
```


