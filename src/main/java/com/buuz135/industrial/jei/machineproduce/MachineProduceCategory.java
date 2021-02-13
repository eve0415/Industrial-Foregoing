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
package com.buuz135.industrial.jei.machineproduce;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import com.buuz135.industrial.utils.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class MachineProduceCategory implements IRecipeCategory<MachineProduceWrapper> {
    private final IGuiHelper guiHelper;

    public MachineProduceCategory(final IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }

    @Override
    public String getUid() {
        return "machine_produce_category";
    }

    @Override
    public String getTitle() {
        return "Machine Outputs";
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(
                new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 0, 0, 82, 26);
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
            final MachineProduceWrapper recipeWrapper, final IIngredients ingredients) {
        final IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();
        guiItemStackGroup.init(0, true, 0, 4);
        guiItemStackGroup.init(1, false, 60, 4);
        guiItemStackGroup.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
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
