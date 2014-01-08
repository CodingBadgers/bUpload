package uk.codingbadgers.bUpload.handlers.auth;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class FTPAuthHandler extends AuthHandler {

	public static class FTPUserData implements Data {
		public String username;
		public char[] password;
		public String host;
		public int port;		
	}
	
	private FTPUserData data = new FTPUserData();
	
	public FTPAuthHandler(File database) {
		super(database);
	}

	public static FTPAuthHandler getInstance() {
		try {
			return (FTPAuthHandler) AuthTypes.FTP.getHandler();
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public JsonObject getSaveData() throws IOException {
		JsonObject object = new JsonObject();
		object.add("username", new JsonPrimitive(data.username));
		object.add("password", new JsonPrimitive(new String(data.password)));
			JsonObject server = new JsonObject();
			server.add("host", new JsonPrimitive(data.host));
			server.add("port", new JsonPrimitive(data.port));
		object.add("server", server);
		return object;
	}

	@Override
	public String getJsonReferance() {
        return "ftp";
	}

	@Override
	public void loadData(JsonObject json) throws IOException {
		data.username = json.get("username").getAsString();
		data.password = json.get("password").getAsString().toCharArray();
			JsonObject server = json.get("server").getAsJsonObject();
			data.host = server.get("host").getAsString();
			data.port = server.get("port").getAsInt();
	}

	@Override
	public void refreshData(JsonObject json) throws IOException {}

	@Override
	public FTPUserData getUserData() {
		return data;
	}
	
	@Override
	public String getUsername() {
		return data.username;
	}

	public String getHost() {
		return data.host;
	}

	public int getPort() {
		return data.port;
	}

	public String getPassword() {
		return new String(data.password);
	}


}
