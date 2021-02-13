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
package com.buuz135.industrial.item.addon;

import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.tile.api.IAcceptsFortuneAddon;
import com.buuz135.industrial.utils.RecipeUtils;
import org.jetbrains.annotations.NotNull;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

public class FortuneAddonItem extends CustomAddon {

    public FortuneAddonItem() {
        super("fortune_addon");
        setMaxStackSize(1);
    }

    @Override
    public void createRecipe() {
        RecipeUtils.addShapedRecipe(new ItemStack(this), "gpg", "gmg", "gpg", 'g', Items.EMERALD,
                'p', ItemRegistry.plastic, 'm', ItemRegistry.pinkSlimeIngot);
    }

    @Override
    public boolean canBeAddedTo(@NotNull final SidedTileEntity machine) {
        return machine instanceof IAcceptsFortuneAddon;
    }

    @Override
    public boolean isEnchantable(final ItemStack stack) {
        return !stack.isItemEnchanted();
    }

    @Override
    public int getItemEnchantability() {
        return 2;
    }

    @Override
    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        return !stack.isItemEnchanted() && Enchantment.getEnchantmentID(enchantment) == 35;
    }

    public int getLevel(final ItemStack stack) {
        if (stack.isItemEnchanted()) {
            return EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(35), stack);
        }
        return 0;
    }
}
