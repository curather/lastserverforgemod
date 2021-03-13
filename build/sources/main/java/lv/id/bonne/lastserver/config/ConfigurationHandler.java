/*******************************************************************************
 * Last Server Joiner 2017.
 * Minecraft Forge Client Modification.
 *
 * Made by BONNe1704
 * Under Common Development and Distribution License (CDDL)
 ******************************************************************************/

package lv.id.bonne.lastserver.config;


import java.io.File;

import lv.id.bonne.lastserver.Constants;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * This class contains, collects and store mod configuration.
 */
@SideOnly(Side.CLIENT)
public class ConfigurationHandler
{
	// ---------------------------------------------------------------------
	// Section: Init method
	// ---------------------------------------------------------------------


	/**
	 * This method inits configuration file and check if all necessary values is set. If not it sets to
	 * default values.
	 * @param configFile config file.
	 */
	public static void init(File configFile)
	{
		ConfigurationHandler.configFile = configFile;
		ConfigurationHandler.configuration = new Configuration(configFile);
		ConfigurationHandler.configuration.load();

		ConfigurationHandler.configuration.getInt(
			Constants.CONFIG_SECONDS,
			Constants.CATEGORY_GENERAL,
			5,
			0,
			Integer.MAX_VALUE,
			I18n.format("config.gui.comment.seconds"));
		ConfigurationHandler.configuration.getBoolean(
			Constants.CONFIG_AUTO_RECONNECT,
			Constants.CATEGORY_GENERAL,
			false,
			I18n.format("config.gui.comment.autoreconnect"));
		ConfigurationHandler.configuration.getString(
			Constants.CONFIG_SERVER_NAME,
			Constants.CATEGORY_HIDDEN,
			"",
			Constants.COMMENT_DO_NOT_CHANGE_IT);
		ConfigurationHandler.configuration.getString(
			Constants.CONFIG_SERVER_IP,
			Constants.CATEGORY_HIDDEN,
			"",
			Constants.COMMENT_DO_NOT_CHANGE_IT);
		ConfigurationHandler.configuration.getBoolean(
			Constants.CONFIG_SERVER_LOCAL,
			Constants.CATEGORY_HIDDEN,
			true,
			Constants.COMMENT_DO_NOT_CHANGE_IT);

		ConfigurationHandler.configuration.save();
	}


	// ---------------------------------------------------------------------
	// Section: Getters
	// ---------------------------------------------------------------------


	/**
	 * This method returns ServerData object that is created from stored data.
	 * @return ServerData object that is created from stored data.
	 */
	public static ServerData getServerData()
	{
		ConfigurationHandler.configuration.load();

		String serverName = ConfigurationHandler.configuration.getString(
			Constants.CONFIG_SERVER_NAME,
			Constants.CATEGORY_HIDDEN,
			"",
			Constants.COMMENT_DO_NOT_CHANGE_IT);
		String serverIP = ConfigurationHandler.configuration.getString(
			Constants.CONFIG_SERVER_IP,
			Constants.CATEGORY_HIDDEN,
			"",
			Constants.COMMENT_DO_NOT_CHANGE_IT);
		boolean isLocal = ConfigurationHandler.configuration.getBoolean(
			Constants.CONFIG_SERVER_LOCAL,
			Constants.CATEGORY_HIDDEN,
			false,
			Constants.COMMENT_DO_NOT_CHANGE_IT);

		return new ServerData(serverName, serverIP, isLocal);
	}


	/**
	 * This method returns integer that represents reconnecting interval in seconds.
	 * @return seconds between reconnecting.
	 */
	public static int getReconnectInterval()
	{
		ConfigurationHandler.configuration.load();

		return ConfigurationHandler.configuration.getInt(
			Constants.CONFIG_SECONDS,
			Constants.CATEGORY_GENERAL,
			5,
			0,
			Integer.MAX_VALUE,
			I18n.format("config.gui.comment.seconds"));
	}


	/**
	 * This method returns boolean that represents if auto reconnect checkbox must be enabled on startup.
	 * @return true if checkbox must be turned-on otherwise false.
	 */
	public static boolean getAutoReconnectStatus()
	{
		ConfigurationHandler.configuration.load();

		return ConfigurationHandler.configuration.getBoolean(
			Constants.CONFIG_AUTO_RECONNECT,
			Constants.CATEGORY_GENERAL,
			false,
			I18n.format("config.gui.comment.autoreconnect"));
	}


	// ---------------------------------------------------------------------
	// Section: Setters
	// ---------------------------------------------------------------------


	/**
	 * This method store given server data in hidden config category.
	 * @param newServer server that must be stored.
	 */
	public static void storeSaverData(ServerData newServer)
	{
		// SinglePlayer games ServerData object is null. Fix null pointer issue.

		if (newServer == null)
		{
			return;
		}

		ConfigurationHandler.configuration.load();

		ConfigCategory category;

		if (ConfigurationHandler.configuration.getCategoryNames().contains(Constants.CATEGORY_HIDDEN))
		{
			category = ConfigurationHandler.configuration.getCategory(Constants.CATEGORY_HIDDEN);
		}
		else
		{
			category = new ConfigCategory(Constants.CATEGORY_HIDDEN);
		}

		if (category.containsKey(Constants.CONFIG_SERVER_NAME))
		{
			category.remove(Constants.CONFIG_SERVER_NAME);
			category.remove(Constants.CONFIG_SERVER_IP);
			category.remove(Constants.CONFIG_SERVER_LOCAL);
		}

		category.put(Constants.CONFIG_SERVER_NAME,
			new Property(Constants.CONFIG_SERVER_NAME,
				newServer.serverName,
				Property.Type.STRING));
		category.put(Constants.CONFIG_SERVER_IP,
			new Property(Constants.CONFIG_SERVER_IP,
				newServer.serverIP,
				Property.Type.STRING));
		category.put(Constants.CONFIG_SERVER_LOCAL,
			new Property(Constants.CONFIG_SERVER_LOCAL,
				String.valueOf(newServer.isOnLAN()),
				Property.Type.BOOLEAN));

		ConfigurationHandler.configuration.save();
	}


	// ---------------------------------------------------------------------
	// Section: Variables
	// ---------------------------------------------------------------------

	/**
	 * Configuration object.
	 */
	public static Configuration configuration;

	/**
	 * Configuration file.
	 */
	public static File configFile;
}
