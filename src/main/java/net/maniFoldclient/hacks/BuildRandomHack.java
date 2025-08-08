/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import java.util.Random;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.events.RenderListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.CheckboxSetting;
import net.maniFoldclient.settings.FacingSetting;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;
import net.maniFoldclient.settings.SwingHandSetting;
import net.maniFoldclient.settings.SwingHandSetting.SwingHand;
import net.maniFoldclient.util.BlockPlacer;
import net.maniFoldclient.util.BlockPlacer.BlockPlacingParams;
import net.maniFoldclient.util.BlockUtils;
import net.maniFoldclient.util.InteractionSimulator;
import net.maniFoldclient.util.RenderUtils;
import net.maniFoldclient.util.RotationUtils;

@SearchTags({"build random", "RandomBuild", "random build", "PlaceRandom",
	"place random", "RandomPlace", "random place"})
public final class BuildRandomHack extends Hack
	implements UpdateListener, RenderListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	private SliderSetting maxAttempts = new SliderSetting("Max attempts",
		"Maximum number of random positions that BuildRandom will try to place"
			+ " a block at in one tick.\n\n"
			+ "Higher values speed up the building process at the cost of"
			+ " increased lag.",
		128, 1, 1024, 1, ValueDisplay.INTEGER);
	
	private final CheckboxSetting checkItem =
		new CheckboxSetting("Check held item",
			"Only builds when you are actually holding a block.\n"
				+ "Turn this off to build with fire, water, lava, spawn eggs,"
				+ " or if you just want to right click with an empty hand"
				+ " in random places.",
			true);
	
	private final CheckboxSetting checkLOS =
		new CheckboxSetting("Check line of sight",
			"Ensure that BuildRandom won't try to place blocks behind walls.",
			false);
	
	private final FacingSetting facing = FacingSetting.withoutPacketSpam(
		"How BuildRandom should face the randomly placed blocks.\n\n"
			+ "\u00a7lOff\u00a7r - Don't face the blocks at all. Will be"
			+ " detected by anti-cheat plugins.\n\n"
			+ "\u00a7lServer-side\u00a7r - Face the blocks on the"
			+ " server-side, while still letting you move the camera freely on"
			+ " the client-side.\n\n"
			+ "\u00a7lClient-side\u00a7r - Face the blocks by moving your"
			+ " camera on the client-side. This is the most legit option, but"
			+ " can be VERY disorienting to look at.");
	
	private final SwingHandSetting swingHand =
		new SwingHandSetting(this, SwingHand.SERVER);
	
	private final CheckboxSetting fastPlace =
		new CheckboxSetting("Always FastPlace",
			"Builds as if FastPlace was enabled, even if it's not.", false);
	
	private final CheckboxSetting placeWhileBreaking = new CheckboxSetting(
		"Place while breaking",
		"Builds even while you are breaking a block.\n"
			+ "Possible with hacks, but wouldn't work in vanilla. May look suspicious.",
		false);
	
	private final CheckboxSetting placeWhileRiding = new CheckboxSetting(
		"Place while riding",
		"Builds even while you are riding a vehicle.\n"
			+ "Possible with hacks, but wouldn't work in vanilla. May look suspicious.",
		false);
	
	private final CheckboxSetting indicator = new CheckboxSetting("Indicator",
		"Shows where BuildRandom is placing blocks.", true);
	
	private final Random random = new Random();
	private BlockPos lastPos;
	
	public BuildRandomHack()
	{
		super("BuildRandom");
		setCategory(Category.BLOCKS);
		addSetting(range);
		addSetting(maxAttempts);
		addSetting(checkItem);
		addSetting(checkLOS);
		addSetting(facing);
		addSetting(swingHand);
		addSetting(fastPlace);
		addSetting(placeWhileBreaking);
		addSetting(placeWhileRiding);
		addSetting(indicator);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		lastPos = null;
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(RenderListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		lastPos = null;
		
		if(MANIFOLD.getHax().freecamHack.isEnabled())
			return;
		
		if(!fastPlace.isChecked() && MC.itemUseCooldown > 0)
			return;
		
		if(checkItem.isChecked() && !MC.player.isHolding(
			stack -> !stack.isEmpty() && stack.getItem() instanceof BlockItem))
			return;
		
		if(!placeWhileBreaking.isChecked()
			&& MC.interactionManager.isBreakingBlock())
			return;
		
		if(!placeWhileRiding.isChecked() && MC.player.isRiding())
			return;
		
		int maxAttempts = this.maxAttempts.getValueI();
		int blockRange = range.getValueCeil();
		int bound = blockRange * 2 + 1;
		BlockPos pos;
		int attempts = 0;
		
		do
		{
			// generate random position
			pos = BlockPos.ofFloored(RotationUtils.getEyesPos()).add(
				random.nextInt(bound) - blockRange,
				random.nextInt(bound) - blockRange,
				random.nextInt(bound) - blockRange);
			attempts++;
			
		}while(attempts < maxAttempts && !tryToPlaceBlock(pos));
	}
	
	private boolean tryToPlaceBlock(BlockPos pos)
	{
		if(!BlockUtils.getState(pos).isReplaceable())
			return false;
		
		BlockPlacingParams params = BlockPlacer.getBlockPlacingParams(pos);
		if(params == null || params.distanceSq() > range.getValueSq())
			return false;
		if(checkLOS.isChecked() && !params.lineOfSight())
			return false;
		
		MC.itemUseCooldown = 4;
		facing.getSelected().face(params.hitVec());
		lastPos = pos;
		
		InteractionSimulator.rightClickBlock(params.toHitResult(),
			swingHand.getSelected());
		return true;
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		if(lastPos == null || !indicator.isChecked())
			return;
		
		// Get colors
		float red = partialTicks * 2F;
		float green = 2 - red;
		float[] rgb = {red, green, 0};
		int quadColor = RenderUtils.toIntColor(rgb, 0.25F);
		int lineColor = RenderUtils.toIntColor(rgb, 0.5F);
		
		// Draw box
		Box box = new Box(lastPos);
		RenderUtils.drawSolidBox(matrixStack, box, quadColor, false);
		RenderUtils.drawOutlinedBox(matrixStack, box, lineColor, false);
	}
}
