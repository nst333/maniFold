/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.settings;

import java.util.function.Consumer;

import net.minecraft.util.math.Vec3d;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.util.RotationUtils;

public final class FacingSetting extends EnumSetting<FacingSetting.Facing>
{
	private static final maniFoldClient MANIFOLD = maniFoldClient.INSTANCE;
	
	private FacingSetting(String name, String description, Facing[] values,
		Facing selected)
	{
		super(name, description, values, selected);
	}
	
	public static FacingSetting withoutPacketSpam(String description)
	{
		return withoutPacketSpam("Facing", description, Facing.SERVER);
	}
	
	public static FacingSetting withoutPacketSpam(String name,
		String description, Facing selected)
	{
		Facing[] values = {Facing.OFF, Facing.SERVER, Facing.CLIENT};
		return new FacingSetting(name, description, values, selected);
	}
	
	public static FacingSetting withPacketSpam(String name, String description,
		Facing selected)
	{
		return new FacingSetting(name, description, Facing.values(), selected);
	}
	
	public enum Facing
	{
		OFF("Off", v -> {}),
		
		SERVER("Server-side",
			v -> MANIFOLD.getRotationFaker().faceVectorPacket(v)),
		
		CLIENT("Client-side",
			v -> MANIFOLD.getRotationFaker().faceVectorClient(v)),
		
		SPAM("Packet spam",
			v -> RotationUtils.getNeededRotations(v).sendPlayerLookPacket());
		
		private String name;
		private Consumer<Vec3d> face;
		
		private Facing(String name, Consumer<Vec3d> face)
		{
			this.name = name;
			this.face = face;
		}
		
		public void face(Vec3d v)
		{
			face.accept(v);
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
