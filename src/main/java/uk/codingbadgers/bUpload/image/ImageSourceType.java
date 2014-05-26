package uk.codingbadgers.bUpload.image;

public enum ImageSourceType {

    HD(HDImageSource.class),
    IMGUR(ImgurImageSource.class),
    FTP(FTPImageSource.class);

    private Class<? extends ImageSource> clazz;

    private ImageSourceType(Class<? extends ImageSource> clazz) {
        this.clazz = clazz;
    }
}
