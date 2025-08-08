/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;

@SearchTags({"range"})
public final class ReachHack extends Hack
{
	private final SliderSetting range =
		new SliderSetting("Range", 6, 1, 10, 0.05, ValueDisplay.DECIMAL);
	
	public ReachHack()
	{
		super("Reach");
		setCategory(Category.OTHER);
		addSetting(range);
	}
	
	public double getReachDistance()
	{
		return range.getValue();
	}
	
	// See ClientPlayerEntityMixin.getBlockInteractionRange() and
	// ClientPlayerEntityMixin.getEntityInteractionRange()
}
