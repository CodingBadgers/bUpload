package uk.codingbadgers.bUpload.image;

public enum ImageSourceType {

	HD(HDImageSource.class),
	IMGUR(ImgurImageSource.class),
	FTP(FTPImageSource.class), 
	TWITTER(TwitterImageSource.class);

	@SuppressWarnings("unused")
	private Class<? extends ImageSource> clazz;

	private ImageSourceType(Class<? extends ImageSource> clazz) {
		this.clazz = clazz;
	}
}
