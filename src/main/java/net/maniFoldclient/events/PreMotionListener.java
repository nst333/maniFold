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

public interface PreMotionListener extends Listener
{
	public void onPreMotion();
	
	public static class PreMotionEvent extends Event<PreMotionListener>
	{
		public static final PreMotionEvent INSTANCE = new PreMotionEvent();
		
		@Override
		public void fire(ArrayList<PreMotionListener> listeners)
		{
			for(PreMotionListener listener : listeners)
				listener.onPreMotion();
		}
		
		@Override
		public Class<PreMotionListener> getListenerType()
		{
			return PreMotionListener.class;
		}
	}
}
