package uk.codingbadgers.bUpload.factory;

import java.util.Set;

import uk.codingbadgers.bUpload.gui.SettingsGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;

public class GuiFactory implements IModGuiFactory {

	private Minecraft minecraft;
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

	@Override
	public void initialize(Minecraft minecraftInstance) {
		minecraft = minecraftInstance;
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return SettingsGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

}
