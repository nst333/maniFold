/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hack;

import java.util.Objects;

import net.maniFoldclient.Category;
import net.maniFoldclient.Feature;
import net.maniFoldclient.hacks.ClickGuiHack;
import net.maniFoldclient.hacks.NavigatorHack;
import net.maniFoldclient.hacks.TooManyHaxHack;

public abstract class Hack extends Feature
{
	private final String name;
	private final String description;
	private Category category;
	
	private boolean enabled;
	private final boolean stateSaved =
		!getClass().isAnnotationPresent(DontSaveState.class);
	
	public Hack(String name)
	{
		this.name = Objects.requireNonNull(name);
		description = "description.maniFold.hack." + name.toLowerCase();
		addPossibleKeybind(name, "Toggle " + name);
	}
	
	@Override
	public final String getName()
	{
		return name;
	}
	
	public String getRenderName()
	{
		return name;
	}
	
	@Override
	public final String getDescription()
	{
		return MANIFOLD.translate(description);
	}
	
	public final String getDescriptionKey()
	{
		return description;
	}
	
	@Override
	public final Category getCategory()
	{
		return category;
	}
	
	protected final void setCategory(Category category)
	{
		this.category = category;
	}
	
	@Override
	public final boolean isEnabled()
	{
		return enabled;
	}
	
	public final void setEnabled(boolean enabled)
	{
		if(this.enabled == enabled)
			return;
		
		TooManyHaxHack tooManyHax = MANIFOLD.getHax().tooManyHaxHack;
		if(enabled && tooManyHax.isEnabled() && tooManyHax.isBlocked(this))
			return;
		
		this.enabled = enabled;
		
		if(!(this instanceof NavigatorHack || this instanceof ClickGuiHack))
			MANIFOLD.getHud().getHackList().updateState(this);
		
		if(enabled)
			onEnable();
		else
			onDisable();
		
		if(stateSaved)
			MANIFOLD.getHax().saveEnabledHax();
	}
	
	@Override
	public final String getPrimaryAction()
	{
		return enabled ? "Disable" : "Enable";
	}
	
	@Override
	public final void doPrimaryAction()
	{
		setEnabled(!enabled);
	}
	
	public final boolean isStateSaved()
	{
		return stateSaved;
	}
	
	protected void onEnable()
	{
		
	}
	
	protected void onDisable()
	{
		
	}
}
