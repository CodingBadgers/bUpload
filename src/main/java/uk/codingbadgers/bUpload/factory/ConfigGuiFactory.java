package uk.codingbadgers.bUpload.factory;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import uk.codingbadgers.bUpload.gui.SettingsGui;

import java.util.Set;

public class ConfigGuiFactory implements IModGuiFactory {

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return SettingsGui.class;
    }

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

}
