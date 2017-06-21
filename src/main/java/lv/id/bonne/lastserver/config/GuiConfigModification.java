//
// Armands  20.06.2017
// ForgeLastServer
//


package lv.id.bonne.lastserver.config;


import lv.id.bonne.lastserver.Constants;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Custom Gui Configuration that allows to change setting ingame.
 */
@SideOnly(Side.CLIENT)
public class GuiConfigModification extends GuiConfig
{
	public GuiConfigModification(GuiScreen parent)
	{
		super(parent,
			new ConfigElement(
				ConfigurationHandler.configuration.getCategory(Constants.CATEGORY_GENERAL)).
				getChildElements(),
			Constants.MODID,
			false,
			false,
			I18n.format("config.gui.title"));
		titleLine2 = ConfigurationHandler.configFile.getAbsolutePath();
	}


	@Override
	public void initGui()
	{
		// You can add buttons and initialize fields here

		super.initGui();
	}


	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		// You can do things like create animations, draw additional elements, etc. here

		super.drawScreen(mouseX, mouseY, partialTicks);
	}


	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		ConfigurationHandler.configuration.save();
	}
}