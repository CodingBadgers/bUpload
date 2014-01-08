package uk.codingbadgers.bUpload.handlers.auth;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.utils.CipherUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public abstract class AuthHandler {

	public static interface Data {}
	
	private final File database;

	public AuthHandler(File database) {
		this.database = database;
	}

	public final void loadData() throws IOException {
		JsonObject json = getJson();
		
		if (!json.has("data") || !json.get("data").isJsonObject()) {
			return;
		}
		
		loadData(getJson().get("data").getAsJsonObject());
	}

	public abstract void loadData(JsonObject json) throws IOException;

	public final void saveData() throws IOException {
		JsonObject object = getSaveData();
		JsonObject data = getFullJson();

		JsonElement profiles = data.get("profiles");
		
		if (profiles == null || !profiles.isJsonArray()) {
			profiles = new JsonArray();
		}
		
		JsonArray profilesArray = profiles.getAsJsonArray();
		boolean found = false;

		for (JsonElement profile : profilesArray) {
			JsonObject objProfile = profile.getAsJsonObject();
			if (objProfile.getAsJsonObject().get("type").getAsString().equalsIgnoreCase(getJsonReferance())) {
				objProfile.remove("data");
				objProfile.add("data", object);
				found = true;
				break;
			}
		}

		if (!found) {
			JsonObject profile = new JsonObject();
			profile.add("type", new JsonPrimitive(getJsonReferance()));
			profile.add("data", object);
			profilesArray.add(profile);
		}

		data.remove("profiles");
		data.add("profiles", profilesArray);


		FileWriter writer = null;

		try {
			writer = new FileWriter(database);
			String output = data.toString();
			
			if (ConfigHandler.ENCRYPT_DATA) {
				output = CipherUtils.encryptString(output);
			}
			
			writer.write(output);
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	public abstract JsonObject getSaveData() throws IOException;

	public final void refreshData() throws IOException {
		JsonObject json = getJson();
		
		if (!json.has("data") || !json.get("data").isJsonObject()) {
			return;
		}
		
		refreshData(getJson().get("data").getAsJsonObject());
	}

	public abstract void refreshData(JsonObject json) throws IOException;

	public void forgetData() throws IOException {}

	public boolean isLoggedIn() throws IOException {
		return false;
	}

	public abstract String getUsername();

	public abstract String getJsonReferance();

	public abstract Data getUserData();
	
	private final JsonObject getJson() throws IOException {
		JsonObject json = getFullJson();

		if (json == null || !json.has("profiles")) {
			return new JsonObject();
		}
		
		for (JsonElement profile : json.get("profiles").getAsJsonArray()) {
			JsonObject objProfile = profile.getAsJsonObject();
			if (objProfile.getAsJsonObject().get("type").getAsString().equalsIgnoreCase(getJsonReferance())) {
				return objProfile;
			}
		}

		return new JsonObject();
	}

	private final JsonObject getFullJson() throws IOException {
		FileReader reader = null;

		try {
			JsonParser parser = new JsonParser();
			reader = new FileReader(database);
			
			String input = StringUtils.join(IOUtils.readLines(reader), "");
			
			if (ConfigHandler.ENCRYPT_DATA) {
				input = CipherUtils.decryptString(input);
			}
			
			JsonElement element = parser.parse(input);
			
			if (element == null || !element.isJsonObject()) {
				return new JsonObject();
			}
			
			return element.getAsJsonObject();
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return new JsonObject();
	}

}
