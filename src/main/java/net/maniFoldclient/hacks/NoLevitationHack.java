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

@SearchTags({"no levitation", "levitation", "levitate"})
public final class NoLevitationHack extends Hack
{
	public NoLevitationHack()
	{
		super("NoLevitation");
		setCategory(Category.MOVEMENT);
	}
	
	// See ClientPlayerEntityMixin.hasStatusEffect()
}
