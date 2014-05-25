package uk.codingbadgers.bUpload.handlers.auth;

import java.io.File;
import java.io.IOException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class TwitterAuthHandler extends AuthHandler {

	private static final String CLIENT_ID = "tttUS8uZmaKd70jQGGEvBg";
	private static final String CLIENT_SECRET = "D65nOWUjrmzVzPyqlq7ZFL144uIhcwpAtIpdPOzydg";
	
	private Twitter twitter = null;
	private RequestToken request = null;
	private AccessToken access = null;
	private TwitterData data = new TwitterData();
	
	public static class TwitterData implements Data {
		public String accessToken;
		public String authSecret;
		public long userId;
		public String username;
	}
	
	public TwitterAuthHandler(File database) {
		super(database);
		
		try {
			twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer(CLIENT_ID, CLIENT_SECRET);
			request = twitter.getOAuthRequestToken();
		} catch (TwitterException ex) {
			ex.printStackTrace();
		}
	}

	public static TwitterAuthHandler getInstance() {
		try {
			return (TwitterAuthHandler) AuthTypes.TWITTER.getHandler();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void loadData(JsonObject json) throws IOException {
		this.data.accessToken = json.get("accessToken").getAsString();
		this.data.authSecret = json.get("authSecret").getAsString();
		this.data.userId = json.get("userId").getAsLong();
		this.data.username = json.get("username").getAsString();
		
		this.access = new AccessToken(this.data.accessToken, this.data.authSecret, this.data.userId);
		this.twitter.setOAuthAccessToken(access);
	}

	@Override
	public JsonObject getSaveData() throws IOException {
		JsonObject object = new JsonObject();
		object.add("accessToken", data.accessToken == null ? JsonNull.INSTANCE : new JsonPrimitive(data.accessToken));
		object.add("authSecret", data.authSecret == null ? JsonNull.INSTANCE : new JsonPrimitive(data.authSecret));
		object.add("username", data.username == null ? JsonNull.INSTANCE : new JsonPrimitive(data.username));
		object.add("userId", new JsonPrimitive(data.userId));
		return object;
	}

	@Override
	public void refreshData(JsonObject json) throws IOException {
		loadData(json);
	}

	@Override
	public String getUsername() {
		return this.data.username;
	}

	@Override
	public String getJsonReferance() {
		return "twitter";
	}

	@Override
	public Data getUserData() {
		return this.data;
	}
	
	public boolean isLoggedIn() {
		return twitter.getAuthorization().isEnabled();
	}
	
	public Twitter getTwitterInstance() {
		return twitter;
	}

	public RequestToken getRequestToken() {
		return request;
	}
	
	@Override
	public void forgetData() throws IOException {
		try {
			this.twitter.setOAuthAccessToken(null);
			this.access = null;
			this.data = new TwitterData();
			this.saveData();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public AccessToken getAccessToken() {
		return access;
	}

	public void setAccessToken(AccessToken access) {
		try {
			this.access = access;
			this.data.username = access.getScreenName();
			this.data.accessToken = access.getToken();
			this.data.authSecret = access.getTokenSecret();
			this.data.userId = access.getUserId();
			
			this.saveData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
