/*******************************************************************************
 * Last Server Joiner 2017.
 * Minecraft Forge Client Modification.
 *
 * Made by BONNe1704
 * Under Common Development and Distribution License (CDDL)
 ******************************************************************************/

package lv.id.bonne.lastserver;


import lv.id.bonne.lastserver.config.ConfigurationHandler;
import lv.id.bonne.lastserver.event.LastServerEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * This is just main run class. Only calls for config initialization and event handler registration.
 */
@SideOnly(Side.CLIENT)
@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, guiFactory = Constants.GUI_FACTORY)
public class Main
{
	@Instance
	public static Main instance = new Main();


	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		// Inits config handler that use config from file.

		ConfigurationHandler.init(e.getSuggestedConfigurationFile());
	}


	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		// Register new event handler that will be process all mod things.

		MinecraftForge.EVENT_BUS.register(new LastServerEventHandler());
	}


	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
	}
}
