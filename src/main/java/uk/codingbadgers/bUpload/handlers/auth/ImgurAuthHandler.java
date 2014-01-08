package uk.codingbadgers.bUpload.handlers.auth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import uk.codingbadgers.bUpload.handlers.MessageHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class ImgurAuthHandler extends AuthHandler {

	public static final String CLIENT_ID = "e2d63c64042ba1a";

	public static class ImgurUserData implements Data {
		public String refreshToken = null;
		public String username = null;
	}

	private String accessToken = null;
	private Date tokenExpire = null;
	private ImgurUserData data = new ImgurUserData();

	public ImgurAuthHandler(File database) {
		super(database);
	}

	@Override
	public void loadData(JsonObject node) {
		try {
			refreshData(node);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JsonObject getSaveData() throws IOException {
		JsonObject object = new JsonObject();
		object.add("username", new JsonPrimitive(data.username));
		object.add("refresh_token", new JsonPrimitive(data.refreshToken));
		return object;
	}

	@Override
	public String getJsonReferance() {
		return "imgur";
	}

	@Override
	public void refreshData(JsonObject node) throws IOException {
		if (node == null) {
			return;
		}

		if (node.has("refresh_token")) {
			data.refreshToken = node.get("refresh_token").getAsString();
		}

		if (node.has("username")) {
			data.username = node.get("username").getAsString();
		}

		if (data.refreshToken == null || data.refreshToken.length() < 1) {
			return;
		}

		// refresh data		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
		nameValuePairs.add(new BasicNameValuePair("client_secret", "d435f03cf62b7ec5589ae4f122354d4a435105d7"));
		nameValuePairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
		nameValuePairs.add(new BasicNameValuePair("refresh_token", data.refreshToken));

		HttpPost post = new HttpPost("https://api.imgur.com/oauth2/token");
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse resp = client.execute(post);
		String result = EntityUtils.toString(resp.getEntity());

		JsonObject object = new JsonParser().parse(result).getAsJsonObject();

		if (object.has("access_token")) {
			data.refreshToken = object.get("refresh_token").getAsString();
			data.username = object.get("account_username").getAsString();
			accessToken = object.get("access_token").getAsString();
			tokenExpire = new Date(Calendar.getInstance().getTimeInMillis() + (object.get("expires_in").getAsInt() * 1000));

			saveData();
			MessageHandler.sendChatMessage("image.auth.login", "Imgur", data.username);
		} else {
			accessToken = null;
			tokenExpire = null;
			MessageHandler.sendChatMessage(object.get("data").getAsJsonObject().get("error").getAsString());
		}
	}

	@Override
	public void forgetData() throws IOException {
		accessToken = null;
		tokenExpire = null;
		data.refreshToken = "";
		data.username = null;

		saveData();
		MessageHandler.sendChatMessage("image.auth.logout", "Imgur");
	}

	@Override
	public boolean isLoggedIn() {
		return getAccessToken() != null;
	}

	@Override
	public String getUsername() {
		return data.username;
	}

	// Specific
	public void setTokens(String refresh) {
		data.refreshToken = refresh;

		try {
			saveData();
			refreshData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getAccessToken() {
		if (accessToken == null || tokenExpire == null || tokenExpire.before(new Date())) {
			try {
				refreshData();
			} catch (IOException e) {
				e.printStackTrace();
				MessageHandler.sendChatMessage(e.getMessage());
			}
		}

		return accessToken;
	}

	public static ImgurAuthHandler getInstance() {
		try {
			return (ImgurAuthHandler) AuthTypes.IMGUR.getHandler();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ImgurUserData getUserData() {
		return data;
	}

}
