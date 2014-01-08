/*
 *  bUpload - a minecraft mod which improves the existing screenshot functionality
 *  Copyright (C) 2013 TheCodingBadgers
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */
package uk.codingbadgers.bUpload.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.lwjgl.opengl.GL11;

import uk.codingbadgers.bUpload.UploadedImage;
import uk.codingbadgers.bUpload.handlers.HistoryHandler;
import uk.codingbadgers.bUpload.handlers.MessageHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class UploadHistoryGUI extends bUploadGuiScreen {

	private static final int COTAINER_WIDTH = 176;
	private static final int CONTAINER_HIGHT = 222;

	private static final int PREVIOUS = 0;
	private static final int NEXT = 1;
	private static final int SETTINGS = 2;

	private int m_currentImage = 0;

	/**
	 * Default constructor
	 */
	public UploadHistoryGUI(bUploadGuiScreen screen) {
		super(screen);
	}

	/**
	 * Initialise the gui, adding buttons to the screen
	 */
	public void initGui() {
		this.field_146292_n.clear();
		addControl(new GuiButton(PREVIOUS, (field_146294_l / 2) - (COTAINER_WIDTH / 2) - (70), ((field_146295_m / 2) - (CONTAINER_HIGHT / 2) + CONTAINER_HIGHT) - 25, 60, 20, I18n.getStringParams("image.history.previous")));
		addControl(new GuiButton(NEXT, (field_146294_l / 2) + (COTAINER_WIDTH / 2) + (10), ((field_146295_m / 2) - (CONTAINER_HIGHT / 2) + CONTAINER_HIGHT) - 25, 60, 20, I18n.getStringParams("image.history.next")));

		addControl(new GuiButton(SETTINGS, (field_146294_l / 2) + (COTAINER_WIDTH / 2) + (10), (this.field_146295_m / 2) - (CONTAINER_HIGHT / 2) + 5, 60, 20, I18n.getStringParams("image.history.settings")));
	}

	/**
	 * Draw the container image to the screen
	 * 
	 * @param
	 * @param
	 * @param
	 */
	public void drawScreen(int i, int j, float f) {
		Minecraft minecraft = Minecraft.getMinecraft();
		drawBackground();
		// load our container image
		minecraft.renderEngine.bindTexture(new ResourceLocation("bUpload:textures/gui/bupload-history.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect((field_146294_l / 2) - (COTAINER_WIDTH / 2), (field_146295_m / 2) - (CONTAINER_HIGHT / 2), 0, 0, COTAINER_WIDTH, CONTAINER_HIGHT);
		UploadedImage imageInfo = HistoryHandler.getUploadedImage(m_currentImage);

		if (imageInfo != null) {
			// draw the image information
			int yOffset = 132;
			drawCenteredString(minecraft.fontRenderer, imageInfo.getName(), (field_146294_l / 2), ((field_146295_m / 2) - (CONTAINER_HIGHT / 2)) + yOffset, 0xFFFFFFFF);
			yOffset += 16;

			if (!imageInfo.isLocal()) {
				drawCenteredString(minecraft.fontRenderer, imageInfo.getUrl(), (field_146294_l / 2), ((field_146295_m / 2) - (CONTAINER_HIGHT / 2)) + yOffset, 0xFFFFAA00);
			} else {
				drawCenteredString(minecraft.fontRenderer, I18n.getStringParams("image.history.open"), (field_146295_m / 2), ((field_146295_m / 2) - (CONTAINER_HIGHT / 2)) + yOffset, 0xFFFFAA00);
			}

			// draw the image preview
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, imageInfo.getImageID());
			drawTexturedModalRectSized((field_146294_l / 2) - (COTAINER_WIDTH / 2) + 8, (field_146295_m / 2) - (CONTAINER_HIGHT / 2) + 18, 0, 0, 160, 101, 256, 256);
		} else {
			drawCenteredString(minecraft.fontRenderer, I18n.getStringParams("image.history.empty"), (field_146294_l / 2), ((field_146295_m / 2) - (CONTAINER_HIGHT / 2)) + 132, 0xFFFFFFFF);
		}

		super.drawScreen(i, j, f);
	}

	/**
	 * Called when a button is pressed by a user
	 */
	public void actionPerformed(GuiButton button) {
		switch (button.field_146127_k) {
			case PREVIOUS: {
				m_currentImage--;

				if (m_currentImage < 0) {
					m_currentImage = HistoryHandler.uploadHistorySize() - 1;
				}

				if (m_currentImage < 0) {
					m_currentImage = 0;
				}

				break;
			}

			case NEXT: {
				m_currentImage++;

				if (m_currentImage >= HistoryHandler.uploadHistorySize()) {
					m_currentImage = 0;
				}

				break;
			}

			case SETTINGS: {
				displayGuiScreen(new SettingsGui(this));
				break;
			}
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);

		if (x < (field_146294_l / 2) - (COTAINER_WIDTH / 2) + 12) {
			return;
		}

		if (x > (field_146294_l / 2) - (COTAINER_WIDTH / 2) + COTAINER_WIDTH - 12) {
			return;
		}

		if (y < ((field_146295_m / 2) - (CONTAINER_HIGHT / 2)) + 148) {
			return;
		}

		if (y > ((field_146295_m / 2) - (CONTAINER_HIGHT / 2)) + 158) {
			return;
		}

		UploadedImage imageInfo = HistoryHandler.getUploadedImage(m_currentImage);

		if (imageInfo != null) {
			if (!imageInfo.isLocal()) {
				if (this.field_146297_k.gameSettings.chatLinksPrompt) {
					displayGuiScreen(new GuiConfirmOpenLink(this, imageInfo.getUrl(), 0, false));
				} else {
					openUrl();
				}
			} else {
				Desktop dt = Desktop.getDesktop();
				try {
					dt.open(new File(imageInfo.getUrl()));
				} catch (IOException e) {
					this.field_146297_k.func_147108_a(null);
					MessageHandler.sendChatMessage("image.history.open.fail.1");
					MessageHandler.sendChatMessage("image.history.open.fail.2");
					try {
						dt.open(new File(imageInfo.getUrl().replace(imageInfo.getName(), "")));
					} catch (IOException e1) {
						MessageHandler.sendChatMessage("image.history.open.fail.3");
					}
				}
			}
		}
	}

	/**
	 * Called when the user clicks a button on the 'should i open that link'
	 * gui
	 */
	public void confirmClicked(boolean openUrl, int par2) {
		if (openUrl) {
			openUrl();
		}

		displayGuiScreen(this);
	}

	public void openUrl() {
		UploadedImage imageInfo = HistoryHandler.getUploadedImage(m_currentImage);

		if (imageInfo != null) {
			try {
				Desktop dt = Desktop.getDesktop();
				dt.browse(URI.create(imageInfo.getUrl()));
			} catch (Throwable var4) {
				var4.printStackTrace();
			}
		}
	}
}
