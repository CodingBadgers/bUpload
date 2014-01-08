package uk.codingbadgers.bUpload.handlers.auth;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.I18n;

import uk.codingbadgers.bUpload.bUpload;
import uk.codingbadgers.bUpload.gui.AddAuthGui;
import uk.codingbadgers.bUpload.gui.AddFTPAuthGui;
import uk.codingbadgers.bUpload.gui.AddImgurAuthGui;
import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;

public enum AuthTypes {

    IMGUR(ImgurAuthHandler.class, AddImgurAuthGui.class),
    FTP(FTPAuthHandler.class, AddFTPAuthGui.class),
    ;
    
    private static Map<Integer, String> by_id = new HashMap<Integer, String>();
    private static Map<AuthTypes, AuthHandler> handlercache = new HashMap<AuthTypes, AuthHandler>();
    private static Map<AuthTypes, AddAuthGui> guicache = new HashMap<AuthTypes, AddAuthGui>();
    private Constructor<? extends AuthHandler> handlerctor;
    private Constructor<? extends AddAuthGui> guictor;
    
    static {
    	for (AuthTypes type : values()) {
    		by_id.put(type.ordinal(), type.toString());
    	}
    }

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
    	if (!guicache.containsKey(this)) {
    		guicache.put(this, newGui(screen));
    	}
    	
    	return guicache.get(this);
    }

	private AddAuthGui newGui(bUploadGuiScreen screen) throws Exception {
		return guictor.newInstance(screen);
	}

	public String getLocalisedName() {
		return I18n.getStringParams("image.auth.type." + this.name().toLowerCase());
	}

	public static AuthTypes getByID(int id) {
		String type = by_id.get(id);
		
		if (type == null) {
			return null;
		}
		
		return valueOf(type);
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
