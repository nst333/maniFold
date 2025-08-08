/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.hack.HackList;

@Mixin(Block.class)
public abstract class BlockMixin implements ItemConvertible
{
	@Inject(at = @At("HEAD"),
		method = "getVelocityMultiplier()F",
		cancellable = true)
	private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir)
	{
		HackList hax = maniFoldClient.INSTANCE.getHax();
		if(hax == null || !hax.noSlowdownHack.isEnabled())
			return;
		
		if(cir.getReturnValueF() < 1)
			cir.setReturnValue(1F);
	}
}
