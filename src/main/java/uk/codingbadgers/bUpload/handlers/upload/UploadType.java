package uk.codingbadgers.bUpload.handlers.upload;

import java.lang.reflect.Constructor;

import uk.codingbadgers.bUpload.Screenshot;

public enum UploadType {

	FTP(FTPUploadHandler.class), 
	IMGUR(ImgurUploadHandler.class), 
	FILE(HDUploadHandler.class), 
	;

	private Constructor<? extends UploadHandler> ctor;

	private UploadType(Class<? extends UploadHandler> clazz) {
		try {
			this.ctor = clazz.getConstructor(Screenshot.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public UploadHandler newHandler(Screenshot screen) {
		try {
			return ctor.newInstance(screen);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static UploadHandler newHandler(String handlertype, Screenshot screen) {
		UploadType type = valueOf(handlertype);

		if (type == null) {
			return null;
		}

		return type.newHandler(screen);
	}
}
