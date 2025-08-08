/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import java.awt.Color;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.events.RenderListener;
import net.maniFoldclient.events.RightClickListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.CheckboxSetting;
import net.maniFoldclient.settings.ColorSetting;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;
import net.maniFoldclient.util.InteractionSimulator;
import net.maniFoldclient.util.RenderUtils;

@SearchTags({"air place"})
public final class AirPlaceHack extends Hack
	implements RightClickListener, UpdateListener, RenderListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	private final CheckboxSetting guide = new CheckboxSetting("Guide",
		"description.maniFold.setting.airplace.guide", true);
	
	private final ColorSetting guideColor = new ColorSetting("Guide color",
		"description.maniFold.setting.airplace.guide_color", Color.RED);
	
	private BlockPos renderPos;
	
	public AirPlaceHack()
	{
		super("AirPlace");
		setCategory(Category.BLOCKS);
		addSetting(range);
		addSetting(guide);
		addSetting(guideColor);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(RenderListener.class, this);
		EVENTS.add(RightClickListener.class, this);
		renderPos = null;
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(RenderListener.class, this);
		EVENTS.remove(RightClickListener.class, this);
	}
	
	@Override
	public void onRightClick(RightClickEvent event)
	{
		BlockHitResult hitResult = getHitResultIfMissed();
		if(hitResult == null)
			return;
		
		MC.itemUseCooldown = 4;
		if(MC.player.isRiding())
			return;
		
		InteractionSimulator.rightClickBlock(hitResult);
		event.cancel();
	}
	
	@Override
	public void onUpdate()
	{
		renderPos = null;
		
		if(!guide.isChecked())
			return;
		
		if(MC.player.getMainHandStack().isEmpty()
			&& MC.player.getOffHandStack().isEmpty())
			return;
		
		if(MC.player.isRiding())
			return;
		
		BlockHitResult hitResult = getHitResultIfMissed();
		if(hitResult != null)
			renderPos = hitResult.getBlockPos();
	}
	
	private BlockHitResult getHitResultIfMissed()
	{
		HitResult hitResult = MC.player.raycast(range.getValue(), 0, false);
		if(hitResult.getType() != HitResult.Type.MISS)
			return null;
		
		if(!(hitResult instanceof BlockHitResult blockHitResult))
			return null;
		
		return blockHitResult;
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		if(renderPos == null)
			return;
		
		Box box = new Box(renderPos);
		
		int quadColor = guideColor.getColorI(0x1A);
		RenderUtils.drawSolidBox(matrixStack, box, quadColor, false);
		
		int lineColor = guideColor.getColorI(0xC0);
		RenderUtils.drawOutlinedBox(matrixStack, box, lineColor, false);
	}
}
