/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks.autofish;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.settings.CheckboxSetting;
import net.maniFoldclient.util.ChatUtils;

public class ShallowWaterWarningCheckbox extends CheckboxSetting
{
	private boolean hasAlreadyWarned;
	
	public ShallowWaterWarningCheckbox()
	{
		super("Shallow water warning",
			"Displays a warning message in chat when you are fishing in shallow"
				+ " water.",
			true);
	}
	
	public void reset()
	{
		hasAlreadyWarned = false;
	}
	
	public void checkWaterType()
	{
		FishingBobberEntity bobber = maniFoldClient.MC.player.fishHook;
		if(bobber.isOpenOrWaterAround(bobber.getBlockPos()))
		{
			hasAlreadyWarned = false;
			return;
		}
		
		if(isChecked() && !hasAlreadyWarned)
		{
			ChatUtils.warning("You are currently fishing in shallow water.");
			ChatUtils.message(
				"You can't get any treasure items while fishing like this.");
			
			if(!maniFoldClient.INSTANCE.getHax().openWaterEspHack.isEnabled())
				ChatUtils.message("Use OpenWaterESP to find open water.");
			
			hasAlreadyWarned = true;
		}
	}
}
