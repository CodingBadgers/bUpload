package uk.codingbadgers.bUpload.handlers.upload;

import com.google.common.base.Preconditions;
import uk.codingbadgers.bUpload.image.Screenshot;

import java.lang.reflect.Constructor;
import java.util.Locale;

public enum UploadType {

    FTP(FTPUploadHandler.class),
    IMGUR(ImgurUploadHandler.class),
    FILE(HDUploadHandler.class),
    TWITTER(TwitterUploadHandler.class),
    DROPBOX(DropboxUploadHandler.class),;

    private Constructor<? extends UploadHandler> ctor;
    private boolean providesUrl;

    private UploadType(Class<? extends UploadHandler> clazz) {
        try {
            this.ctor = clazz.getConstructor(Screenshot.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        this.providesUrl = hasInterface(clazz, URLProvider.class);
    }

    private boolean hasInterface(Class<?> clazz, Class<?> superface) {
        for (Class<?> current : clazz.getInterfaces()) {
            if (current == superface) {
                return true;
            }
        }
        return false;
    }

    public UploadHandler newHandler(Screenshot screen) {
        try {
            return ctor.newInstance(screen);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return ordinal();
    }

    public boolean providesUrl() {
        return providesUrl;
    }

    public String toString() {
        return name().toLowerCase(Locale.UK);
    }

    public static UploadHandler newHandler(String handlertype, Screenshot screen) {
        UploadType type = valueOf(handlertype.toUpperCase());

        if (type == null) {
            return null;
        }

        return type.newHandler(screen);
    }

    public static UploadType getById(int id) {
        Preconditions.checkArgument(id >= 0 && id < values().length, "Id must be between 0 and " + values().length);
        return values()[id];
    }
}
