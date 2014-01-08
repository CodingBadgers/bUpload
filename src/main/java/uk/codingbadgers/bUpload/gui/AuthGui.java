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
		drawCenteredString(field_146289_q, TranslationManager.getTranslation("image.auth.title"), field_146294_l / 2, field_146295_m / 5 - 20, 0xffffff);
		super.drawScreen(i, j, f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		int ypos = (field_146295_m / 5);
		int buttonwidth = 160;

		for (AuthTypes handler : AuthTypes.values()) {
			GuiButton button = new GuiButton(handler.ordinal() + ADD_AUTH_OFFSET, field_146294_l / 2 - (buttonwidth / 2), ypos, buttonwidth, 20, TranslationManager.getTranslation("image.auth.type." + handler.toString().toLowerCase()));
			this.field_146292_n.add(button);
			ypos += 24;
		}

		ypos = (field_146295_m / 5) * 4;
		addControl(new GuiButton(CANCEL, field_146294_l / 2 - (buttonwidth / 2), ypos, buttonwidth, 20, TranslationManager.getTranslation("image.auth.cancel")));
	}

	@Override
	protected void func_146284_a(GuiButton par1GuiButton) {
		if (par1GuiButton.field_146127_k == CANCEL) {
			displayGuiScreen(parent);
			return;
		}

		try {
			AuthTypes type = AuthTypes.getByID(par1GuiButton.field_146127_k - ADD_AUTH_OFFSET);
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
