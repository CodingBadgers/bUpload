package uk.codingbadgers.bUpload.gui;

import org.lwjgl.input.Keyboard;

import uk.codingbadgers.Gui.GuiPasswordField;
import uk.codingbadgers.bUpload.handlers.auth.FTPAuthHandler;
import uk.codingbadgers.bUpload.handlers.auth.FTPAuthHandler.FTPUserData;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class AddFTPAuthGui extends AddAuthGui {

	private static final int ACCEPT = 0;
	private static final int CANCEL = 1;

	private GuiTextField userTxtBox;
	private GuiPasswordField passTxtBox;
	private GuiTextField hostTxtBox;
	private GuiTextField portTxtBox;

	public AddFTPAuthGui(bUploadGuiScreen screen) {
		super(screen);
	}

	@Override
	protected void keyTyped(char c, int i) {

		if (i == Keyboard.KEY_TAB) {
			if (userTxtBox.func_146176_q()) {
				updateFocus(passTxtBox);
			} else if (passTxtBox.isFocused()) {
				updateFocus(hostTxtBox);
			} else if (hostTxtBox.func_146176_q()) {
				updateFocus(portTxtBox);
			} else if (portTxtBox.func_146176_q()) {
				updateFocus(userTxtBox);
			} else {
				updateFocus(userTxtBox);
			}

			return;
		}

		if (userTxtBox.func_146206_l()) {
			userTxtBox.func_146201_a(c, i);
		} else if (passTxtBox.isFocused()) {
			passTxtBox.textboxKeyTyped(c, i);
		} else if (hostTxtBox.func_146206_l()) {
			hostTxtBox.func_146201_a(c, i);
		} else if (portTxtBox.func_146206_l()) {
			portTxtBox.func_146201_a(c, i);
		}

		super.keyTyped(c, i);
	}

	private void updateFocus(GuiTextField field) {
		userTxtBox.func_146195_b(false);
		passTxtBox.setFocused(false);
		hostTxtBox.func_146195_b(false);
		portTxtBox.func_146195_b(false);

		field.func_146195_b(true);
	}

	private void updateFocus(GuiPasswordField field) {
		userTxtBox.func_146195_b(false);
		passTxtBox.setFocused(false);
		hostTxtBox.func_146195_b(false);
		portTxtBox.func_146195_b(false);

		field.setFocused(true);
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);

		this.userTxtBox.func_146192_a(par1, par2, par3);
		this.passTxtBox.mouseClicked(par1, par2, par3);
		this.hostTxtBox.func_146192_a(par1, par2, par3);
		this.portTxtBox.func_146192_a(par1, par2, par3);
	}

	@Override
	public void drawScreen(int i, int j, float k) {
		drawBackground();
		int textOffset = 24 / 2 - field_146289_q.FONT_HEIGHT / 2;
		int ypos = this.field_146295_m / 5;

		field_146289_q.drawString(I18n.getStringParams("image.auth.username"), this.field_146294_l / 2 - 108, ypos + textOffset, 0xFFFFFF);
		ypos += 24;
		field_146289_q.drawString(I18n.getStringParams("image.auth.password"), this.field_146294_l / 2 - 108, ypos + textOffset, 0xFFFFFF);
		ypos += 24;
		field_146289_q.drawString(I18n.getStringParams("image.auth.host"), this.field_146294_l / 2 - 108, ypos + textOffset, 0xFFFFFF);
		ypos += 24;
		field_146289_q.drawString(I18n.getStringParams("image.auth.port"), this.field_146294_l / 2 - 108, ypos + textOffset, 0xFFFFFF);

		userTxtBox.func_146194_f();
		passTxtBox.drawTextBox();
		hostTxtBox.func_146194_f();
		portTxtBox.func_146194_f();
		super.drawScreen(i, j, k);
	}

	@Override
	protected void func_146284_a(GuiButton par1GuiButton) {
		switch (par1GuiButton.field_146127_k) {
			case ACCEPT:
				FTPAuthHandler handler = FTPAuthHandler.getInstance();
				FTPUserData data = handler.getUserData();
				data.username = userTxtBox.func_146179_b();
				data.password = passTxtBox.getText().toCharArray();
				data.host = hostTxtBox.func_146179_b();
				data.port = Integer.parseInt(portTxtBox.func_146179_b());

				try {
					handler.saveData();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				displayGuiScreen(parent);
				break;
			case CANCEL:
				displayGuiScreen(parent);
				break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		int buttonWidth = 160;
		int buttonHeight = 20;
		int ypos = field_146295_m / 5;

		FTPUserData data = FTPAuthHandler.getInstance().getUserData();
		userTxtBox = new GuiTextField(this.field_146289_q, this.field_146294_l / 2 - (buttonWidth - 108), ypos, buttonWidth, buttonHeight);
		userTxtBox.func_146180_a(data.username == null ? "" : data.username);
		ypos += 24;
		passTxtBox = new GuiPasswordField(field_146289_q, this.field_146294_l / 2 - (buttonWidth - 108), ypos, buttonWidth, buttonHeight);
		passTxtBox.setText(new String(data.password == null ? new char[0] : data.password));
		ypos += 24;
		hostTxtBox = new GuiTextField(field_146289_q, this.field_146294_l / 2 - (buttonWidth - 108), ypos, buttonWidth, buttonHeight);
		hostTxtBox.func_146180_a(data.host == null ? "" : data.host);
		ypos += 24;
		portTxtBox = new GuiTextField(field_146289_q, this.field_146294_l / 2 - (buttonWidth - 108), ypos, buttonWidth, buttonHeight);
		portTxtBox.func_146180_a("" + (data.port == -1 ? 21 : data.port));
		ypos += 24;

		ypos = (field_146295_m / 5) * 4;

		buttonWidth = 100;
		this.field_146292_n.add(new GuiButton(ACCEPT, this.field_146294_l / 2 - buttonWidth - 8, ypos, buttonWidth, buttonHeight, I18n.getStringParams("image.auth.accept")));
		this.field_146292_n.add(new GuiButton(CANCEL, this.field_146294_l / 2 + 8, ypos, buttonWidth, buttonHeight, I18n.getStringParams("image.auth.cancel")));
	}

}
