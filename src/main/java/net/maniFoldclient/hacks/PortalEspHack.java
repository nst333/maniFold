/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.maniFoldclient.Category;
import net.maniFoldclient.events.CameraTransformViewBobbingListener;
import net.maniFoldclient.events.PacketInputListener;
import net.maniFoldclient.events.RenderListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.hacks.portalesp.PortalEspBlockGroup;
import net.maniFoldclient.settings.CheckboxSetting;
import net.maniFoldclient.settings.ChunkAreaSetting;
import net.maniFoldclient.settings.ColorSetting;
import net.maniFoldclient.settings.EspStyleSetting;
import net.maniFoldclient.util.RenderUtils;
import net.maniFoldclient.util.chunk.ChunkSearcher.Result;
import net.maniFoldclient.util.chunk.ChunkSearcherCoordinator;

public final class PortalEspHack extends Hack implements UpdateListener,
	CameraTransformViewBobbingListener, RenderListener
{
	private final EspStyleSetting style = new EspStyleSetting();
	
	private final PortalEspBlockGroup netherPortal =
		new PortalEspBlockGroup(Blocks.NETHER_PORTAL,
			new ColorSetting("Nether portal color",
				"Nether portals will be highlighted in this color.", Color.RED),
			new CheckboxSetting("Include nether portals", true));
	
	private final PortalEspBlockGroup endPortal =
		new PortalEspBlockGroup(Blocks.END_PORTAL,
			new ColorSetting("End portal color",
				"End portals will be highlighted in this color.", Color.GREEN),
			new CheckboxSetting("Include end portals", true));
	
	private final PortalEspBlockGroup endPortalFrame = new PortalEspBlockGroup(
		Blocks.END_PORTAL_FRAME,
		new ColorSetting("End portal frame color",
			"End portal frames will be highlighted in this color.", Color.BLUE),
		new CheckboxSetting("Include end portal frames", true));
	
	private final PortalEspBlockGroup endGateway = new PortalEspBlockGroup(
		Blocks.END_GATEWAY,
		new ColorSetting("End gateway color",
			"End gateways will be highlighted in this color.", Color.YELLOW),
		new CheckboxSetting("Include end gateways", true));
	
	private final List<PortalEspBlockGroup> groups =
		Arrays.asList(netherPortal, endPortal, endPortalFrame, endGateway);
	
	private final ChunkAreaSetting area = new ChunkAreaSetting("Area",
		"The area around the player to search in.\n"
			+ "Higher values require a faster computer.");
	
	private final BiPredicate<BlockPos, BlockState> query =
		(pos, state) -> state.getBlock() == Blocks.NETHER_PORTAL
			|| state.getBlock() == Blocks.END_PORTAL
			|| state.getBlock() == Blocks.END_PORTAL_FRAME
			|| state.getBlock() == Blocks.END_GATEWAY;
	
	private final ChunkSearcherCoordinator coordinator =
		new ChunkSearcherCoordinator(query, area);
	
	private boolean groupsUpToDate;
	
	public PortalEspHack()
	{
		super("PortalESP");
		setCategory(Category.RENDER);
		
		addSetting(style);
		groups.stream().flatMap(PortalEspBlockGroup::getSettings)
			.forEach(this::addSetting);
		addSetting(area);
	}
	
	@Override
	protected void onEnable()
	{
		groupsUpToDate = false;
		
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(PacketInputListener.class, coordinator);
		EVENTS.add(CameraTransformViewBobbingListener.class, this);
		EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(PacketInputListener.class, coordinator);
		EVENTS.remove(CameraTransformViewBobbingListener.class, this);
		EVENTS.remove(RenderListener.class, this);
		
		coordinator.reset();
		groups.forEach(PortalEspBlockGroup::clear);
	}
	
	@Override
	public void onCameraTransformViewBobbing(
		CameraTransformViewBobbingEvent event)
	{
		if(style.getSelected().hasLines())
			event.cancel();
	}
	
	@Override
	public void onUpdate()
	{
		boolean searchersChanged = coordinator.update();
		if(searchersChanged)
			groupsUpToDate = false;
		
		if(!groupsUpToDate && coordinator.isDone())
			updateGroupBoxes();
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		if(style.getSelected().hasBoxes())
			renderBoxes(matrixStack);
		
		if(style.getSelected().hasLines())
			renderTracers(matrixStack, partialTicks);
	}
	
	private void renderBoxes(MatrixStack matrixStack)
	{
		for(PortalEspBlockGroup group : groups)
		{
			if(!group.isEnabled())
				return;
			
			List<Box> boxes = group.getBoxes();
			int quadsColor = group.getColorI(0x40);
			int linesColor = group.getColorI(0x80);
			
			RenderUtils.drawSolidBoxes(matrixStack, boxes, quadsColor, false);
			RenderUtils.drawOutlinedBoxes(matrixStack, boxes, linesColor,
				false);
		}
	}
	
	private void renderTracers(MatrixStack matrixStack, float partialTicks)
	{
		for(PortalEspBlockGroup group : groups)
		{
			if(!group.isEnabled())
				return;
			
			List<Box> boxes = group.getBoxes();
			List<Vec3d> ends = boxes.stream().map(Box::getCenter).toList();
			int color = group.getColorI(0x80);
			
			RenderUtils.drawTracers(matrixStack, partialTicks, ends, color,
				false);
		}
	}
	
	private void updateGroupBoxes()
	{
		groups.forEach(PortalEspBlockGroup::clear);
		coordinator.getMatches().forEach(this::addToGroupBoxes);
		groupsUpToDate = true;
	}
	
	private void addToGroupBoxes(Result result)
	{
		for(PortalEspBlockGroup group : groups)
			if(result.state().getBlock() == group.getBlock())
			{
				group.add(result.pos());
				break;
			}
	}
}
