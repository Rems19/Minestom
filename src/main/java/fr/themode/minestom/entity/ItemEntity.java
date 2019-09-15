package fr.themode.minestom.entity;

import com.github.simplenet.packet.Packet;
import fr.themode.minestom.item.ItemStack;
import fr.themode.minestom.utils.Utils;

import java.util.function.Consumer;

public class ItemEntity extends ObjectEntity {

    private ItemStack itemStack;
    private boolean pickable = true;

    private long spawnTime;
    private long pickupDelay;

    public ItemEntity(ItemStack itemStack) {
        super(34);
        this.itemStack = itemStack;
        setBoundingBox(0.25f, 0.25f, 0.25f);
        setGravity(0.025f);
    }

    @Override
    public void update() {

    }

    @Override
    public void spawn() {
        this.spawnTime = System.currentTimeMillis();
        // setVelocity(new Vector(0, 1, 0), 5000);
    }

    @Override
    public void addViewer(Player player) {
        super.addViewer(player);
    }

    @Override
    public Consumer<Packet> getMetadataConsumer() {
        return packet -> {
            super.getMetadataConsumer().accept(packet);
            packet.putByte((byte) 7);
            packet.putByte(METADATA_SLOT);
            Utils.writeItemStack(packet, itemStack);
        };
    }

    @Override
    public int getObjectData() {
        return 1;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        sendMetadataIndex(7); // Refresh itemstack for viewers
    }

    public boolean isPickable() {
        return pickable && (System.currentTimeMillis() - getSpawnTime() >= pickupDelay);
    }

    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }

    public long getPickupDelay() {
        return pickupDelay;
    }

    public void setPickupDelay(long pickupDelay) {
        this.pickupDelay = pickupDelay;
    }

    public long getSpawnTime() {
        return spawnTime;
    }
}