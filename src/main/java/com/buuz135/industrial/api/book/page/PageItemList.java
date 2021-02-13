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
package com.buuz135.industrial.api.book.page;

import java.util.ArrayList;
import java.util.List;
import com.buuz135.industrial.api.book.CategoryEntry;
import com.buuz135.industrial.api.book.IPage;
import com.buuz135.industrial.api.book.gui.GUIBookBase;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class PageItemList implements IPage {

    private final List<ItemStack> itemStacks;
    private final String display;

    public PageItemList(final List<ItemStack> itemStacks, final String display) {
        this.itemStacks = itemStacks;
        this.display = display;
    }

    public static List<PageItemList> generatePagesFromItemStacks(List<ItemStack> stacks,
            final String display) {
        final List<PageItemList> pages = new ArrayList<>();
        while (stacks.size() > 49) {
            pages.add(new PageItemList(stacks.subList(0, 49), display));
            stacks = stacks.subList(49, stacks.size());
        }
        pages.add(new PageItemList(stacks, display));
        return pages;
    }

    @Override
    public void drawScreenPre(final CategoryEntry entry, final GUIBookBase base, final int mouseX,
            final int mouseY, final float partialTicks, final FontRenderer renderer) {
        renderer.drawString(TextFormatting.DARK_GRAY + display, base.getGuiLeft() + 15,
                base.getGuiTop() + 24, 0xFFFFFF);
        RenderHelper.enableGUIStandardItemLighting();
        final int itemsRow = (base.getGuiXSize() - 40) / 18;
        for (int pos = 0; pos < itemStacks.size(); ++pos) {
            base.mc.getRenderItem().renderItemIntoGUI(itemStacks.get(pos),
                    base.getGuiLeft() + 15 + (pos % itemsRow) * 20,
                    base.getGuiTop() + 35 + (pos / itemsRow) * 20);
        }
    }

    @Override
    public void drawScreen(final CategoryEntry entry, final GUIBookBase base, final int mouseX,
            final int mouseY, final float partialTicks, final FontRenderer renderer) {

    }

    @Override
    public void drawScreenPost(final CategoryEntry entry, final GUIBookBase base, final int mouseX,
            final int mouseY, final float partialTicks, final FontRenderer renderer) {
        for (int pos = 0; pos < itemStacks.size(); ++pos) {
            final int itemsRow = (base.getGuiXSize() - 40) / 18;
            if (mouseX >= base.getGuiLeft() + 15 + (pos % itemsRow) * 20
                    && mouseX <= base.getGuiLeft() + 15 + (pos % itemsRow) * 20 + 16
                    && mouseY >= base.getGuiTop() + 35 + (pos / itemsRow) * 20
                    && mouseY <= base.getGuiTop() + 35 + (pos / itemsRow) * 20 + 16) {
                base.drawHoveringText(
                        itemStacks.get(pos).getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL),
                        mouseX, mouseY);
                break;
            }
        }
    }
}
