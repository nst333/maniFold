/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.other_features;

import net.maniFoldclient.DontBlock;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.other_feature.OtherFeature;
import net.maniFoldclient.settings.CheckboxSetting;

@SearchTags({"turn off", "hide maniFold logo", "ghost mode", "stealth mode",
	"vanilla Minecraft"})
@DontBlock
public final class DisableOtf extends OtherFeature
{
	private final CheckboxSetting hideEnableButton = new CheckboxSetting(
		"Hide enable button",
		"Removes the \"Enable maniFold\" button as soon as you close the Statistics screen."
			+ " You will have to restart the game to re-enable maniFold.",
		false);
	
	public DisableOtf()
	{
		super("Disable maniFold",
			"To disable maniFold, go to the Statistics screen and press the \"Disable maniFold\" button.\n"
				+ "It will turn into an \"Enable maniFold\" button once pressed.");
		addSetting(hideEnableButton);
	}
	
	public boolean shouldHideEnableButton()
	{
		return !MANIFOLD.isEnabled() && hideEnableButton.isChecked();
	}
}
