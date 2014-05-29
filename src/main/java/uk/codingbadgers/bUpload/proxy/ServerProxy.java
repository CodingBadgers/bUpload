package uk.codingbadgers.bUpload.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy implements Proxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        throw new RuntimeException("bUpload is a client only mod");
    }

    @Override
    public void load(FMLInitializationEvent event) {
    }

}
