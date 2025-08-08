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

@SearchTags({"Clean Up"})
@DontBlock
public final class CleanUpOtf extends OtherFeature
{
	public CleanUpOtf()
	{
		super("CleanUp", "Cleans up your server list.\n"
			+ "To use it, press the 'Clean Up' button on the server selection screen.");
	}
}
