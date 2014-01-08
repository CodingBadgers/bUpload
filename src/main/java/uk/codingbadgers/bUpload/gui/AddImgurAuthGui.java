package uk.codingbadgers.bUpload.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import uk.codingbadgers.bUpload.handlers.MessageHandler;
import uk.codingbadgers.bUpload.handlers.auth.ImgurAuthHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class AddImgurAuthGui extends AddAuthGui {

	private static final String URL = "https://api.imgur.com/oauth2/authorize?client_id=" + ImgurAuthHandler.CLIENT_ID + "&response_type=pin&state=bUpload";
	private static final int ACCEPT = 0;
	private static final int CANCEL = 1;
	private GuiTextField pinCode;
	private boolean linkGiven = false;

	public AddImgurAuthGui(bUploadGuiScreen parent) {
	    super(parent);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (!pinCode.func_146176_q()) {
			pinCode.func_146195_b(true);
		}

		pinCode.func_146201_a(par1, par2);
		super.keyTyped(par1, par2);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.field_146127_k) {
		case ACCEPT:
			try {
				getAccessToken();
			} catch (Exception e) {
				e.printStackTrace();
			}
			parent.updateLogin();
			displayGuiScreen(parent);
			break;
		case CANCEL:
			parent.updateLogin();
			displayGuiScreen(parent);
			break;
		default:
			break;
		}
	}

	private void getAccessToken() throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost("https://api.imgur.com/oauth2/token");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("client_id", ImgurAuthHandler.CLIENT_ID));
		nameValuePairs.add(new BasicNameValuePair("client_secret", "d435f03cf62b7ec5589ae4f122354d4a435105d7"));
		nameValuePairs.add(new BasicNameValuePair("grant_type", "pin"));
		nameValuePairs.add(new BasicNameValuePair("pin", pinCode.func_146179_b()));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse resp = client.execute(post);
		String result = EntityUtils.toString(resp.getEntity());

		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(result).getAsJsonObject();

		if (json.has("success") && !json.get("success").getAsBoolean()) {
			MessageHandler.sendChatMessage(json.get("data").getAsJsonObject().get("error").getAsString());
			return;
		}

		String refresh = json.get("refresh_token").getAsString();
		ImgurAuthHandler.getInstance().setTokens(refresh);
	}

	@Override
	public void confirmClicked(boolean accepted, int par2) {
		if (accepted) {
			openLink();
			displayGuiScreen(this);
		} else {
			displayGuiScreen(parent);
		}
	}

	private void openLink() {
		try {
			Desktop dt = Desktop.getDesktop();
			dt.browse(URI.create(URL));
			linkGiven = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initGui() {
		if (!linkGiven) {
			if (this.field_146297_k.gameSettings.chatLinksPrompt) {
				displayGuiScreen(new GuiConfirmOpenLink(this, URL, 0, false));
			} else {
				openLink();
			}
		}

		int buttonWidth = 100;
		int buttonHeight = 20;

		addControl(new GuiButton(ACCEPT, this.field_146294_l / 2 - buttonWidth - 8, this.field_146295_m / 6 + 96, buttonWidth, buttonHeight,I18n.getStringParams("image.auth.accept")));
		addControl(new GuiButton(CANCEL, this.field_146294_l / 2 + 8, this.field_146295_m / 6 + 96, buttonWidth, buttonHeight, I18n.getStringParams("image.auth.cancel")));

		pinCode = new GuiTextField(this.field_146289_q, this.field_146294_l / 2 - buttonWidth - 8, this.field_146295_m / 6 + 64, buttonWidth * 2 + 16, buttonHeight);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawBackground();
		drawCenteredString(field_146289_q, I18n.getStringParams("image.imgur.login.ln1"), this.field_146294_l / 2, this.field_146295_m / 6 + 30, 0xFFFFFF);
		drawCenteredString(field_146289_q, I18n.getStringParams("image.imgur.login.ln2"), this.field_146294_l / 2, this.field_146295_m / 6 + 40, 0xFFFFFF);
		pinCode.func_146194_f();
		super.drawScreen(par1, par2, par3);
	}
}
