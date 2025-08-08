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
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;

@SearchTags({"legit", "disable"})
@DontBlock
public final class PanicHack extends Hack implements UpdateListener
{
	public PanicHack()
	{
		super("Panic");
		setCategory(Category.OTHER);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		for(Hack hack : MANIFOLD.getHax().getAllHax())
			if(hack.isEnabled() && hack != this)
				hack.setEnabled(false);
			
		setEnabled(false);
	}
}
