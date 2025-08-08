/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import net.maniFoldclient.DontBlock;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.hack.DontSaveState;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.navigator.NavigatorMainScreen;

@DontSaveState
@DontBlock
@SearchTags({"ClickGUI", "click gui", "SearchGUI", "search gui", "HackMenu",
	"hack menu"})
public final class NavigatorHack extends Hack
{
	public NavigatorHack()
	{
		super("Navigator");
	}
	
	@Override
	protected void onEnable()
	{
		if(!(MC.currentScreen instanceof NavigatorMainScreen))
			MC.setScreen(new NavigatorMainScreen());
		
		setEnabled(false);
	}
}
