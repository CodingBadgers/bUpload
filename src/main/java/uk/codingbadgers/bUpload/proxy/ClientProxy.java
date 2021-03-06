package uk.codingbadgers.bUpload.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import uk.codingbadgers.bUpload.bUpload;
import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.handlers.KeyBindingHandler;
import uk.codingbadgers.bUpload.handlers.auth.AuthTypes;

import java.io.File;
import java.io.IOException;

public class ClientProxy implements Proxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        try {
            ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());
            File configFolder = event.getSuggestedConfigurationFile().getParentFile();
            bUpload.AUTH_DATABASE = new File(configFolder, "bUpload-auth.dtb");
            bUpload.HISTORY_DATABASE = new File(configFolder, "bUpload-history.dtb");

            if (!bUpload.AUTH_DATABASE.exists()) {
                bUpload.AUTH_DATABASE.createNewFile();
            }

            AuthTypes.loadData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
    }

}
