/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.MinecraftClient;
import net.maniFoldclient.event.EventManager;
import net.maniFoldclient.keybinds.PossibleKeybind;
import net.maniFoldclient.mixinterface.IMinecraftClient;
import net.maniFoldclient.settings.Setting;
import net.maniFoldclient.util.ChatUtils;

public abstract class Feature
{
	protected static final maniFoldClient MANIFOLD = maniFoldClient.INSTANCE;
	protected static final EventManager EVENTS = MANIFOLD.getEventManager();
	protected static final MinecraftClient MC = maniFoldClient.MC;
	protected static final IMinecraftClient IMC = maniFoldClient.IMC;
	
	private final LinkedHashMap<String, Setting> settings =
		new LinkedHashMap<>();
	private final LinkedHashSet<PossibleKeybind> possibleKeybinds =
		new LinkedHashSet<>();
	
	private final String searchTags =
		getClass().isAnnotationPresent(SearchTags.class) ? String.join("\u00a7",
			getClass().getAnnotation(SearchTags.class).value()) : "";
	
	private final boolean safeToBlock =
		!getClass().isAnnotationPresent(DontBlock.class);
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	public String getWrappedDescription(int width)
	{
		return ChatUtils.wrapText(getDescription(), width);
	}
	
	public Category getCategory()
	{
		return null;
	}
	
	public abstract String getPrimaryAction();
	
	public void doPrimaryAction()
	{
		
	}
	
	public boolean isEnabled()
	{
		return false;
	}
	
	public final Map<String, Setting> getSettings()
	{
		return Collections.unmodifiableMap(settings);
	}
	
	protected final void addSetting(Setting setting)
	{
		String key = setting.getName().toLowerCase();
		
		if(settings.containsKey(key))
			throw new IllegalArgumentException(
				"Duplicate setting: " + getName() + " " + key);
		
		settings.put(key, setting);
		possibleKeybinds.addAll(setting.getPossibleKeybinds(getName()));
	}
	
	protected final void addPossibleKeybind(String command, String description)
	{
		possibleKeybinds.add(new PossibleKeybind(command, description));
	}
	
	public final Set<PossibleKeybind> getPossibleKeybinds()
	{
		return Collections.unmodifiableSet(possibleKeybinds);
	}
	
	public final String getSearchTags()
	{
		return searchTags;
	}
	
	public final boolean isSafeToBlock()
	{
		return safeToBlock;
	}
}
