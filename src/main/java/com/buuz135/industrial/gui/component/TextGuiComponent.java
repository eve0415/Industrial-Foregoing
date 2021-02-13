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
package com.buuz135.industrial.gui.component;


import java.util.List;
import javax.annotation.Nullable;
import com.buuz135.industrial.api.conveyor.gui.PositionedGuiComponent;
import com.buuz135.industrial.gui.conveyor.GuiConveyor;
import net.minecraft.client.Minecraft;

public abstract class TextGuiComponent extends PositionedGuiComponent {

    public TextGuiComponent(final int x, final int y) {
        super(x, y, 0, 0);
    }

    @Override
    public void handleClick(final GuiConveyor conveyor, final int guiX, final int guiY,
            final int mouseX, final int mouseY) {

    }

    @Override
    public void drawGuiBackgroundLayer(final int guiX, final int guiY, final int mouseX,
            final int mouseY) {
        Minecraft.getMinecraft().fontRenderer.drawString(getText(), guiX + getXPos(),
                guiY + getYPos(), 0xffffff);
    }

    @Override
    public void drawGuiForegroundLayer(final int guiX, final int guiY, final int mouseX,
            final int mouseY) {

    }

    @Nullable
    @Override
    public List<String> getTooltip(final int guiX, final int guiY, final int mouseX,
            final int mouseY) {
        return null;
    }

    public abstract String getText();
}
