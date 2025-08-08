/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import java.awt.Color;

import net.maniFoldclient.DontBlock;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.clickgui.screens.ClickGuiScreen;
import net.maniFoldclient.hack.DontSaveState;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.ColorSetting;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;

@DontSaveState
@DontBlock
@SearchTags({"click gui", "WindowGUI", "window gui", "HackMenu", "hack menu"})
public final class ClickGuiHack extends Hack
{
	private final ColorSetting bgColor =
		new ColorSetting("Background", "Background color", new Color(0x404040));
	
    // Set a dreamy purple shade as the default accent color for maniFold.
    private final ColorSetting acColor =
        new ColorSetting("Accent", "Accent color", new Color(0x6C3FA7));
	
	private final ColorSetting txtColor =
		new ColorSetting("Text", "Text color", new Color(0xF0F0F0));
	
	private final SliderSetting opacity = new SliderSetting("Opacity", 0.5,
		0.15, 0.85, 0.01, ValueDisplay.PERCENTAGE);
	
	private final SliderSetting ttOpacity = new SliderSetting("Tooltip opacity",
		0.75, 0.15, 1, 0.01, ValueDisplay.PERCENTAGE);
	
	private final SliderSetting maxHeight = new SliderSetting("Max height",
		"Maximum window height\n" + "0 = no limit", 200, 0, 1000, 50,
		ValueDisplay.INTEGER);
	
	private final SliderSetting maxSettingsHeight =
		new SliderSetting("Max settings height",
			"Maximum height for settings windows\n" + "0 = no limit", 200, 0,
			1000, 50, ValueDisplay.INTEGER);
	
	public ClickGuiHack()
	{
		super("ClickGUI");
		addSetting(bgColor);
		addSetting(acColor);
		addSetting(txtColor);
		addSetting(opacity);
		addSetting(ttOpacity);
		addSetting(maxHeight);
		addSetting(maxSettingsHeight);
	}
	
	@Override
	protected void onEnable()
	{
		MC.setScreen(new ClickGuiScreen(MANIFOLD.getGui()));
		setEnabled(false);
	}
	
	public float[] getBackgroundColor()
	{
		return bgColor.getColorF();
	}
	
	public float[] getAccentColor()
	{
		return acColor.getColorF();
	}
	
	public int getTextColor()
	{
		return txtColor.getColorI();
	}
	
	public float getOpacity()
	{
		return opacity.getValueF();
	}
	
	public float getTooltipOpacity()
	{
		return ttOpacity.getValueF();
	}
	
	public int getMaxHeight()
	{
		return maxHeight.getValueI();
	}
	
	public int getMaxSettingsHeight()
	{
		return maxSettingsHeight.getValueI();
	}
}
