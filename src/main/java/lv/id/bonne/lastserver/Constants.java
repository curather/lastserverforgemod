//
// Armands  21.06.2017
// ForgeLastServer
//

package lv.id.bonne.lastserver;

import java.lang.String;

/**
 * This class holds and manage all constants.
 */
public final class Constants
{

// ---------------------------------------------------------------------
// Section: Global constants
// ---------------------------------------------------------------------

	/**
	 * Main mod package name
	 */
	public static final String MODID = "lastserver";

	/**
	 * Mod name
	 */
	public static final String MODNAME = "${name}";

	/**
	 * Mod version
	 */
	public static final String VERSION = "${version}";

	/**
	 * GUI Factory class location that allows to change settings ingame.
	 */
	public static final String GUI_FACTORY = "lv.id.bonne." + Constants.MODID + ".config." + "GuiFactoryModification";

// ---------------------------------------------------------------------
// Section: Config constant names
// ---------------------------------------------------------------------

	public static final String CATEGORY_GENERAL = "general";

	public static final String CATEGORY_HIDDEN = "hidden";

	public static final String CONFIG_SECONDS = "seconds";

	public static final String CONFIG_AUTO_RECONNECT = "auto_reconnect";

	public static final String CONFIG_SERVER_NAME = "server_name";

	public static final String CONFIG_SERVER_IP = "server_ip";

	public static final String CONFIG_SERVER_LOCAL = "server_is_local";

	public static final String COMMENT_DO_NOT_CHANGE_IT = "Do not change this value.";
}
