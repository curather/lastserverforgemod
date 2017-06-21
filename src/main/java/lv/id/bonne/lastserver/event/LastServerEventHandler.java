//
// Armands  19.06.2017
// ForgeLastServer
//


package lv.id.bonne.lastserver.event;


import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import lv.id.bonne.lastserver.config.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * This is main event handler class. It contains events that will allow to function this mods.
 */
@SideOnly(Side.CLIENT)
public class LastServerEventHandler
{

	// ---------------------------------------------------------------------
	// Section: Events
	// ---------------------------------------------------------------------


	/**
	 * This method process GuiScreenEvent. If new GuiScreen is created, it checks if screen is GuiMainMenu or
	 * GuiDisconnected and add new buttons to them.
	 * In GuiMainMenu it adds Join: [SERVER NAME] button.
	 * In GuiDisconnected it adds Reconnect button and AutoConnect checkbox.
	 * @param event GuiScreenEvent.InitGuiEvent.Post event that informs that new gui is created.
	 */
	@SubscribeEvent
	public void onGuiScreenOpen(GuiScreenEvent.InitGuiEvent.Post event)
	{
		this.isInsideDisconnectedGui = false;

		if (event.getGui() instanceof GuiMainMenu)
		{
			List<GuiButton> buttonList = event.getButtonList();

			GuiButton multilayerButton = null;
			int index = 0;

			Set<Integer> usedButtonIDSet = new HashSet<>();

			for (int i = 0; i < buttonList.size(); i++)
			{
				GuiButton button = buttonList.get(i);

				if (button.displayString.equals(I18n.format("menu.multiplayer")))
				{
					multilayerButton = button;
					index = i;
				}

				usedButtonIDSet.add(button.id);
			}

			int buttonID = this.generateUniqueID(usedButtonIDSet);
			usedButtonIDSet.add(buttonID);

			if (multilayerButton != null)
			{
				this.joinLastServerButton =
					initNewButton(multilayerButton,
						I18n.format("menu.joinButton",
							this.getLastServerData().serverName,
							this.getLastServerData().serverIP,
							this.getLastServerData().gameVersion),
						buttonID);

				if (ConfigurationHandler.getServerData().serverName.isEmpty())
				{
					this.joinLastServerButton.enabled = false;
				}

				buttonList.add(++index, this.joinLastServerButton);
			}
		}
		else if (event.getGui() instanceof GuiDisconnected)
		{
			this.isInsideDisconnectedGui = true;

			List<GuiButton> buttonList = event.getButtonList();

			GuiButton backToServerList = null;
			int index = 0;

			Set<Integer> usedButtonIDSet = new HashSet<>();

			for (int i = 0; i < buttonList.size(); i++)
			{
				GuiButton button = buttonList.get(i);

				if (button.displayString.equals(I18n.format("gui.toMenu")))
				{
					backToServerList = button;
					index = i;
				}

				usedButtonIDSet.add(button.id);
			}

			int buttonID = this.generateUniqueID(usedButtonIDSet);
			usedButtonIDSet.add(buttonID);

			if (backToServerList != null)
			{
				this.reconnectServerButton = this.initNewButton(
					backToServerList,
					I18n.format("gui.reconnect"),
					buttonID);
				buttonList.add(++index, this.reconnectServerButton);
			}

			buttonID = this.generateUniqueID(usedButtonIDSet);

			if (this.reconnectServerButton != null)
			{
				this.autoReconnectButton = this.initNewCheckBox(
					buttonList,
					I18n.format("gui.autoReconnect"),
					buttonID);
				buttonList.add(this.autoReconnectButton);
			}

			this.autoReconnectButton.setIsChecked(this.isCheckBoxEnabled);
		}
	}


	/**
	 * This method adds functionality for new buttons, if button is pressed.
	 * If button is joinLastServer or button is reconnectToServer, then it calls for method that will join
	 * client to the server.
	 * If button is autoReconnect check box, then it turns on / off counter.
	 * @param event GuiScreenEvent.ActionPerformedEvent.Post event that informs about pressing on button.
	 */
	@SubscribeEvent
	public void onButtonPress(GuiScreenEvent.ActionPerformedEvent.Post event)
	{
		GuiButton button = event.getButton();

		if (button == this.joinLastServerButton || button == this.reconnectServerButton)
		{
			this.connectToServer();
		}
		else if (button == this.autoReconnectButton)
		{
			button.displayString = I18n.format("gui.autoReconnect");

			this.isCheckBoxEnabled = ((GuiCheckBox) button).isChecked();
		}
	}


	/**
	 * This method adds new tick event that checks if counter is done and client must reconnect to server.
	 * @param event TickEvent.ClientTickEvent event that informs about each tick in client.
	 */
	@SubscribeEvent
	public void reconnectTicking(TickEvent.ClientTickEvent event)
	{
		if (this.isCheckBoxEnabled && this.isInsideDisconnectedGui)
		{
			this.tickCounter++;

			long interval = ConfigurationHandler.getReconnectInterval() * 40;

			this.autoReconnectButton.displayString =
				I18n.format("gui.connecting", (interval - this.tickCounter) / 40);

			if (this.tickCounter > interval)
			{
				this.tickCounter = 0;
				this.connectToServer();
			}
		}
		else
		{
			this.tickCounter = 0;
		}
	}


