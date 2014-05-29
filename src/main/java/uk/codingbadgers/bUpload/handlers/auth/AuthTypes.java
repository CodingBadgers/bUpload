package uk.codingbadgers.bUpload.handlers.auth;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;
import uk.codingbadgers.bUpload.bUpload;
import uk.codingbadgers.bUpload.gui.auth.AddAuthGui;
import uk.codingbadgers.bUpload.gui.auth.AddDropboxAuthGui;
import uk.codingbadgers.bUpload.gui.auth.AddFTPAuthGui;
import uk.codingbadgers.bUpload.gui.auth.AddImgurAuthGui;
import uk.codingbadgers.bUpload.gui.auth.AddTwitterAuthGui;
import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.manager.TranslationManager;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Map;

public enum AuthTypes {

    IMGUR(ImgurAuthHandler.class, AddImgurAuthGui.class),
    FTP(FTPAuthHandler.class, AddFTPAuthGui.class),
    TWITTER(TwitterAuthHandler.class, AddTwitterAuthGui.class),
    DROPBOX(DropboxAuthHandler.class, AddDropboxAuthGui.class);

    private static Map<AuthTypes, AuthHandler> handlercache = Maps.newHashMap();
    private static Map<AuthTypes, AddAuthGui> guiCache = Maps.newHashMap();
    private Constructor<? extends AuthHandler> handlerctor;
    private Constructor<? extends AddAuthGui> guictor;

    private AuthTypes(Class<? extends AuthHandler> handlerclazz, Class<? extends AddAuthGui> guiclazz) {
        try {
            this.handlerctor = handlerclazz.getConstructor(File.class);
            this.guictor = guiclazz.getConstructor(bUploadGuiScreen.class);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public AuthHandler getHandler() throws Exception {
        if (!handlercache.containsKey(this)) {
            handlercache.put(this, newHandler());
        }

        return handlercache.get(this);
    }

    private AuthHandler newHandler() throws Exception {
        return handlerctor.newInstance(bUpload.AUTH_DATABASE);
    }

    public AddAuthGui getAuthGui(bUploadGuiScreen screen) throws Exception {
        if (!guiCache.containsKey(this)) {
            guiCache.put(this, newGui(screen));
        }

        return guiCache.get(this);
    }

    private AddAuthGui newGui(bUploadGuiScreen screen) throws Exception {
        return guictor.newInstance(screen);
    }

    public String getLocalisedName() {
        return TranslationManager.getTranslation("image.auth.type." + this.name().toLowerCase());
    }

    public static AuthTypes getByID(int id) {
        Validate.inclusiveBetween(0, values().length - 1, id);
        return values()[id];
    }

    public static void loadData() {
        for (AuthTypes type : values()) {
            try {
                type.getHandler().loadData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
