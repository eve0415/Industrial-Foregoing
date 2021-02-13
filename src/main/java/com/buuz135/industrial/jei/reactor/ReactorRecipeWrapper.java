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
package com.buuz135.industrial.jei.reactor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.buuz135.industrial.proxy.BlockRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ReactorRecipeWrapper implements IRecipeWrapper {

    private final ItemStack stack;
    private final Fluid fluid;
    private final int amount;

    public ReactorRecipeWrapper(final ItemStack stack, final Fluid fluid, final int amount) {
        this.stack = stack;
        this.fluid = fluid;
        this.amount = amount;
    }

    @Override
    public void getIngredients(final IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, stack);
        ingredients.setOutput(VanillaTypes.FLUID, new FluidStack(fluid, amount));
    }

    @Override
    public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight,
            final int mouseX, final int mouseY) {

    }

    @Override
    public List<String> getTooltipStrings(final int mouseX, final int mouseY) {
        if (mouseX >= 18 && mouseX <= 58)
            return Arrays.asList("Efficiency",
                    " Min: " + BlockRegistry.bioReactorBlock.getBaseAmount() + "mb/item",
                    " Max: " + BlockRegistry.bioReactorBlock.getBaseAmount() * 2 + "mb/item");
        return new ArrayList<>();
    }

    @Override
    public boolean handleClick(final Minecraft minecraft, final int mouseX, final int mouseY,
            final int mouseButton) {
        return false;
    }
}
