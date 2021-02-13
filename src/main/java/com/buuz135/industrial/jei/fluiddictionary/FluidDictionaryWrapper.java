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
package com.buuz135.industrial.jei.fluiddictionary;

import java.util.Arrays;
import java.util.List;
import com.buuz135.industrial.api.recipe.FluidDictionaryEntry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidDictionaryWrapper implements IRecipeWrapper {

    private final FluidDictionaryEntry entry;

    public FluidDictionaryWrapper(final FluidDictionaryEntry entry) {
        this.entry = entry;
    }

    @Override
    public void getIngredients(final IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID,
                new FluidStack(FluidRegistry.getFluid(entry.getFluidOrigin()), 100));
        ingredients.setOutput(VanillaTypes.FLUID, new FluidStack(
                FluidRegistry.getFluid(entry.getFluidResult()), (int) (100 * entry.getRatio())));
    }

    @Override
    public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight,
            final int mouseX, final int mouseY) {

    }

    @Override
    public List<String> getTooltipStrings(final int mouseX, final int mouseY) {
        return Arrays.asList();
    }

    @Override
    public boolean handleClick(final Minecraft minecraft, final int mouseX, final int mouseY,
            final int mouseButton) {
        return false;
    }
}
