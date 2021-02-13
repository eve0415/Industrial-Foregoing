/*
 * This file is part of Industrial Foregoing.
 *
 * Copyright 2019, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.industrial.book;

import java.util.LinkedHashMap;
import java.util.Map;
import com.buuz135.industrial.api.book.CategoryEntry;
import com.buuz135.industrial.api.book.IBookCategory;
import com.buuz135.industrial.proxy.BlockRegistry;
import com.buuz135.industrial.proxy.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public enum BookCategory implements IBookCategory {
    GETTING_STARTED("getting_started", new ItemStack(ItemRegistry.plastic)), GENERATORS(
            "generators",
            new ItemStack(BlockRegistry.petrifiedFuelGeneratorBlock)), AGRICULTURE("agriculture",
                    new ItemStack(BlockRegistry.cropRecolectorBlock)), ANIMAL_HUSBANDRY(
                            "animal_husbandry", new ItemStack(Items.COOKED_BEEF)), MAGIC("magic",
                                    new ItemStack(BlockRegistry.potionEnervatorBlock)), STORAGE(
                                            "storage",
                                            new ItemStack(BlockRegistry.blackHoleUnitBlock)), MOB(
                                                    "mob_interaction",
                                                    new ItemStack(
                                                            BlockRegistry.mobDetectorBlock)), RESOURCE_PRODUCTION(
                                                                    "resource_production",
                                                                    new ItemStack(
                                                                            BlockRegistry.laserDrillBlock)), ITEM(
                                                                                    "items",
                                                                                    new ItemStack(
                                                                                            ItemRegistry.rangeAddonItem,
                                                                                            1,
                                                                                            11)), CONVEYORS(
                                                                                                    "conveyors",
                                                                                                    new ItemStack(
                                                                                                            BlockRegistry.blockConveyor
                                                                                                                    .getItem()));

    private String name;
    private ItemStack display;
    private Map<ResourceLocation, CategoryEntry> entries;

    BookCategory(final String name, final ItemStack display) {
        this.name = new TextComponentTranslation("text.industrialforegoing.book.category." + name)
                .getFormattedText();
        this.display = display;
        entries = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public Map<ResourceLocation, CategoryEntry> getEntries() {
        return entries;
    }
}
