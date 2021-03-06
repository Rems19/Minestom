package net.minestom.server.utils.entity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.BlockPosition;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.chunk.ChunkUtils;

public class EntityUtils {

    public static boolean areVisible(Entity ent1, Entity ent2) {
        if (ent1.getInstance() == null || ent2.getInstance() == null)
            return false;
        if (!ent1.getInstance().equals(ent2.getInstance()))
            return false;

        Chunk chunk = ent1.getInstance().getChunkAt(ent1.getPosition());

        long[] visibleChunksEntity = ChunkUtils.getChunksInRange(ent2.getPosition(), MinecraftServer.ENTITY_VIEW_DISTANCE);
        for (long visibleChunk : visibleChunksEntity) {
            int[] chunkPos = ChunkUtils.getChunkCoord(visibleChunk);
            int chunkX = chunkPos[0];
            int chunkZ = chunkPos[1];
            if (chunk.getChunkX() == chunkX && chunk.getChunkZ() == chunkZ)
                return true;
        }

        return false;
    }

    public static boolean isOnGround(Entity entity) {
        Instance instance = entity.getInstance();
        if (instance == null)
            return false;

        Position entityPosition = entity.getPosition();

        // TODO: check entire bounding box
        BlockPosition blockPosition = entityPosition.toBlockPosition();
        blockPosition = blockPosition.subtract(0, 1, 0);
        try {
            short blockId = instance.getBlockId(blockPosition);
            Block block = Block.fromId(blockId);
            return block.isSolid();
        } catch (NullPointerException e) {
            // Probably an entity at the border of an unloaded chunk
            return false;
        }
    }

}
