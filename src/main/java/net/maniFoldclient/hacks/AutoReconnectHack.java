/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import net.maniFoldclient.Category;
import net.maniFoldclient.DontBlock;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;

@SearchTags({"auto reconnect", "AutoRejoin", "auto rejoin"})
@DontBlock
public final class AutoReconnectHack extends Hack
{
	private final SliderSetting waitTime =
		new SliderSetting("Wait time", "Time before reconnecting in seconds.",
			5, 0, 60, 0.5, ValueDisplay.DECIMAL.withSuffix("s"));
	
	public AutoReconnectHack()
	{
		super("AutoReconnect");
		setCategory(Category.OTHER);
		addSetting(waitTime);
	}
	
	public int getWaitTicks()
	{
		return (int)(waitTime.getValue() * 20);
	}
	
	// See DisconnectedScreenMixin
}
