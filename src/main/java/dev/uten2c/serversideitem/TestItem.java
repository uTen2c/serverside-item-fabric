package dev.uten2c.serversideitem;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class TestItem extends Item implements ServerSideItem {

    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getVisualItem() {
        return Items.DIAMOND;
    }
}
