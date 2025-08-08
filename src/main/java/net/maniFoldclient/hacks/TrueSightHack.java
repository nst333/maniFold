/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hacks;

import net.minecraft.entity.Entity;
import net.maniFoldclient.Category;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.settings.filterlists.EntityFilterList;
import net.maniFoldclient.settings.filters.*;

@SearchTags({"true sight"})
public final class TrueSightHack extends Hack
{
	private final EntityFilterList entityFilters =
		new EntityFilterList(FilterHostileSetting.genericVision(false),
			FilterNeutralSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterPassiveSetting.genericVision(false),
			FilterPassiveWaterSetting.genericVision(false),
			FilterBatsSetting.genericVision(false),
			FilterSlimesSetting.genericVision(false),
			FilterPetsSetting.genericVision(false),
			FilterVillagersSetting.genericVision(false),
			FilterZombieVillagersSetting.genericVision(false),
			FilterGolemsSetting.genericVision(false),
			FilterPiglinsSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterZombiePiglinsSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterEndermenSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterShulkersSetting.genericVision(false),
			FilterAllaysSetting.genericVision(false),
			FilterNamedSetting.genericVision(false),
			FilterArmorStandsSetting.genericVision(false));
	
	public TrueSightHack()
	{
		super("TrueSight");
		setCategory(Category.RENDER);
		entityFilters.forEach(this::addSetting);
	}
	
	public boolean shouldBeVisible(Entity entity)
	{
		return isEnabled() && entityFilters.testOne(entity);
	}
	
	// See EntityMixin.onIsInvisibleTo()
}
