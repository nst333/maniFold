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
import net.maniFoldclient.events.VelocityFromEntityCollisionListener;
import net.maniFoldclient.hack.Hack;

@SearchTags({"anti entity push", "NoEntityPush", "no entity push"})
public final class AntiEntityPushHack extends Hack
	implements VelocityFromEntityCollisionListener
{
	public AntiEntityPushHack()
	{
		super("AntiEntityPush");
		setCategory(Category.MOVEMENT);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(VelocityFromEntityCollisionListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(VelocityFromEntityCollisionListener.class, this);
	}
	
	@Override
	public void onVelocityFromEntityCollision(
		VelocityFromEntityCollisionEvent event)
	{
		if(event.getEntity() == MC.player)
			event.cancel();
	}
}
