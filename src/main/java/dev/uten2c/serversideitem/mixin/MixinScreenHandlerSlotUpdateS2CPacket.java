package dev.uten2c.serversideitem.mixin;

import dev.uten2c.serversideitem.ServerSideItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenHandlerSlotUpdateS2CPacket.class)
public class MixinScreenHandlerSlotUpdateS2CPacket {

    @Redirect(method = "<init>(IILnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
    private ItemStack ssi_swapItem(ItemStack stack) {
        stack = stack.copy();
        Item item = stack.getItem();
        if (item instanceof ServerSideItem) {
            return ((ServerSideItem) item).createVisualStack(stack);
        }
        return stack;
    }
}
