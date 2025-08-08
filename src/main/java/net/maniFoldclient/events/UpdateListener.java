/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.events;

import java.util.ArrayList;

import net.maniFoldclient.event.Event;
import net.maniFoldclient.event.Listener;

public interface UpdateListener extends Listener
{
	public void onUpdate();
	
	public static class UpdateEvent extends Event<UpdateListener>
	{
		public static final UpdateEvent INSTANCE = new UpdateEvent();
		
		@Override
		public void fire(ArrayList<UpdateListener> listeners)
		{
			for(UpdateListener listener : listeners)
				listener.onUpdate();
		}
		
		@Override
		public Class<UpdateListener> getListenerType()
		{
			return UpdateListener.class;
		}
	}
}
