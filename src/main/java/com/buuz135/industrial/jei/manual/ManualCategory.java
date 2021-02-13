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
package com.buuz135.industrial.jei.manual;

import com.buuz135.industrial.utils.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;

public class ManualCategory implements IRecipeCategory<ManualWrapper> {

    private final IDrawable drawable;

    public ManualCategory(final IGuiHelper helper) {
        this.drawable = helper.createBlankDrawable(160, 125);
    }

    @Override
    public String getUid() {
        return "if_manual_category";
    }

    @Override
    public String getTitle() {
        return "Industrial Foregoing's Manual";
    }

    @Override
    public String getModName() {
        return Reference.MOD_ID;
    }

    @Override
    public IDrawable getBackground() {
        return drawable;
    }

    @Override
    public void setRecipe(final IRecipeLayout recipeLayout, final ManualWrapper recipeWrapper,
            final IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 70, 0);
        recipeLayout.getItemStacks().set(0, recipeWrapper.getEntry().getDisplay());
    }
}
