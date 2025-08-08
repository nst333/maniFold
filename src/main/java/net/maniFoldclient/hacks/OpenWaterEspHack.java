/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.Box;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.events.RenderListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.util.RenderUtils;

@SearchTags({"open water esp", "AutoFishESP", "auto fish esp"})
public final class OpenWaterEspHack extends Hack implements RenderListener
{
	public OpenWaterEspHack()
	{
		super("OpenWaterESP");
		setCategory(Category.RENDER);
	}
	
	@Override
	public String getRenderName()
	{
		FishingBobberEntity bobber = MC.player.fishHook;
		if(bobber == null)
			return getName();
		
		return getName() + (isInOpenWater(bobber) ? " [open]" : " [shallow]");
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(RenderListener.class, this);
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		FishingBobberEntity bobber = MC.player.fishHook;
		if(bobber == null)
			return;
		
		Box box = new Box(-2, -1, -2, 3, 2, 3).offset(bobber.getBlockPos());
		boolean inOpenWater = isInOpenWater(bobber);
		int color = inOpenWater ? 0x8000FF00 : 0x80FF0000;
		
		if(!inOpenWater)
			RenderUtils.drawCrossBox(matrixStack, box, color, false);
		
		RenderUtils.drawOutlinedBox(matrixStack, box, color, false);
	}
	
	private boolean isInOpenWater(FishingBobberEntity bobber)
	{
		return bobber.isOpenOrWaterAround(bobber.getBlockPos());
	}
}
