/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.events.LeftClickListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.AttackSpeedSliderSetting;
import net.maniFoldclient.settings.EnumSetting;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;
import net.maniFoldclient.settings.filterlists.EntityFilterList;
import net.maniFoldclient.util.EntityUtils;
import net.maniFoldclient.util.RotationUtils;

@SearchTags({"click aura", "ClickAimbot", "click aimbot"})
public final class ClickAuraHack extends Hack
	implements UpdateListener, LeftClickListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 10, 0.05, ValueDisplay.DECIMAL);
	
	private final AttackSpeedSliderSetting speed =
		new AttackSpeedSliderSetting();
	
	private final EnumSetting<Priority> priority = new EnumSetting<>("Priority",
		"Determines which entity will be attacked first.\n"
			+ "\u00a7lDistance\u00a7r - Attacks the closest entity.\n"
			+ "\u00a7lAngle\u00a7r - Attacks the entity that requires the least head movement.\n"
			+ "\u00a7lHealth\u00a7r - Attacks the weakest entity.",
		Priority.values(), Priority.ANGLE);
	
	private final SliderSetting fov =
		new SliderSetting("FOV", 360, 30, 360, 10, ValueDisplay.DEGREES);
	
	private final EntityFilterList entityFilters =
		EntityFilterList.genericCombat();
	
	public ClickAuraHack()
	{
		super("ClickAura");
		
		setCategory(Category.COMBAT);
		addSetting(range);
		addSetting(speed);
		addSetting(priority);
		addSetting(fov);
		
		entityFilters.forEach(this::addSetting);
	}
	
	@Override
	protected void onEnable()
	{
		// disable other killauras
		MANIFOLD.getHax().aimAssistHack.setEnabled(false);
		MANIFOLD.getHax().crystalAuraHack.setEnabled(false);
		MANIFOLD.getHax().fightBotHack.setEnabled(false);
		MANIFOLD.getHax().killauraLegitHack.setEnabled(false);
		MANIFOLD.getHax().killauraHack.setEnabled(false);
		MANIFOLD.getHax().multiAuraHack.setEnabled(false);
		MANIFOLD.getHax().protectHack.setEnabled(false);
		MANIFOLD.getHax().triggerBotHack.setEnabled(false);
		MANIFOLD.getHax().tpAuraHack.setEnabled(false);
		
		speed.resetTimer();
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(LeftClickListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(LeftClickListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(!MC.options.attackKey.isPressed())
			return;
		
		speed.updateTimer();
		if(!speed.isTimeToAttack())
			return;
		
		attack();
	}
	
	@Override
	public void onLeftClick(LeftClickEvent event)
	{
		attack();
	}
	
	private void attack()
	{
		// set entity
		ClientPlayerEntity player = MC.player;
		Stream<Entity> stream = EntityUtils.getAttackableEntities();
		
		double rangeSq = Math.pow(range.getValue(), 2);
		stream = stream.filter(e -> player.squaredDistanceTo(e) <= rangeSq);
		
		if(fov.getValue() < 360.0)
			stream = stream.filter(e -> RotationUtils.getAngleToLookVec(
				e.getBoundingBox().getCenter()) <= fov.getValue() / 2.0);
		
		stream = entityFilters.applyTo(stream);
		
		Entity target =
			stream.min(priority.getSelected().comparator).orElse(null);
		if(target == null)
			return;
		
		MANIFOLD.getHax().autoSwordHack.setSlot(target);
		
		// face entity
		RotationUtils.getNeededRotations(target.getBoundingBox().getCenter())
			.sendPlayerLookPacket();
		
		// attack entity
		MC.interactionManager.attackEntity(player, target);
		player.swingHand(Hand.MAIN_HAND);
		speed.resetTimer();
	}
	
	private enum Priority
	{
		DISTANCE("Distance", e -> MC.player.squaredDistanceTo(e)),
		
		ANGLE("Angle",
			e -> RotationUtils
				.getAngleToLookVec(e.getBoundingBox().getCenter())),
		
		HEALTH("Health", e -> e instanceof LivingEntity
			? ((LivingEntity)e).getHealth() : Integer.MAX_VALUE);
		
		private final String name;
		private final Comparator<Entity> comparator;
		
		private Priority(String name, ToDoubleFunction<Entity> keyExtractor)
		{
			this.name = name;
			comparator = Comparator.comparingDouble(keyExtractor);
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
