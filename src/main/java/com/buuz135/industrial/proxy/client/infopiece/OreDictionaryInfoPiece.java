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
package com.buuz135.industrial.proxy.client.infopiece;

import com.buuz135.industrial.proxy.client.ClientProxy;
import com.buuz135.industrial.tile.misc.OreDictionaryConverterTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.ndrei.teslacorelib.gui.BasicRenderedGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;

public class OreDictionaryInfoPiece extends BasicRenderedGuiPiece {

    private final OreDictionaryConverterTile tile;

    public OreDictionaryInfoPiece(final OreDictionaryConverterTile tile, final int left,
            final int top) {
        super(left, top, 131, 26, ClientProxy.GUI, 1, 187);
        this.tile = tile;
    }

    @Override
    public void drawForegroundLayer(final BasicTeslaGuiContainer<?> container, final int guiX,
            final int guiY, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(container, guiX, guiY, mouseX, mouseY);
        if (!tile.getFilter().getStackInSlot(0).isEmpty()) {
            final FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            GlStateManager.pushMatrix();
            renderer.drawString(TextFormatting.DARK_GRAY + "MOD: " + tile.getModid(), 48, 26,
                    0xFFFFFF);
            renderer.drawString(TextFormatting.DARK_GRAY + "ORE: " + tile.getOreDict(), 48,
                    26 + renderer.FONT_HEIGHT, 0xFFFFFF);
            GlStateManager.popMatrix();
        }
    }
}
