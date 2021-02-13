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

import java.util.Collections;
import com.buuz135.industrial.proxy.client.ClientProxy;
import net.minecraft.util.text.TextComponentTranslation;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.ButtonPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;

public abstract class ArrowInfoPiece extends ButtonPiece {

    private final int textureX;
    private final int textureY;
    private final String display;

    public ArrowInfoPiece(final int left, final int top, final int textureX, final int textureY,
            final String display) {
        super(left, top, 15, 15);
        this.textureX = textureX;
        this.textureY = textureY;
        this.display = display;
    }

    @Override
    protected void renderState(final BasicTeslaGuiContainer<?> basicTeslaGuiContainer,
            final boolean b, final BoundingRectangle boundingRectangle) {

    }

    @Override
    public void drawBackgroundLayer(final BasicTeslaGuiContainer<?> container, final int guiX,
            final int guiY, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);
        container.mc.getTextureManager().bindTexture(ClientProxy.GUI);
        container.drawTexturedRect(this.getLeft() - 1, this.getTop() - 1, textureX, textureY, 15,
                15);
    }

    @Override
    public void drawForegroundTopLayer(final BasicTeslaGuiContainer<?> container, final int guiX,
            final int guiY, final int mouseX, final int mouseY) {
        super.drawForegroundTopLayer(container, guiX, guiY, mouseX, mouseY);
        if (isInside(container, mouseX, mouseY)) {
            container.drawTooltip(
                    Collections.singletonList(
                            new TextComponentTranslation(display).getFormattedText()),
                    mouseX - guiX, mouseY - guiY);
        }
    }
}
