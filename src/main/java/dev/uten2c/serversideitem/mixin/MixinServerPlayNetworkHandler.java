package dev.uten2c.serversideitem.mixin;

import dev.uten2c.serversideitem.Constants;
import dev.uten2c.serversideitem.ServerSideItem;
import dev.uten2c.serversideitem.mixin.accessor.ItemStackAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

    @Redirect(method = "onCreativeInventoryAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/c2s/play/CreativeInventoryActionC2SPacket;getItemStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack ssi_swapStack(CreativeInventoryActionC2SPacket packet) {
        ItemStack stack = packet.getItemStack();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(Constants.TAG_KEY)) {
            Identifier id = new Identifier(tag.getString(Constants.TAG_KEY));
            Item item = Registry.ITEM.get(id);
            if (item instanceof ServerSideItem) {
                return ssi_convertStack(stack);
            }
        }
        return stack;
    }

    @SuppressWarnings("ConstantConditions")
    private ItemStack ssi_convertStack(ItemStack stack) {
        ItemStack copy = stack.copy();
        Identifier id = new Identifier(copy.getOrCreateTag().getString(Constants.TAG_KEY));
        Item item = Registry.ITEM.get(id);
        ((ItemStackAccessor) (Object) copy).setItem(item);
        CompoundTag tag = copy.getTag();
        if (tag != null) {
            tag.remove(Constants.TAG_KEY);
            ItemStack defaultVisualStack = ((ServerSideItem) item).createVisualStack(item.getDefaultStack());
            CompoundTag displayTag = copy.getSubTag("display");
            if (displayTag.get("Name").equals(defaultVisualStack.getSubTag("display").get("Name"))) {
                displayTag.remove("Name");

                if (displayTag.isEmpty()) {
                    tag.remove("display");
                }
            }
            if (tag.isEmpty()) {
                copy.setTag(null);
            }
        }
        return copy;
    }
}
