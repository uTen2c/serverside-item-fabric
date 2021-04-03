package dev.uten2c.serversideitem;

import dev.uten2c.serversideitem.mixin.accessor.ItemStackAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface ServerSideItem {

    Item getVisualItem();

    @SuppressWarnings("ConstantConditions")
    default ItemStack createVisualStack(@NotNull ItemStack itemStack) {
        ItemStack stack = itemStack.copy();
        Item item = stack.getItem();
        Identifier id = Registry.ITEM.getId(item);
        String translationKey = "item." + id.getNamespace() + "." + id.getPath();
        Text customName = new TranslatableText(translationKey).setStyle(Style.EMPTY.withItalic(false));
        ((ItemStackAccessor) (Object) stack).setItem(getVisualItem());
        if (!stack.hasCustomName()) {
            stack.setCustomName(customName);
        }
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(Constants.TAG_KEY, id.toString());
        return stack;
    }
}
