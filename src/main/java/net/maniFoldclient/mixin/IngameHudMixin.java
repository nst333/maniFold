/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.event.EventManager;
import net.maniFoldclient.events.GUIRenderListener.GUIRenderEvent;
import net.maniFoldclient.hack.HackList;

@Mixin(InGameHud.class)
public class IngameHudMixin
{
	@Shadow
	@Final
	private DebugHud debugHud;
	
	// runs after renderScoreboardSidebar()
	// and before playerListHud.setVisible()
	@Inject(at = @At("HEAD"),
		method = "renderPlayerList(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
	private void onRenderPlayerList(DrawContext context,
		RenderTickCounter tickCounter, CallbackInfo ci)
	{
		if(debugHud.shouldShowDebugHud())
			return;
		
		float tickDelta = tickCounter.getTickProgress(true);
		EventManager.fire(new GUIRenderEvent(context, tickDelta));
	}
	
	@Inject(at = @At("HEAD"),
		method = "renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V",
		cancellable = true)
	private void onRenderOverlay(DrawContext context, Identifier texture,
		float opacity, CallbackInfo ci)
	{
		if(texture == null)
			return;
		
		String path = texture.getPath();
		HackList hax = maniFoldClient.INSTANCE.getHax();
		
		if("textures/misc/pumpkinblur.png".equals(path)
			&& hax.noPumpkinHack.isEnabled())
			ci.cancel();
		
		if("textures/misc/powder_snow_outline.png".equals(path)
			&& hax.noOverlayHack.isEnabled())
			ci.cancel();
	}
	
	@Inject(at = @At("HEAD"),
		method = "renderVignetteOverlay",
		cancellable = true)
	private void onRenderVignetteOverlay(DrawContext context, Entity entity,
		CallbackInfo ci)
	{
		HackList hax = maniFoldClient.INSTANCE.getHax();
		if(hax == null || !hax.noVignetteHack.isEnabled())
			return;
		
		ci.cancel();
	}
}
