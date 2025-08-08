/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.commands;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.command.CmdError;
import net.maniFoldclient.command.CmdException;
import net.maniFoldclient.command.CmdSyntaxError;
import net.maniFoldclient.command.Command;
import net.maniFoldclient.util.ChatUtils;

@SearchTags({"view nbt", "NBTViewer", "nbt viewer"})
public final class ViewNbtCmd extends Command
{
	public ViewNbtCmd()
	{
		super("viewnbt", "Shows you the NBT data of an item.", ".viewnbt",
			"Copy to clipboard: .viewnbt copy");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		ClientPlayerEntity player = MC.player;
		ItemStack stack = player.getInventory().getSelectedStack();
		if(stack.isEmpty())
			throw new CmdError("You must hold an item in your main hand.");
		
		NbtCompound tag = stack
			.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT)
			.copyNbt();
		String nbtString = tag.toString();
		
		switch(String.join(" ", args).toLowerCase())
		{
			case "":
			ChatUtils.message("NBT: " + nbtString);
			break;
			
			case "copy":
			MC.keyboard.setClipboard(nbtString);
			ChatUtils.message("NBT data copied to clipboard.");
			break;
			
			default:
			throw new CmdSyntaxError();
		}
	}
}
