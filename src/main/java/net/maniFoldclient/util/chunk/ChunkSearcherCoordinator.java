/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.util.chunk;

import java.util.function.BiPredicate;
import java.util.stream.Stream;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.maniFoldclient.settings.ChunkAreaSetting;
import net.maniFoldclient.util.chunk.ChunkSearcher.Result;

public final class ChunkSearcherCoordinator extends AbstractChunkCoordinator
{
	public ChunkSearcherCoordinator(ChunkAreaSetting area)
	{
		this((pos, state) -> false, area);
	}
	
	public ChunkSearcherCoordinator(BiPredicate<BlockPos, BlockState> query,
		ChunkAreaSetting area)
	{
		super(query, area);
	}
	
	@Override
	public void onReceivedPacket(PacketInputEvent event)
	{
		ChunkPos chunkPos = ChunkUtils.getAffectedChunk(event.getPacket());
		
		if(chunkPos != null)
			chunksToUpdate.add(chunkPos);
	}
	
	public Stream<Result> getMatches()
	{
		return searchers.values().stream().flatMap(ChunkSearcher::getMatches);
	}
}
