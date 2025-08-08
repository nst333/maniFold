/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.events;

import java.util.ArrayList;

import net.minecraft.client.util.math.MatrixStack;
import net.maniFoldclient.event.Event;
import net.maniFoldclient.event.Listener;

public interface RenderListener extends Listener
{
	public void onRender(MatrixStack matrixStack, float partialTicks);
	
	public static class RenderEvent extends Event<RenderListener>
	{
		private final MatrixStack matrixStack;
		private final float partialTicks;
		
		public RenderEvent(MatrixStack matrixStack, float partialTicks)
		{
			this.matrixStack = matrixStack;
			this.partialTicks = partialTicks;
		}
		
		@Override
		public void fire(ArrayList<RenderListener> listeners)
		{
			for(RenderListener listener : listeners)
				listener.onRender(matrixStack, partialTicks);
		}
		
		@Override
		public Class<RenderListener> getListenerType()
		{
			return RenderListener.class;
		}
	}
}
