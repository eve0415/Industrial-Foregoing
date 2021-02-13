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
package com.buuz135.industrial.jei.sludge;

import java.text.DecimalFormat;
import java.util.List;
import com.buuz135.industrial.proxy.FluidsRegistry;
import com.buuz135.industrial.utils.ItemStackWeightedItem;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

public class SludgeRefinerRecipeWrapper implements IRecipeWrapper {

    private final ItemStackWeightedItem item;
    private final int maxWeight;

    public SludgeRefinerRecipeWrapper(final ItemStackWeightedItem item, final int maxWeight) {
        this.item = item;
        this.maxWeight = maxWeight;
    }

    @Override
    public void getIngredients(final IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, new FluidStack(FluidsRegistry.SLUDGE, 1000));
        ingredients.setOutput(VanillaTypes.ITEM, item.getStack());
    }

    @Override
    public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight,
            final int mouseX, final int mouseY) {
        final String chance = "Chance: "
                + new DecimalFormat("##.##").format((item.itemWeight / (double) maxWeight) * 100)
                + "%";
        minecraft.fontRenderer.drawString(TextFormatting.DARK_GRAY + chance,
                recipeWidth / 2 - minecraft.fontRenderer.getStringWidth(chance) / 2, 55, 0xFFFFFF);
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
}
