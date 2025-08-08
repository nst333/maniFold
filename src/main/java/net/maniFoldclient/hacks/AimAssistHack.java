/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import java.util.Comparator;
import java.util.stream.Stream;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.maniFoldclient.Category;
import net.maniFoldclient.events.MouseUpdateListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.AimAtSetting;
import net.maniFoldclient.settings.CheckboxSetting;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;
import net.maniFoldclient.settings.filterlists.EntityFilterList;
import net.maniFoldclient.settings.filters.*;
import net.maniFoldclient.util.BlockUtils;
import net.maniFoldclient.util.EntityUtils;
import net.maniFoldclient.util.Rotation;
import net.maniFoldclient.util.RotationUtils;

public final class AimAssistHack extends Hack
	implements UpdateListener, MouseUpdateListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 4.5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	private final SliderSetting rotationSpeed =
		new SliderSetting("Rotation Speed", 600, 10, 3600, 10,
			ValueDisplay.DEGREES.withSuffix("/s"));
	
	private final SliderSetting fov =
		new SliderSetting("FOV", "description.maniFold.setting.aimassist.fov", 120,
			30, 360, 10, ValueDisplay.DEGREES);
	
	private final AimAtSetting aimAt = new AimAtSetting(
		"What point in the target's hitbox AimAssist should aim at.");
	
	private final SliderSetting ignoreMouseInput =
		new SliderSetting("Ignore mouse input",
			"description.maniFold.setting.aimassist.ignore_mouse_input", 0, 0, 1,
			0.01, ValueDisplay.PERCENTAGE);
	
	private final CheckboxSetting checkLOS =
		new CheckboxSetting("Check line of sight",
			"description.maniFold.setting.aimassist.check_line_of_sight", true);
	
	private final CheckboxSetting aimWhileBlocking =
		new CheckboxSetting("Aim while blocking",
			"description.maniFold.setting.aimassist.aim_while_blocking", false);
	
	private final EntityFilterList entityFilters =
		new EntityFilterList(FilterPlayersSetting.genericCombat(false),
			FilterSleepingSetting.genericCombat(false),
			FilterFlyingSetting.genericCombat(0),
			FilterHostileSetting.genericCombat(false),
			FilterNeutralSetting
				.genericCombat(AttackDetectingEntityFilter.Mode.OFF),
			FilterPassiveSetting.genericCombat(true),
			FilterPassiveWaterSetting.genericCombat(true),
			FilterBabiesSetting.genericCombat(true),
			FilterBatsSetting.genericCombat(true),
			FilterSlimesSetting.genericCombat(true),
			FilterPetsSetting.genericCombat(true),
			FilterVillagersSetting.genericCombat(true),
			FilterZombieVillagersSetting.genericCombat(true),
			FilterGolemsSetting.genericCombat(false),
			FilterPiglinsSetting
				.genericCombat(AttackDetectingEntityFilter.Mode.OFF),
			FilterZombiePiglinsSetting
				.genericCombat(AttackDetectingEntityFilter.Mode.OFF),
			FilterEndermenSetting
				.genericCombat(AttackDetectingEntityFilter.Mode.OFF),
			FilterShulkersSetting.genericCombat(false),
			FilterInvisibleSetting.genericCombat(true),
			FilterNamedSetting.genericCombat(false),
			FilterShulkerBulletSetting.genericCombat(false),
			FilterArmorStandsSetting.genericCombat(true),
			FilterCrystalsSetting.genericCombat(true));
	
	private Entity target;
	private float nextYaw;
	private float nextPitch;
	
	public AimAssistHack()
	{
		super("AimAssist");
		setCategory(Category.COMBAT);
		
		addSetting(range);
		addSetting(rotationSpeed);
		addSetting(fov);
		addSetting(aimAt);
		addSetting(ignoreMouseInput);
		addSetting(checkLOS);
		addSetting(aimWhileBlocking);
		
		entityFilters.forEach(this::addSetting);
	}
	
	@Override
	protected void onEnable()
	{
		// disable incompatible hacks
		MANIFOLD.getHax().autoFishHack.setEnabled(false);
		MANIFOLD.getHax().clickAuraHack.setEnabled(false);
		MANIFOLD.getHax().crystalAuraHack.setEnabled(false);
		MANIFOLD.getHax().fightBotHack.setEnabled(false);
		MANIFOLD.getHax().killauraHack.setEnabled(false);
		MANIFOLD.getHax().killauraLegitHack.setEnabled(false);
		MANIFOLD.getHax().multiAuraHack.setEnabled(false);
		MANIFOLD.getHax().protectHack.setEnabled(false);
		MANIFOLD.getHax().tpAuraHack.setEnabled(false);
		
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(MouseUpdateListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(MouseUpdateListener.class, this);
		target = null;
	}
	
	@Override
	public void onUpdate()
	{
		target = null;
		
		// don't aim when a container/inventory screen is open
		if(MC.currentScreen instanceof HandledScreen)
			return;
		
		if(!aimWhileBlocking.isChecked() && MC.player.isUsingItem())
			return;
		
		chooseTarget();
		if(target == null)
			return;
		
		Vec3d hitVec = aimAt.getAimPoint(target);
		if(checkLOS.isChecked() && !BlockUtils.hasLineOfSight(hitVec))
		{
			target = null;
			return;
		}
		
		MANIFOLD.getHax().autoSwordHack.setSlot(target);
		
		// get needed rotation
		Rotation needed = RotationUtils.getNeededRotations(hitVec);
		
		// turn towards center of boundingBox
		Rotation next = RotationUtils.slowlyTurnTowards(needed,
			rotationSpeed.getValueI() / 20F);
		nextYaw = next.yaw();
		nextPitch = next.pitch();
	}
	
	private void chooseTarget()
	{
		Stream<Entity> stream = EntityUtils.getAttackableEntities();
		
		double rangeSq = range.getValueSq();
		stream = stream.filter(e -> MC.player.squaredDistanceTo(e) <= rangeSq);
		
		if(fov.getValue() < 360.0)
			stream = stream.filter(e -> RotationUtils.getAngleToLookVec(
				aimAt.getAimPoint(e)) <= fov.getValue() / 2.0);
		
		stream = entityFilters.applyTo(stream);
		
		target = stream
			.min(Comparator.comparingDouble(
				e -> RotationUtils.getAngleToLookVec(aimAt.getAimPoint(e))))
			.orElse(null);
	}
	
	@Override
	public void onMouseUpdate(MouseUpdateEvent event)
	{
		if(target == null || MC.player == null)
			return;
		
		float curYaw = MC.player.getYaw();
		float curPitch = MC.player.getPitch();
		int diffYaw = (int)(nextYaw - curYaw);
		int diffPitch = (int)(nextPitch - curPitch);
		
		// If we are <1 degree off but still missing the hitbox,
		// slightly exaggerate the difference to fix that.
		if(diffYaw == 0 && diffPitch == 0 && !RotationUtils
			.isFacingBox(target.getBoundingBox(), range.getValue()))
		{
			diffYaw = nextYaw < curYaw ? -1 : 1;
			diffPitch = nextPitch < curPitch ? -1 : 1;
		}
		
		double inputFactor = 1 - ignoreMouseInput.getValue();
		int mouseInputX = (int)(event.getDefaultDeltaX() * inputFactor);
		int mouseInputY = (int)(event.getDefaultDeltaY() * inputFactor);
		
		event.setDeltaX(mouseInputX + diffYaw);
		event.setDeltaY(mouseInputY + diffPitch);
	}
}
