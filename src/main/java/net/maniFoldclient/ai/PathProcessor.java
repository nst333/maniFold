/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.ai;

import java.util.ArrayList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.mixinterface.IKeyBinding;

public abstract class PathProcessor
{
	protected static final maniFoldClient MANIFOLD = maniFoldClient.INSTANCE;
	protected static final MinecraftClient MC = maniFoldClient.MC;
	
	private static final KeyBinding[] CONTROLS =
		{MC.options.forwardKey, MC.options.backKey, MC.options.rightKey,
			MC.options.leftKey, MC.options.jumpKey, MC.options.sneakKey};
	
	protected final ArrayList<PathPos> path;
	protected int index;
	protected boolean done;
	protected int ticksOffPath;
	
	public PathProcessor(ArrayList<PathPos> path)
	{
		if(path.isEmpty())
			throw new IllegalStateException("There is no path!");
		
		this.path = path;
	}
	
	public abstract void process();
	
	public abstract boolean canBreakBlocks();
	
	public final int getIndex()
	{
		return index;
	}
	
	public final boolean isDone()
	{
		return done;
	}
	
	public final int getTicksOffPath()
	{
		return ticksOffPath;
	}
	
	protected final void facePosition(BlockPos pos)
	{
		MANIFOLD.getRotationFaker()
			.faceVectorClientIgnorePitch(Vec3d.ofCenter(pos));
	}
	
	public static final void lockControls()
	{
		// disable keys
		for(KeyBinding key : CONTROLS)
			key.setPressed(false);
		
		// disable sprinting
		MC.player.setSprinting(false);
	}
	
	public static final void releaseControls()
	{
		// reset keys
		for(KeyBinding key : CONTROLS)
			IKeyBinding.get(key).resetPressedState();
	}
}
