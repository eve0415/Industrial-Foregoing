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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import com.buuz135.industrial.utils.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class SludgeRefinerRecipeCategory implements IRecipeCategory<SludgeRefinerRecipeWrapper> {

    private final IGuiHelper guiHelper;
    private final IDrawable tankOverlay;

    public SludgeRefinerRecipeCategory(final IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        tankOverlay = guiHelper.createDrawable(
                new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 1, 207, 12, 48);
    }

    @Override
    public String getUid() {
        return "sludge_refiner_category";
    }

    @Override
    public String getTitle() {
        return "Sludge Refiner";
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper
                .drawableBuilder(new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 0,
                        78, 81, 50)
                .addPadding(0, 16, 0, 0).build();
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void drawExtras(final Minecraft minecraft) {

    }

    @Override
    public void setRecipe(final IRecipeLayout recipeLayout,
            final SludgeRefinerRecipeWrapper recipeWrapper, final IIngredients ingredients) {
        final IGuiFluidStackGroup guiFluidStackGroup = recipeLayout.getFluidStacks();

        guiFluidStackGroup.init(0, true, 5, 1, 12, 48, 8000, false, tankOverlay);

        final IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();

        guiItemStackGroup.init(1, false, 59, 17);
        guiFluidStackGroup.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
        guiItemStackGroup.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    public List<String> getTooltipStrings(final int mouseX, final int mouseY) {
        return new ArrayList<>();
    }

    @Override
    public String getModName() {
        return Reference.NAME;
    }
}
