package uk.codingbadgers.bUpload.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import uk.codingbadgers.bUpload.handlers.auth.AuthTypes;
import uk.codingbadgers.bUpload.manager.TranslationManager;

public class AuthGui extends bUploadGuiScreen {

	protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

	private static final int ADD_AUTH_OFFSET = 2 << 2;
	private static final int CANCEL = 1;

	public AuthGui(bUploadGuiScreen screen) {
		super(screen);
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawBackground();
		drawCenteredString(this.fontRendererObj, TranslationManager.getTranslation("image.auth.title"), width / 2, height / 5 - 20, 0xffffff);
		super.drawScreen(i, j, f);
	}

	@Override
	public void initGui() {
		int ypos = (height / 5);
		int buttonwidth = 160;

		for (AuthTypes handler : AuthTypes.values()) {
			GuiButton button = new GuiButton(handler.ordinal() + ADD_AUTH_OFFSET, width / 2 - (buttonwidth / 2), ypos, buttonwidth, 20, TranslationManager.getTranslation("image.auth.type." + handler.toString().toLowerCase()));
			this.addControl(button);
			ypos += 24;
		}

		ypos = (height / 5) * 4;
		addControl(new GuiButton(CANCEL, width / 2 - (buttonwidth / 2), ypos, buttonwidth, 20, TranslationManager.getTranslation("image.auth.cancel")));
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == CANCEL) {
			displayGuiScreen(parent);
			return;
		}

		try {
			AuthTypes type = AuthTypes.getByID(par1GuiButton.id - ADD_AUTH_OFFSET);
			AddAuthGui gui = type.getAuthGui(this);

			if (gui == null) {
				return;
			}

			displayGuiScreen(gui);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
