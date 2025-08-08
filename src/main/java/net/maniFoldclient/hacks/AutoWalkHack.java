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
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.mixinterface.IKeyBinding;

@SearchTags({"auto walk"})
public final class AutoWalkHack extends Hack implements UpdateListener
{
	public AutoWalkHack()
	{
		super("AutoWalk");
		setCategory(Category.MOVEMENT);
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
		IKeyBinding.get(MC.options.forwardKey).resetPressedState();
	}
	
	@Override
	public void onUpdate()
	{
		MC.options.forwardKey.setPressed(true);
	}
}
