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
package uk.codingbadgers.bUpload.handlers;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

import uk.codingbadgers.bUpload.gui.UploadHistoryGUI;
import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.manager.TranslationManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindingHandler {

	public static KeyBinding onScreenShot = new KeyBinding(TranslationManager.getTranslation("image.binding.screenshot"), Keyboard.getKeyIndex(ConfigHandler.KEYBIND_ADV_SS), TranslationManager.getTranslation("image.binding.group"));
	public static KeyBinding onUploadHistory = new KeyBinding(TranslationManager.getTranslation("image.binding.history"), Keyboard.getKeyIndex(ConfigHandler.KEYBIND_HISTORY), TranslationManager.getTranslation("image.binding.group"));

	public KeyBindingHandler() {
		ClientRegistry.registerKeyBinding(onScreenShot);
		ClientRegistry.registerKeyBinding(onUploadHistory);
	}

	@SubscribeEvent
	public void onKeyPress(KeyInputEvent event) {
		System.out.println("EVENT");

		Minecraft minecraft = Minecraft.getMinecraft();

		if (onScreenShot.getIsKeyPressed()) {
			ScreenshotHandler.handleScreenshot();
		} else if (onScreenShot.getIsKeyPressed() && minecraft.currentScreen == null) {
			minecraft.displayGuiScreen(new UploadHistoryGUI(minecraft.currentScreen instanceof bUploadGuiScreen ? (bUploadGuiScreen) minecraft.currentScreen : null));
		}
	}

}
