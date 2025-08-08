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
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.maniFoldclient.maniFoldClient;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin extends Block
	implements FluidDrainable
{
	private PowderSnowBlockMixin(maniFoldClient maniFold, Settings settings)
	{
		super(settings);
	}
	
	@Inject(at = @At("HEAD"),
		method = "canWalkOnPowderSnow(Lnet/minecraft/entity/Entity;)Z",
		cancellable = true)
	private static void onCanWalkOnPowderSnow(Entity entity,
		CallbackInfoReturnable<Boolean> cir)
	{
		if(!maniFoldClient.INSTANCE.getHax().snowShoeHack.isEnabled())
			return;
		
		if(entity == maniFoldClient.MC.player)
			cir.setReturnValue(true);
	}
}
