/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.other_features;

import net.maniFoldclient.DontBlock;
import net.maniFoldclient.other_feature.OtherFeature;

@DontBlock
public final class ReconnectOtf extends OtherFeature
{
	public ReconnectOtf()
	{
		super("Reconnect",
			"Whenever you get kicked from a server, maniFold gives you a \"Reconnect\" button that lets you instantly join again.");
	}
}