	/**
	 * This method gets and stores last server on which client is successfully connected.
	 * @param event FMLNetworkEvent.ClientConnectedToServerEvent event that informs about successful
	 * connection.
	 */
	@SubscribeEvent
	public void customConnectingEvent(FMLNetworkEvent.ClientConnectedToServerEvent event)
	{
		// Resets last server data from current server.

		this.lastServerData = Minecraft.getMinecraft().getCurrentServerData();

		// stores last server data object.

		ConfigurationHandler.storeSaverData(this.lastServerData);
	}


	// ---------------------------------------------------------------------
	// Section: Private methods
	// ---------------------------------------------------------------------


	/**
	 * This method returns last server data. If last server is not set, it reads and sets it from config.
	 * @return last server data.
	 */
	private ServerData getLastServerData()
	{
		if (this.lastServerData == null)
		{
			this.lastServerData = ConfigurationHandler.getServerData();
		}

		return this.lastServerData;
	}


	/**
	 * This method gets lastConnectedServer and create new GuiConnecting that will connects to that server.
	 */
	private void connectToServer()
	{
		FMLClientHandler.instance().showGuiScreen(
			new GuiConnecting(new GuiMainMenu(),
				Minecraft.getMinecraft(),
				this.getLastServerData()));
	}


	/**
	 * This method generate new button id and check if it is not used already.
	 * @param usedButtonIDSet Set that contains all previous used ids.
	 * @return new integer that represents button id.
	 */
	private int generateUniqueID(Set<Integer> usedButtonIDSet)
	{
		Random random = new Random(System.currentTimeMillis());

		int num = Math.abs(random.nextInt());

		while (usedButtonIDSet.contains(num))
		{
			num = Math.abs(random.nextInt());
		}

		return num;
	}


	/**
	 * This method create new button with given name in line with previous button. It shrinks previous button
	 * on half.
	 * @param previousButton Previous button.
	 * @param buttonName New button name.
	 * @param uniqueButtonID Unique button id.
	 * @return new GuiButton that is inline with previous button.
	 */
	private GuiButton initNewButton(GuiButton previousButton, String buttonName, int uniqueButtonID)
	{
		int width = previousButton.getButtonWidth() / 2 - LastServerEventHandler.WHITE_SPACE;
		int x = previousButton.x + width + 2 * LastServerEventHandler.WHITE_SPACE;

		previousButton.setWidth(width);

		return new GuiButton(
			uniqueButtonID,
			x,
			previousButton.y,
			width,
			previousButton.height,
			buttonName);
	}


	/**
	 * This method create new checkbox button with given name under all buttons from button list.
	 * @param buttonList List that contains all buttons in current screen.
	 * @param buttonName Current button name.
	 * @param uniqueID Current button unique id.
	 * @return new GuiCheckBox with given name under all buttons.
	 */
	private GuiCheckBox initNewCheckBox(List<GuiButton> buttonList, String buttonName, int uniqueID)
	{
		// find best checkbox place.

		int minX = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;

		for (GuiButton button : buttonList)
		{
			minX = Math.min(button.x, minX);
			maxY = Math.max(button.y, maxY);
		}

		int x = minX;
		int y = maxY + this.reconnectServerButton.height + LastServerEventHandler.WHITE_SPACE;

		return new GuiCheckBox(uniqueID,
			x + LastServerEventHandler.WHITE_SPACE,
			y,
			buttonName,
			false);
	}


	// ---------------------------------------------------------------------
	// Section: Variables
	// ---------------------------------------------------------------------

	/**
	 * This boolean represents if AutoReconnect checkbox is enabled or not, as GuiDisconnected screen is
	 * destroyed after changing to GuiConnecting screen.
	 * Default value it gets from user configuration.
	 */
	private boolean isCheckBoxEnabled = ConfigurationHandler.getAutoReconnectStatus();

	/**
	 * This boolean is true only if client is in GuiDisconnected screen. Otherwise it must be false.
	 */
	private boolean isInsideDisconnectedGui = false;

	/**
	 * This is tick counter. It stores how mach tick already is passed.
	 */
	private int tickCounter = 0;

	/**
	 * This variable stores information about last server.
	 */
	private ServerData lastServerData;

// ---------------------------------------------------------------------
// Section: Buttons
// ---------------------------------------------------------------------

	/**
	 * This is auto reconnect checkbox. Appears in GuiDisconnected screen.
	 */
	private GuiCheckBox autoReconnectButton;

	/**
	 * This is reconnect button. Appears in GuiDisconnected screen.
	 */
	private GuiButton reconnectServerButton;

	/**
	 * This is join last server button. Appears in GuiMainMenu screen.
	 */
	private GuiButton joinLastServerButton;

// ---------------------------------------------------------------------
// Section: Constants
// ---------------------------------------------------------------------

	/**
	 * This constant represents whitespace size between buttons.
	 */
	private static final int WHITE_SPACE = 2;
}

