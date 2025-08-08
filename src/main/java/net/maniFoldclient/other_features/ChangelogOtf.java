/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.other_features;

import net.minecraft.util.Util;
import net.maniFoldclient.DontBlock;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.other_feature.OtherFeature;
import net.maniFoldclient.update.Version;

@SearchTags({"change log", "maniFold update", "release notes", "what's new",
	"what is new", "new features", "recently added features"})
@DontBlock
public final class ChangelogOtf extends OtherFeature
{
	public ChangelogOtf()
	{
		super("Changelog", "Opens the changelog in your browser.");
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "View Changelog";
	}
	
	@Override
	public void doPrimaryAction()
	{
		String link = new Version(maniFoldClient.VERSION).getChangelogLink()
			+ "?utm_source=maniFold+Client&utm_medium=ChangelogOtf&utm_content=View+Changelog";
		Util.getOperatingSystem().open(link);
	}
}
