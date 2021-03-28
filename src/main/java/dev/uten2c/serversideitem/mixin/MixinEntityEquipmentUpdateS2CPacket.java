package dev.uten2c.serversideitem.mixin;

import com.mojang.datafixers.util.Pair;
import dev.uten2c.serversideitem.ServerSideItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityEquipmentUpdateS2CPacket.class)
public class MixinEntityEquipmentUpdateS2CPacket {

    @Redirect(method = "write", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;getSecond()Ljava/lang/Object;"), remap = false)
    private Object swapStack(Pair<EquipmentSlot, ItemStack> pair) {
        ItemStack stack = pair.getSecond();
        Item item = stack.getItem();
        if (item instanceof ServerSideItem) {
            return ((ServerSideItem) item).createVisualStack(stack);
        }
        return stack;
    }
}
