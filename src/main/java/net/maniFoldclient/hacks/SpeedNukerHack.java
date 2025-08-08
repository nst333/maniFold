/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.events.LeftClickListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.DontSaveState;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.hacks.nukers.CommonNukerSettings;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;
import net.maniFoldclient.settings.SwingHandSetting;
import net.maniFoldclient.settings.SwingHandSetting.SwingHand;
import net.maniFoldclient.util.BlockBreaker;
import net.maniFoldclient.util.BlockUtils;
import net.maniFoldclient.util.RotationUtils;

@SearchTags({"speed nuker", "FastNuker", "fast nuker"})
@DontSaveState
public final class SpeedNukerHack extends Hack implements UpdateListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	private final CommonNukerSettings commonSettings =
		new CommonNukerSettings();
	
	private final SwingHandSetting swingHand = new SwingHandSetting(
		SwingHandSetting.genericMiningDescription(this), SwingHand.OFF);
	
	public SpeedNukerHack()
	{
		super("SpeedNuker");
		setCategory(Category.BLOCKS);
		addSetting(range);
		commonSettings.getSettings().forEach(this::addSetting);
		addSetting(swingHand);
	}
	
	@Override
	public String getRenderName()
	{
		return getName() + commonSettings.getRenderNameSuffix();
	}
	
	@Override
	protected void onEnable()
	{
		MANIFOLD.getHax().autoMineHack.setEnabled(false);
		MANIFOLD.getHax().excavatorHack.setEnabled(false);
		MANIFOLD.getHax().nukerHack.setEnabled(false);
		MANIFOLD.getHax().nukerLegitHack.setEnabled(false);
		MANIFOLD.getHax().tunnellerHack.setEnabled(false);
		MANIFOLD.getHax().veinMinerHack.setEnabled(false);
		
		EVENTS.add(LeftClickListener.class, commonSettings);
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(LeftClickListener.class, commonSettings);
		EVENTS.remove(UpdateListener.class, this);
		
		commonSettings.reset();
	}
	
	@Override
	public void onUpdate()
	{
		if(commonSettings.isIdModeWithAir())
			return;
		
		Vec3d eyesVec = RotationUtils.getEyesPos();
		BlockPos eyesBlock = BlockPos.ofFloored(eyesVec);
		double rangeSq = range.getValueSq();
		int blockRange = range.getValueCeil();
		
		Stream<BlockPos> stream =
			BlockUtils.getAllInBoxStream(eyesBlock, blockRange)
				.filter(BlockUtils::canBeClicked)
				.filter(commonSettings::shouldBreakBlock);
		
		if(commonSettings.isSphereShape())
			stream = stream
				.filter(pos -> pos.getSquaredDistance(eyesVec) <= rangeSq);
		
		ArrayList<BlockPos> blocks = stream
			.sorted(Comparator
				.comparingDouble(pos -> pos.getSquaredDistance(eyesVec)))
			.collect(Collectors.toCollection(ArrayList::new));
		
		if(blocks.isEmpty())
			return;
		
		MANIFOLD.getHax().autoToolHack.equipIfEnabled(blocks.get(0));
		BlockBreaker.breakBlocksWithPacketSpam(blocks);
		swingHand.swing(Hand.MAIN_HAND);
	}
}
