/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.commands;

import java.util.Comparator;
import java.util.stream.StreamSupport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.maniFoldclient.command.CmdError;
import net.maniFoldclient.command.CmdException;
import net.maniFoldclient.command.CmdSyntaxError;
import net.maniFoldclient.command.Command;
import net.maniFoldclient.hacks.ProtectHack;
import net.maniFoldclient.util.FakePlayerEntity;

public final class ProtectCmd extends Command
{
	public ProtectCmd()
	{
		super("protect", "Protects the given entity from other entities.",
			".protect <entity>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 1)
			throw new CmdSyntaxError();
		
		ProtectHack protectHack = MANIFOLD.getHax().protectHack;
		
		if(protectHack.isEnabled())
			protectHack.setEnabled(false);
		
		Entity entity = StreamSupport
			.stream(MC.world.getEntities().spliterator(), true)
			.filter(LivingEntity.class::isInstance)
			.filter(e -> !e.isRemoved() && ((LivingEntity)e).getHealth() > 0)
			.filter(e -> e != MC.player)
			.filter(e -> !(e instanceof FakePlayerEntity))
			.filter(e -> args[0].equalsIgnoreCase(e.getName().getString()))
			.min(
				Comparator.comparingDouble(e -> MC.player.squaredDistanceTo(e)))
			.orElse(null);
		
		if(entity == null)
			throw new CmdError(
				"Entity \"" + args[0] + "\" could not be found.");
		
		protectHack.setFriend(entity);
		protectHack.setEnabled(true);
	}
}
