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

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import com.buuz135.industrial.api.conveyor.gui.PositionedGuiComponent;
import com.buuz135.industrial.gui.conveyor.GuiConveyor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TexturedStateButtonGuiComponent extends PositionedGuiComponent {

    private final int id;
    private StateButtonInfo[] buttonInfos;

    public TexturedStateButtonGuiComponent(final int id, final int x, final int y, final int xSize,
            final int ySize, final StateButtonInfo... buttonInfos) {
        super(x, y, xSize, ySize);
        this.id = id;
        this.buttonInfos = new StateButtonInfo[] {};
        if (buttonInfos != null)
            this.buttonInfos = buttonInfos;
    }

    @Override
    public void handleClick(final GuiConveyor conveyor, final int guiX, final int guiY,
            final int mouseX, final int mouseY) {
        conveyor.sendMessage(id, new NBTTagCompound());
        Minecraft.getMinecraft().getSoundHandler().playSound(
                PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public void drawGuiBackgroundLayer(final int guiX, final int guiY, final int mouseX,
            final int mouseY) {
        final StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null) {
            GlStateManager.color(1, 1, 1, 1);
            Minecraft.getMinecraft().getTextureManager().bindTexture(buttonInfo.getTexture());
            Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(guiX + getXPos(),
                    guiY + getYPos(), buttonInfo.getTextureX(), buttonInfo.getTextureY(),
                    getXSize(), getYSize());
        }
    }

    @Override
    public void drawGuiForegroundLayer(final int guiX, final int guiY, final int mouseX,
            final int mouseY) {
        final StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null && isInside(mouseX, mouseY)) {
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            Gui.drawRect(getXPos() - guiX, getYPos() - guiY, getXPos() + getXSize() - guiX,
                    getYPos() + getYSize() - guiY, -2130706433);
            GlStateManager.enableLighting();
            GlStateManager.disableDepth();
        }
    }

    @Nullable
    @Override
    public List<String> getTooltip(final int guiX, final int guiY, final int mouseX,
            final int mouseY) {
        final StateButtonInfo buttonInfo = getStateInfo();
        if (buttonInfo != null) {
            return Arrays.asList(buttonInfo.getTooltip());
        }
        return null;
    }

    public abstract int getState();

    private StateButtonInfo getStateInfo() {
        for (final StateButtonInfo buttonInfo : buttonInfos) {
            if (buttonInfo.getState() == getState()) {
                return buttonInfo;
            }
        }
        return null;
    }
}
