package dev.uten2c.serversideitem.mixin;

import dev.uten2c.serversideitem.ServerSideItem;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.util.List;

@Mixin(EntityTrackerUpdateS2CPacket.class)
public class MixinEntityTrackerUpdateS2CPacket {

    @SuppressWarnings("unchecked")
    @Redirect(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;entriesToPacket(Ljava/util/List;Lnet/minecraft/network/PacketByteBuf;)V"))
    private <T> void ssi_write(List<DataTracker.Entry<T>> list, PacketByteBuf packetByteBuf) throws IOException {
        for (DataTracker.Entry<T> entry : list) {
            T value = entry.get();
            if (value instanceof ItemStack) {
                ItemStack stack = (ItemStack) value;
                Item item = stack.getItem();
                if (item instanceof ServerSideItem) {
                    stack = ((ServerSideItem) item).createVisualStack(stack);
                    stack.removeCustomName();
                }

                entry.set((T) stack);
            }
        }
        DataTracker.entriesToPacket((List<DataTracker.Entry<?>>) (Object) list, packetByteBuf);
    }
}
