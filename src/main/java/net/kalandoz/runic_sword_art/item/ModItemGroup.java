package net.kalandoz.runic_sword_art.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
    public static final ItemGroup RUNIC_GROUP = new ItemGroup("runicSwordArt") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.RUNIC_FRAGMENT.get());
        }
    };
}