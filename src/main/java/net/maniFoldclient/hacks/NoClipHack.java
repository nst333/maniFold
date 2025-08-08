/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import net.minecraft.client.network.ClientPlayerEntity;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.events.AirStrafingSpeedListener;
import net.maniFoldclient.events.IsNormalCubeListener;
import net.maniFoldclient.events.PlayerMoveListener;
import net.maniFoldclient.events.SetOpaqueCubeListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;

@SearchTags({"no clip"})
public final class NoClipHack extends Hack
	implements UpdateListener, PlayerMoveListener, IsNormalCubeListener,
	SetOpaqueCubeListener, AirStrafingSpeedListener
{
	public NoClipHack()
	{
		super("NoClip");
		setCategory(Category.MOVEMENT);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(PlayerMoveListener.class, this);
		EVENTS.add(IsNormalCubeListener.class, this);
		EVENTS.add(SetOpaqueCubeListener.class, this);
		EVENTS.add(AirStrafingSpeedListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(PlayerMoveListener.class, this);
		EVENTS.remove(IsNormalCubeListener.class, this);
		EVENTS.remove(SetOpaqueCubeListener.class, this);
		EVENTS.remove(AirStrafingSpeedListener.class, this);
		
		MC.player.noClip = false;
	}
	
	@Override
	public void onUpdate()
	{
		ClientPlayerEntity player = MC.player;
		
		player.noClip = true;
		player.fallDistance = 0;
		player.setOnGround(false);
		
		player.getAbilities().flying = false;
		player.setVelocity(0, 0, 0);
		
		float speed = 0.2F;
		if(MC.options.jumpKey.isPressed())
			player.addVelocity(0, speed, 0);
		if(MC.options.sneakKey.isPressed())
			player.addVelocity(0, -speed, 0);
	}
	
	@Override
	public void onGetAirStrafingSpeed(AirStrafingSpeedEvent event)
	{
		event.setSpeed(0.2F);
	}
	
	@Override
	public void onPlayerMove()
	{
		MC.player.noClip = true;
	}
	
	@Override
	public void onIsNormalCube(IsNormalCubeEvent event)
	{
		event.cancel();
	}
	
	@Override
	public void onSetOpaqueCube(SetOpaqueCubeEvent event)
	{
		event.cancel();
	}
}
