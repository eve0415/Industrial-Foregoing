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
package com.buuz135.industrial.jei.petrifiedgen;

import java.awt.Color;
import java.util.List;
import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class PetrifiedBurnTimeWrapper implements IRecipeWrapper {

    private final ItemStack stack;
    private final int burnTime;

    public PetrifiedBurnTimeWrapper(final ItemStack stack, final int burnTime) {
        this.stack = stack;
        this.burnTime = burnTime;
    }

    @Override
    public void getIngredients(final IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, stack);
    }

    @Override
    public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight,
            final int mouseX, final int mouseY) {
        minecraft.fontRenderer.drawString(
                "Power: " + PetrifiedFuelGeneratorTile.getEnergy(this.burnTime) + " RF/tick", 24, 8,
                Color.gray.getRGB());
    }

    @Override
    public List<String> getTooltipStrings(final int mouseX, final int mouseY) {
        return null;
    }

    @Override
    public boolean handleClick(final Minecraft minecraft, final int mouseX, final int mouseY,
            final int mouseButton) {
        return false;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getBurnTime() {
        return burnTime;
    }
}
