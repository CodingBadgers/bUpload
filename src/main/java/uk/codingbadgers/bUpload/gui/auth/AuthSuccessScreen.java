package uk.codingbadgers.bUpload.gui.auth;

import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.manager.TranslationManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;

public class AuthSuccessScreen extends bUploadGuiScreen {

	private static final int OK = 0;
	private String result;
	private String title;

	public AuthSuccessScreen(bUploadGuiScreen screen, String title, String message) {
		super(screen);
		this.result = message;
		this.title = title;
	}

	@Override
	public void initGui() {
		int buttonWidth = 216;
		int buttonHeight = 20;

		addControl(new GuiButton(OK, (this.width / 2) - (buttonWidth / 2), this.height / 6 + 96, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.login.ok")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
			case OK:
				displayGuiScreen(parent);
				break;
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawBackground();
		drawCenteredString(this.fontRendererObj, EnumChatFormatting.UNDERLINE + title, this.width / 2, (this.height / 6) + 30, 0xFFFFFF);
		drawCenteredString(this.fontRendererObj, this.result, this.width / 2, (this.height / 6) + 42, 0xFFFFFF);
		super.drawScreen(par1, par2, par3);
	}

}
