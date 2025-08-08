/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.clickgui.Component;
import net.maniFoldclient.clickgui.components.ItemListEditButton;
import net.maniFoldclient.keybinds.PossibleKeybind;
import net.maniFoldclient.util.json.JsonException;
import net.maniFoldclient.util.json.JsonUtils;
import net.maniFoldclient.util.text.WText;

public final class ItemListSetting extends Setting
{
	private final ArrayList<String> itemNames = new ArrayList<>();
	private final String[] defaultNames;
	
	public ItemListSetting(String name, WText description, String... items)
	{
		super(name, description);
		
		Arrays.stream(items).parallel()
			.map(s -> Registries.ITEM.get(Identifier.of(s)))
			.filter(Objects::nonNull)
			.map(i -> Registries.ITEM.getId(i).toString()).distinct().sorted()
			.forEachOrdered(s -> itemNames.add(s));
		defaultNames = itemNames.toArray(new String[0]);
	}
	
	public ItemListSetting(String name, String descriptionKey, String... items)
	{
		this(name, WText.translated(descriptionKey), items);
	}
	
	public List<String> getItemNames()
	{
		return Collections.unmodifiableList(itemNames);
	}
	
	public void add(Item item)
	{
		String name = Registries.ITEM.getId(item).toString();
		if(Collections.binarySearch(itemNames, name) >= 0)
			return;
		
		itemNames.add(name);
		Collections.sort(itemNames);
		maniFoldClient.INSTANCE.saveSettings();
	}
	
	public void remove(int index)
	{
		if(index < 0 || index >= itemNames.size())
			return;
		
		itemNames.remove(index);
		maniFoldClient.INSTANCE.saveSettings();
	}
	
	public void resetToDefaults()
	{
		itemNames.clear();
		itemNames.addAll(Arrays.asList(defaultNames));
		maniFoldClient.INSTANCE.saveSettings();
	}
	
	@Override
	public Component getComponent()
	{
		return new ItemListEditButton(this);
	}
	
	@Override
	public void fromJson(JsonElement json)
	{
		try
		{
			itemNames.clear();
			
			// if string "default", load default items
			if(JsonUtils.getAsString(json, "nope").equals("default"))
			{
				itemNames.addAll(Arrays.asList(defaultNames));
				return;
			}
			
			// otherwise, load the items in the JSON array
			JsonUtils.getAsArray(json).getAllStrings().parallelStream()
				.map(s -> Registries.ITEM.get(Identifier.of(s)))
				.filter(Objects::nonNull)
				.map(i -> Registries.ITEM.getId(i).toString()).distinct()
				.sorted().forEachOrdered(s -> itemNames.add(s));
			
		}catch(JsonException e)
		{
			e.printStackTrace();
			resetToDefaults();
		}
	}
	
	@Override
	public JsonElement toJson()
	{
		// if itemNames is the same as defaultNames, save string "default"
		if(itemNames.equals(Arrays.asList(defaultNames)))
			return new JsonPrimitive("default");
		
		JsonArray json = new JsonArray();
		itemNames.forEach(s -> json.add(s));
		return json;
	}
	
	@Override
	public JsonObject exportWikiData()
	{
		JsonObject json = new JsonObject();
		json.addProperty("name", getName());
		json.addProperty("description", getDescription());
		json.addProperty("type", "ItemList");
		
		JsonArray defaultItems = new JsonArray();
		Arrays.stream(defaultNames).forEachOrdered(s -> defaultItems.add(s));
		json.add("defaultItems", defaultItems);
		
		return json;
	}
	
	@Override
	public Set<PossibleKeybind> getPossibleKeybinds(String featureName)
	{
		String fullName = featureName + " " + getName();
		
		String command = ".itemlist " + featureName.toLowerCase() + " ";
		command += getName().toLowerCase().replace(" ", "_") + " ";
		
		LinkedHashSet<PossibleKeybind> pkb = new LinkedHashSet<>();
		// Can't just list all the items here. Would need to change UI to allow
		// user to choose an item after selecting this option.
		// pkb.add(new PossibleKeybind(command + "add dirt",
		// "Add dirt to " + fullName));
		// pkb.add(new PossibleKeybind(command + "remove dirt",
		// "Remove dirt from " + fullName));
		pkb.add(new PossibleKeybind(command + "reset", "Reset " + fullName));
		
		return pkb;
	}
}
