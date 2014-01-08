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
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

import uk.codingbadgers.bUpload.gui.UploadHistoryGUI;
import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindingHandler {

	public static KeyBinding onScreenShot = new KeyBinding(I18n.getStringParams("image.binding.screenshot"), Keyboard.getKeyIndex(ConfigHandler.KEYBIND_ADV_SS), I18n.getStringParams("image.binding.screenshot"));
	public static KeyBinding onUploadHistory = new KeyBinding(I18n.getStringParams("image.binding.history"), Keyboard.getKeyIndex(ConfigHandler.KEYBIND_HISTORY), I18n.getStringParams("image.binding.history"));

	public KeyBindingHandler() {
		System.out.println(onScreenShot.func_151469_h());
		System.out.println(onUploadHistory.func_151469_h());

		ClientRegistry.registerKeyBinding(onScreenShot);
		ClientRegistry.registerKeyBinding(onUploadHistory);
	}

	@EventHandler
	public void onKeyPress(KeyInputEvent event) {
		System.out.println("EVENT");

		Minecraft minecraft = Minecraft.getMinecraft();

		if (Keyboard.isKeyDown(onScreenShot.func_151469_h())) {
			ScreenshotHandler.handleScreenshot();
		} else if (Keyboard.isKeyDown(onUploadHistory.func_151469_h()) && minecraft.currentScreen == null) {
			minecraft.func_147108_a(new UploadHistoryGUI(minecraft.currentScreen instanceof bUploadGuiScreen ? (bUploadGuiScreen) minecraft.currentScreen : null));
		}
	}

}
