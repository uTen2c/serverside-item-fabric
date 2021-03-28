package dev.uten2c.serversideitem.mixin;

import dev.uten2c.serversideitem.ServerSideItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(InventoryS2CPacket.class)
public class MixinInventoryS2CPacket<E> {

    @Shadow
    private List<ItemStack> contents;

    @SuppressWarnings("unchecked")
    @Redirect(method = "<init>(ILnet/minecraft/util/collection/DefaultedList;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;set(ILjava/lang/Object;)Ljava/lang/Object;"))
    private E ssi_set(List<E> list, int index, E element) {
        ItemStack stack = (ItemStack) element;
        Item item = stack.getItem();
        if (item instanceof ServerSideItem) {
            stack = ((ServerSideItem) item).createVisualStack(stack);
        }
        return (E) contents.set(index, stack);
    }
}
