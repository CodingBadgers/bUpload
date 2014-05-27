package uk.codingbadgers.bUpload.gui.auth;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.handlers.auth.TwitterAuthHandler;
import uk.codingbadgers.bUpload.manager.TranslationManager;

public class AddTwitterAuthGui extends AddAuthGui {

	private static final int ACCEPT = 0;
	private static final int CANCEL = 1;
	private GuiTextField pinCode;
	private GuiButton btnAccept;
	private GuiButton btnCancel;
	
	private boolean linkGiven = false;
	private boolean busy = false;
	private boolean init = false;

	public AddTwitterAuthGui(bUploadGuiScreen parent) {
		super(parent);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (!pinCode.isFocused()) {
			pinCode.setFocused(true);
		}

		pinCode.textboxKeyTyped(par1, par2);
		super.keyTyped(par1, par2);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
			case ACCEPT:
				busy = true;

				this.pinCode.setEnabled(false);
				this.btnAccept.enabled = false;
				this.btnCancel.enabled = false;

				Thread worker = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							getAccessToken();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						parent.updateLogin();
					}
					
				});
				worker.start();
				
				break;
			case CANCEL:
				parent.updateLogin();
				displayGuiScreen(parent);
				break;
			default:
				break;
		}
	}

	private void getAccessToken() throws TwitterException {
		TwitterAuthHandler twitter = TwitterAuthHandler.getInstance();
		AccessToken access = twitter.getTwitterInstance().getOAuthAccessToken(TwitterAuthHandler.getInstance().getRequestToken(), pinCode.getText());
		
		if (access == null) {
			displayGuiScreen(new AuthSuccessScreen(parent, TranslationManager.getTranslation("image.twitter.title"), TranslationManager.getTranslation("image.login.fail", TranslationManager.getTranslation("image.auth.type.twitter"))));
			return;
		}
		
		twitter.setAccessToken(access);
		displayGuiScreen(new AuthSuccessScreen(parent, TranslationManager.getTranslation("image.twitter.title"), TranslationManager.getTranslation("image.login.success", EnumChatFormatting.GOLD + access.getScreenName())));
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
			dt.browse(URI.create(TwitterAuthHandler.getInstance().getRequestToken().getAuthorizationURL()));
			linkGiven = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initGui() {
		if (TwitterAuthHandler.getInstance().isLoggedIn()) {
			displayGuiScreen(new TwitterAuthConfirmScreen(this, this.parent));
			return;
		}
		
		if (!linkGiven) {
			if (this.mc.gameSettings.chatLinksPrompt) {
				displayGuiScreen(new GuiConfirmOpenLink(this, TwitterAuthHandler.getInstance().getRequestToken().getAuthorizationURL(), 0, false));
			} else {
				openLink();
			}
		}

		int buttonWidth = 100;
		int buttonHeight = 20;

		btnAccept = addControl(new GuiButton(ACCEPT, this.width / 2 - buttonWidth - 8, this.height / 6 + 96, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.auth.accept")));
		btnCancel = addControl(new GuiButton(CANCEL, this.width / 2 + 8, this.height / 6 + 96, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.auth.cancel")));

		pinCode = new GuiTextField(this.fontRendererObj, this.width / 2 - buttonWidth - 8, this.height / 6 + 64, buttonWidth * 2 + 16, buttonHeight);
		init = true;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawBackground();
		
		drawCenteredString(this.fontRendererObj, EnumChatFormatting.UNDERLINE + TranslationManager.getTranslation("image.twitter.title"), this.width / 2, this.height / 6 + 20, 0xFFFFFF);
		drawCenteredString(this.fontRendererObj, TranslationManager.getTranslation("image.login.ln1"), this.width / 2, this.height / 6 + 32, 0xFFFFFF);
		drawCenteredString(this.fontRendererObj, TranslationManager.getTranslation("image.login.ln2", TranslationManager.getTranslation("image.auth.type.twitter")), this.width / 2, this.height / 6 + 44, 0xFFFFFF);
		
		pinCode.drawTextBox();
		super.drawScreen(par1, par2, par3);
		
		if (busy) {
			String message = EnumChatFormatting.BOLD + TranslationManager.getTranslation("image.auth.process").trim();
			int width = this.fontRendererObj.getStringWidth(message);
			
			int x = (this.width / 2) - ((width / 2) + 5);
			int y = (this.height / 6) + 66;
			
			Gui.drawRect(x, y, x + width + 10, y + (this.fontRendererObj.FONT_HEIGHT * 2) - 2, 0xBEBEBEFF);
			
			drawCenteredString(this.fontRendererObj, message, this.width / 2, y + 4, 0xFFFFFF);
		}
	}
	
	@Override
	public void resetGui() {
		if (!init) {
			return;
		}
		
		this.linkGiven = false;
		this.busy = false;
		
		this.pinCode.setEnabled(true);
		this.btnAccept.enabled = true;
		this.btnCancel.enabled = true;
	}
}
