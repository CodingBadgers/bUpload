package uk.codingbadgers.bUpload.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * The logic proxy, changes internal mod logic based of environment
 * conditions.
 */
public interface Proxy {

    /**
     * Called pre initialisation of the mod.
     *
     * @param event the pre initialisation event
     */
    public void preInit(FMLPreInitializationEvent event);

    /**
     * Called on initialisation of the mod.
     *
     * @param event the initialisation event
     */
    public void load(FMLInitializationEvent event);
    
}
