/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hud;

import net.minecraft.client.gui.DrawContext;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.clickgui.ClickGui;
import net.maniFoldclient.clickgui.screens.ClickGuiScreen;
import net.maniFoldclient.events.GUIRenderListener;

public final class IngameHUD implements GUIRenderListener
{
	private final maniFoldLogo maniFoldLogo = new maniFoldLogo();
	private final HackListHUD hackList = new HackListHUD();
	private TabGui tabGui;
	
	@Override
	public void onRenderGUI(DrawContext context, float partialTicks)
	{
		if(!maniFoldClient.INSTANCE.isEnabled())
			return;
		
		if(tabGui == null)
			tabGui = new TabGui();
		
		ClickGui clickGui = maniFoldClient.INSTANCE.getGui();
		
		clickGui.updateColors();
		
		maniFoldLogo.render(context);
		hackList.render(context, partialTicks);
		tabGui.render(context, partialTicks);
		
		// pinned windows
		if(!(maniFoldClient.MC.currentScreen instanceof ClickGuiScreen))
			clickGui.renderPinnedWindows(context, partialTicks);
	}
	
	public HackListHUD getHackList()
	{
		return hackList;
	}
}
