/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.events.HandleBlockBreakingListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.mixinterface.IKeyBinding;
import net.maniFoldclient.settings.CheckboxSetting;

@SearchTags({"auto mine", "AutoBreak", "auto break"})
public final class AutoMineHack extends Hack
	implements UpdateListener, HandleBlockBreakingListener
{
	private final CheckboxSetting superFastMode =
		new CheckboxSetting("Super fast mode",
			"Breaks blocks faster than you normally could. May get detected by"
				+ " anti-cheat plugins.",
			false);
	
	public AutoMineHack()
	{
		super("AutoMine");
		setCategory(Category.BLOCKS);
		addSetting(superFastMode);
	}
	
	@Override
	protected void onEnable()
	{
		MANIFOLD.getHax().excavatorHack.setEnabled(false);
		MANIFOLD.getHax().nukerHack.setEnabled(false);
		MANIFOLD.getHax().nukerLegitHack.setEnabled(false);
		MANIFOLD.getHax().speedNukerHack.setEnabled(false);
		MANIFOLD.getHax().tunnellerHack.setEnabled(false);
		MANIFOLD.getHax().veinMinerHack.setEnabled(false);
		
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(HandleBlockBreakingListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(HandleBlockBreakingListener.class, this);
		IKeyBinding.get(MC.options.attackKey).resetPressedState();
		MC.interactionManager.cancelBlockBreaking();
	}
	
	@Override
	public void onUpdate()
	{
		ClientPlayerInteractionManager im = MC.interactionManager;
		
		// Ignore the attack cooldown because opening any screen
		// will set it to 10k ticks.
		
		if(MC.player.isRiding())
		{
			im.cancelBlockBreaking();
			return;
		}
		
		HitResult hitResult = MC.crosshairTarget;
		if(hitResult == null || hitResult.getType() != HitResult.Type.BLOCK
			|| !(hitResult instanceof BlockHitResult bHitResult))
		{
			im.cancelBlockBreaking();
			return;
		}
		
		BlockPos pos = bHitResult.getBlockPos();
		BlockState state = MC.world.getBlockState(pos);
		Direction side = bHitResult.getSide();
		if(state.isAir())
		{
			im.cancelBlockBreaking();
			return;
		}
		
		MANIFOLD.getHax().autoToolHack.equipIfEnabled(pos);
		
		if(MC.player.isUsingItem())
			// This case doesn't cancel block breaking in vanilla Minecraft.
			return;
		
		if(!im.isBreakingBlock())
			im.attackBlock(pos, side);
		
		if(im.updateBlockBreakingProgress(pos, side))
		{
			MC.particleManager.addBlockBreakingParticles(pos, side);
			MC.player.swingHand(Hand.MAIN_HAND);
			MC.options.attackKey.setPressed(true);
		}
	}
	
	@Override
	public void onHandleBlockBreaking(HandleBlockBreakingEvent event)
	{
		// Cancel vanilla block breaking so we don't send the packets twice.
		if(!superFastMode.isChecked())
			event.cancel();
	}
}
