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

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import uk.codingbadgers.bUpload.handlers.HistoryHandler;
import uk.codingbadgers.bUpload.image.ImageSource;
import uk.codingbadgers.bUpload.image.UploadedImage;
import uk.codingbadgers.bUpload.manager.TranslationManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class UploadHistoryGUI extends bUploadGuiScreen {

    private static final int CONTAINER_WIDTH = 176;
	private static final int CONTAINER_HEIGHT = 222;

	private static final int PREVIOUS = 0;
	private static final int NEXT = 1;
	private static final int SETTINGS = 2;
	private static final int EXIT = 3;
    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("bUpload:textures/gui/bupload-history.png");

    private int m_currentImage = 0;

	private GuiScreen screen;

	public UploadHistoryGUI(GuiScreen screen) {
		super(screen instanceof bUploadGuiScreen ? (bUploadGuiScreen) screen : null);
		
		this.screen = screen;
	}

	/**
	 * Initialise the gui, adding buttons to the screen
	 */
	@Override
	public void initGui() {
		this.buttonList.clear();
		
		int buttonHeight = 20;
		int buttonWidth = 50;
		int ypos =  ((height / 2) - (CONTAINER_HEIGHT / 2) + CONTAINER_HEIGHT) - 25;
		
		addControl(new GuiButton(PREVIOUS, (width / 2) - (80), ypos, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.history.previous")));
		addControl(new GuiButton(NEXT, (width / 2) + (30), ypos, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.history.next")));
		addControl(new GuiButton(EXIT, (width / 2) - (25), ypos, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.history.exit")));
		
		addControl(new GuiButton(SETTINGS, (width / 2) + (CONTAINER_WIDTH / 2) + (10), (this.height / 2) - (CONTAINER_HEIGHT / 2) + 5, 60, 20, TranslationManager.getTranslation("image.history.settings")));
	}

	/**
	 * Draw the container image to the screen
	 * 
	 * @param
	 * @param
	 * @param
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		Minecraft minecraft = Minecraft.getMinecraft();
		drawBackground();
        int xPos = (width / 2) - (CONTAINER_WIDTH / 2);
        int yPos = (height / 2) - (CONTAINER_HEIGHT / 2);

        // load our container image
		minecraft.renderEngine.bindTexture(BACKGROUND_TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(xPos, yPos, 0, 0, CONTAINER_WIDTH, CONTAINER_HEIGHT);

		UploadedImage imageInfo = HistoryHandler.getUploadedImage(m_currentImage);
        super.drawScreen(mouseX, mouseY, f);

        drawRect(xPos + 8, yPos + 144, xPos + 168, yPos + 144 + (4 * 10), -1);
		if (imageInfo != null) {
			// draw the image information
			int yOffset = 132;
			drawCenteredString(minecraft.fontRenderer, imageInfo.getName(), (width / 2), yPos + yOffset, 0xFFFFFFFF);
			yOffset += 12;

            int sources = 0;
			for (ImageSource source : imageInfo.getSources()) {
				drawCenteredString(minecraft.fontRenderer, source.getDescription(), (width / 2), yPos + yOffset, 0xFFFFAA00);
				yOffset += 10;
                sources++;
			}
			
			// draw the image preview
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, imageInfo.getImageID());
			drawTexturedModalRectSized(xPos + 8, yPos + 18, 0, 0, 160, 101, 256, 256);

            if (inBounds(mouseX, mouseY)) {
                int hover = (int) Math.floor((mouseY - ((height / 2) - (CONTAINER_HEIGHT / 2) + 140)) / 12);

                if (imageInfo.getSources().get(hover).hasTooltip() && hover >= 0 && hover < imageInfo.getSources().size()) {
                    drawHoveringText(Arrays.asList(imageInfo.getSources().get(hover).getTooltip()), mouseX, mouseY, mc.fontRenderer);
                }
            }
		} else {
			drawCenteredString(minecraft.fontRenderer, TranslationManager.getTranslation("image.history.empty"), (width / 2), yPos + 132, 0xFFFFFFFF);
		}
	}

    /**
	 * Called when a button is pressed by a user
	 */
	@Override
	public void actionPerformed(GuiButton button) {
		switch (button.id) {
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
				displayGuiScreen(new SettingsGui(this.screen));
				break;
			}
			
			case EXIT: {
				displayGuiScreen(this.screen);
				break;
			}
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);

        if (!this.inBounds(x, y)) {
            return;
        }
		
		int i = (y - ((height / 2) - (CONTAINER_HEIGHT / 2) + 140)) / 10;
		
		UploadedImage imageInfo = HistoryHandler.getUploadedImage(m_currentImage);

		if (imageInfo != null) {
			List<ImageSource> sources = imageInfo.getSources();
			
			if (i >= 0 && i < sources.size()) {
				ImageSource source = imageInfo.getSources().get(i);
				source.onClick();
			}
		}
	}

    private boolean inBounds(int x, int y) {
        if (x < (width / 2) - (CONTAINER_WIDTH / 2) + 12) {
            return false;
        }

        if (x > (width / 2) - (CONTAINER_WIDTH / 2) + CONTAINER_WIDTH - 12) {
            return false;
        }

        if (y < ((height / 2) - (CONTAINER_HEIGHT / 2)) + 148) {
            return false;
        }

        if (y > ((height / 2) - (CONTAINER_HEIGHT / 2)) + 182) {
            return false;
        }

        return true;
    }

}
