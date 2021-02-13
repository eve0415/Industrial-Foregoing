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
package com.buuz135.industrial.api.book.button;

import com.buuz135.industrial.api.book.CategoryEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.text.TextFormatting;

public class CategoryEntryButton extends GuiButton {

    private final CategoryEntry entry;
    private boolean textTooBig;

    public CategoryEntryButton(final int buttonId, final int x, final int y, final int widthIn,
            final int heightIn, final String buttonText, final CategoryEntry entry) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.entry = entry;
    }

    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY,
            final float partialTicks) {
        if (visible) {
            this.hovered =
                    mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.color(1f, 1f, 1f);
            mc.getRenderItem().renderItemIntoGUI(entry.getDisplay(), x, y);
            final String displayString = TextFormatting.DARK_BLUE + entry.getName();
            final int sw = mc.fontRenderer.getStringWidth(displayString);
            textTooBig = sw > width - 20;
            mc.fontRenderer
                    .drawString(textTooBig
                            ? displayString.substring(0,
                                    (displayString.length() * (width - 25)) / sw) + "..."
                            : displayString, x + 20, y + 4, 0xFFFFFF);

        }
    }

    public CategoryEntry getEntry() {
        return entry;
    }

    public boolean isTextTooBig() {
        return textTooBig;
    }
}
