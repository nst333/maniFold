/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.AttackSpeedSliderSetting;
import net.maniFoldclient.settings.PauseAttackOnContainersSetting;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;
import net.maniFoldclient.settings.SwingHandSetting;
import net.maniFoldclient.settings.SwingHandSetting.SwingHand;
import net.maniFoldclient.settings.filterlists.EntityFilterList;
import net.maniFoldclient.util.EntityUtils;
import net.maniFoldclient.util.RotationUtils;

@SearchTags({"multi aura", "ForceField", "force field"})
public final class MultiAuraHack extends Hack implements UpdateListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	private final AttackSpeedSliderSetting speed =
		new AttackSpeedSliderSetting();
	
	private final SliderSetting fov =
		new SliderSetting("FOV", 360, 30, 360, 10, ValueDisplay.DEGREES);
	
	private final SwingHandSetting swingHand = new SwingHandSetting(
		SwingHandSetting.genericCombatDescription(this), SwingHand.CLIENT);
	
	private final PauseAttackOnContainersSetting pauseOnContainers =
		new PauseAttackOnContainersSetting(false);
	
	private final EntityFilterList entityFilters =
		EntityFilterList.genericCombat();
	
	public MultiAuraHack()
	{
		super("MultiAura");
		setCategory(Category.COMBAT);
		
		addSetting(range);
		addSetting(speed);
		addSetting(fov);
		addSetting(swingHand);
		addSetting(pauseOnContainers);
		
		entityFilters.forEach(this::addSetting);
	}
	
	@Override
	protected void onEnable()
	{
		// disable other killauras
		MANIFOLD.getHax().aimAssistHack.setEnabled(false);
		MANIFOLD.getHax().clickAuraHack.setEnabled(false);
		MANIFOLD.getHax().crystalAuraHack.setEnabled(false);
		MANIFOLD.getHax().fightBotHack.setEnabled(false);
		MANIFOLD.getHax().killauraLegitHack.setEnabled(false);
		MANIFOLD.getHax().killauraHack.setEnabled(false);
		MANIFOLD.getHax().protectHack.setEnabled(false);
		MANIFOLD.getHax().tpAuraHack.setEnabled(false);
		MANIFOLD.getHax().triggerBotHack.setEnabled(false);
		
		speed.resetTimer();
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
		speed.updateTimer();
		if(!speed.isTimeToAttack())
			return;
		
		if(pauseOnContainers.shouldPause())
			return;
		
		// get entities
		Stream<Entity> stream = EntityUtils.getAttackableEntities();
		double rangeSq = Math.pow(range.getValue(), 2);
		stream = stream.filter(e -> MC.player.squaredDistanceTo(e) <= rangeSq);
		
		if(fov.getValue() < 360.0)
			stream = stream.filter(e -> RotationUtils.getAngleToLookVec(
				e.getBoundingBox().getCenter()) <= fov.getValue() / 2.0);
		
		stream = entityFilters.applyTo(stream);
		
		ArrayList<Entity> entities =
			stream.collect(Collectors.toCollection(ArrayList::new));
		if(entities.isEmpty())
			return;
		
		MANIFOLD.getHax().autoSwordHack.setSlot(entities.get(0));
		
		// attack entities
		for(Entity entity : entities)
		{
			RotationUtils
				.getNeededRotations(entity.getBoundingBox().getCenter())
				.sendPlayerLookPacket();
			
			MC.interactionManager.attackEntity(MC.player, entity);
		}
		
		swingHand.swing(Hand.MAIN_HAND);
		speed.resetTimer();
	}
}
