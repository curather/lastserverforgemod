/*******************************************************************************
 * Last Server Joiner 2017.
 * Minecraft Forge Client Modification.
 *
 * Made by BONNe1704
 * Under Common Development and Distribution License (CDDL)
 ******************************************************************************/

package lv.id.bonne.lastserver.config;


import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * This class allows to change configuration ingame.
 */
@SideOnly(Side.CLIENT)
public class GuiFactoryModification implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
	}


	@Override
	public boolean hasConfigGui()
	{
		return true;
	}


	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new GuiConfigModification(parentScreen);
	}


	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return GuiConfigModification.class;
	}


	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}


	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}
}