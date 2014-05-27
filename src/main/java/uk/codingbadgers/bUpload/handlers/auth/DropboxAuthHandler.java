package uk.codingbadgers.bUpload.handlers.auth;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class DropboxAuthHandler extends AuthHandler {

    private static final String APP_KEY = "gzeerr59rmsj6hk";

    private DbxAppInfo appInfo;
    private DbxClient client;

    private DropboxUserData data;
    private DbxRequestConfig config;

    public static class DropboxUserData implements Data {
        public String accessToken = "";
        public String displayName = "";
    }

    public DropboxAuthHandler(File database) {
        super(database);

        this.data = new DropboxUserData();
        this.appInfo = new DbxAppInfo(APP_KEY, "mcyqkp1j8mxon0r");
        this.config = new DbxRequestConfig("bUpload/1.5.0", Locale.getDefault().toString());
    }

    public static DropboxAuthHandler getInstance() {
        try {
            return (DropboxAuthHandler) AuthTypes.DROPBOX.getHandler();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void loadData(JsonObject json) throws IOException {
        data = new DropboxUserData();
        data.accessToken = json.get("accessToken").getAsString();
        data.displayName = json.get("displayName").getAsString();
        createClient();
    }

    @Override
    public JsonObject getSaveData() throws IOException {
        JsonObject json = new JsonObject();
        json.add("accessToken", new JsonPrimitive(data.accessToken));
        json.add("displayName", new JsonPrimitive(data.displayName));
        return json;
    }

    @Override
    public void refreshData(JsonObject json) throws IOException {
        data = new DropboxUserData();
        data.accessToken = json.get("accessToken").getAsString();
        data.displayName = json.get("displayName").getAsString();
        createClient();
    }

    @Override
    public String getUsername() {
        return data.displayName;
    }

    @Override
    public String getJsonReferance() {
        return "dropbox";
    }

    @Override
    public DropboxUserData getUserData() {
        return data;
    }

    @Override
    public boolean isLoggedIn() {
        return this.data.accessToken != null && !"".equals(this.data.accessToken);
    }

    @Override
    public void forgetData() throws IOException {
        data = new DropboxUserData();

        saveData();
        client = null;
    }

    public DbxRequestConfig getRequestConfig() {
        return config;
    }

    public DbxAppInfo getAppInfo() {
        return appInfo;
    }

    public DbxClient getClient() {
        return client;
    }

    public void setAccessToken(String accessToken) {
        this.data.accessToken = accessToken;

        try {
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        createClient();
    }

    private void createClient() {
        try {
            this.client = new DbxClient(getRequestConfig(), this.data.accessToken);
            this.data.displayName = this.client.getAccountInfo().displayName;
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }
}
