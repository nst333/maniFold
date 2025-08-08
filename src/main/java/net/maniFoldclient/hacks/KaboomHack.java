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

import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.maniFoldclient.Category;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.CheckboxSetting;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;
import net.maniFoldclient.util.BlockBreaker;
import net.maniFoldclient.util.BlockUtils;
import net.maniFoldclient.util.RotationUtils;

public final class KaboomHack extends Hack implements UpdateListener
{
	private final SliderSetting power =
		new SliderSetting("Power", "description.maniFold.setting.kaboom.power",
			128, 32, 512, 32, ValueDisplay.INTEGER);
	
	private final CheckboxSetting sound = new CheckboxSetting("Sound",
		"description.maniFold.setting.kaboom.sound", true);
	
	private final CheckboxSetting particles = new CheckboxSetting("Particles",
		"description.maniFold.setting.kaboom.particles", true);
	
	private final Random random = Random.create();
	
	public KaboomHack()
	{
		super("Kaboom");
		setCategory(Category.BLOCKS);
		addSetting(power);
		addSetting(sound);
		addSetting(particles);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		// Abort if flying to prevent getting kicked
		if(!MC.player.getAbilities().creativeMode && !MC.player.isOnGround())
			return;
		
		double x = MC.player.getX();
		double y = MC.player.getY();
		double z = MC.player.getZ();
		
		// Do explosion effect
		if(sound.isChecked())
		{
			float soundPitch =
				(1F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F;
			MC.world.playSoundClient(x, y, z,
				SoundEvents.ENTITY_GENERIC_EXPLODE.value(),
				SoundCategory.BLOCKS, 4, soundPitch, false);
		}
		if(particles.isChecked())
			MC.world.addParticleClient(ParticleTypes.EXPLOSION_EMITTER, x, y, z,
				1, 0, 0);
		
		// Break all blocks
		ArrayList<BlockPos> blocks = getBlocksByDistanceReversed();
		for(int i = 0; i < power.getValueI(); i++)
			BlockBreaker.breakBlocksWithPacketSpam(blocks);
		
		setEnabled(false);
	}
	
	private ArrayList<BlockPos> getBlocksByDistanceReversed()
	{
		Vec3d eyesVec = RotationUtils.getEyesPos();
		BlockPos eyesBlock = BlockPos.ofFloored(eyesVec);
		double rangeSq = 36;
		int blockRange = 6;
		
		// farthest blocks first
		return BlockUtils.getAllInBoxStream(eyesBlock, blockRange)
			.filter(pos -> pos.getSquaredDistance(eyesVec) <= rangeSq)
			.sorted(Comparator
				.comparingDouble(pos -> -pos.getSquaredDistance(eyesVec)))
			.collect(Collectors.toCollection(ArrayList::new));
	}
}
