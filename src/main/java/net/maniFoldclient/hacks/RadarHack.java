/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.clickgui.Window;
import net.maniFoldclient.clickgui.components.RadarComponent;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.CheckboxSetting;
import net.maniFoldclient.settings.SliderSetting;
import net.maniFoldclient.settings.SliderSetting.ValueDisplay;
import net.maniFoldclient.settings.filterlists.EntityFilterList;
import net.maniFoldclient.settings.filters.*;
import net.maniFoldclient.util.FakePlayerEntity;

@SearchTags({"MiniMap", "mini map"})
public final class RadarHack extends Hack implements UpdateListener
{
	private final Window window;
	private final ArrayList<Entity> entities = new ArrayList<>();
	
	private final SliderSetting radius = new SliderSetting("Radius",
		"Radius in blocks.", 100, 1, 100, 1, ValueDisplay.INTEGER);
	private final CheckboxSetting rotate =
		new CheckboxSetting("Rotate with player", true);
	
	private final EntityFilterList entityFilters =
		new EntityFilterList(FilterPlayersSetting.genericVision(false),
			FilterSleepingSetting.genericVision(false),
			FilterHostileSetting.genericVision(false),
			FilterPassiveSetting.genericVision(false),
			FilterPassiveWaterSetting.genericVision(false),
			FilterBatsSetting.genericVision(true),
			FilterSlimesSetting.genericVision(false),
			FilterInvisibleSetting.genericVision(false));
	
	public RadarHack()
	{
		super("Radar");
		
		setCategory(Category.RENDER);
		addSetting(radius);
		addSetting(rotate);
		entityFilters.forEach(this::addSetting);
		
		window = new Window("Radar");
		window.setPinned(true);
		window.setInvisible(true);
		window.add(new RadarComponent(this));
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		window.setInvisible(false);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		window.setInvisible(true);
	}
	
	@Override
	public void onUpdate()
	{
		ClientPlayerEntity player = MC.player;
		ClientWorld world = MC.world;
		
		entities.clear();
		Stream<Entity> stream =
			StreamSupport.stream(world.getEntities().spliterator(), true)
				.filter(e -> !e.isRemoved() && e != player)
				.filter(e -> !(e instanceof FakePlayerEntity))
				.filter(LivingEntity.class::isInstance)
				.filter(e -> ((LivingEntity)e).getHealth() > 0);
		
		stream = entityFilters.applyTo(stream);
		
		entities.addAll(stream.collect(Collectors.toList()));
	}
	
	public Window getWindow()
	{
		return window;
	}
	
	public Iterable<Entity> getEntities()
	{
		return Collections.unmodifiableList(entities);
	}
	
	public double getRadius()
	{
		return radius.getValue();
	}
	
	public boolean isRotateEnabled()
	{
		return rotate.isChecked();
	}
}
