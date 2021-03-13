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
	public void initialize(Minecraft mc) {
		// Doesn't really have a use right now
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		// Doesn't have a use right now
		return null;
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return GuiConfigModification.class;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		// Doesn't have a use right now
		return null;
	}
}