/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.commands;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.StringHelper;
import net.maniFoldclient.altmanager.AltManager;
import net.maniFoldclient.altmanager.CrackedAlt;
import net.maniFoldclient.command.CmdException;
import net.maniFoldclient.command.CmdSyntaxError;
import net.maniFoldclient.command.Command;
import net.maniFoldclient.util.ChatUtils;

public final class AddAltCmd extends Command
{
	public AddAltCmd()
	{
		super("addalt", "Adds a player to your alt list.", ".addalt <player>",
			"Add all players on the server: .addalt all");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 1)
			throw new CmdSyntaxError();
		
		String name = args[0];
		
		switch(name)
		{
			case "all":
			addAll();
			break;
			
			default:
			add(name);
			break;
		}
	}
	
	private void add(String name)
	{
		if(name.equalsIgnoreCase("Alexander01998"))
			return;
		
		MANIFOLD.getAltManager().add(new CrackedAlt(name));
		ChatUtils.message("Added 1 alt.");
	}
	
	private void addAll()
	{
		int alts = 0;
		AltManager altManager = MANIFOLD.getAltManager();
		String playerName = MC.getSession().getUsername();
		
		for(PlayerListEntry entry : MC.player.networkHandler.getPlayerList())
		{
			String name = entry.getProfile().getName();
			name = StringHelper.stripTextFormat(name);
			
			if(altManager.contains(name))
				continue;
			
			if(name.equalsIgnoreCase(playerName)
				|| name.equalsIgnoreCase("Alexander01998"))
				continue;
			
			altManager.add(new CrackedAlt(name));
			alts++;
		}
		
		ChatUtils.message("Added " + alts + (alts == 1 ? " alt." : " alts."));
	}
}
