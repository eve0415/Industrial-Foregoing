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
package com.buuz135.industrial.gui.conveyor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.buuz135.industrial.IndustrialForegoing;
import com.buuz135.industrial.api.conveyor.ConveyorUpgrade;
import com.buuz135.industrial.api.conveyor.gui.IGuiComponent;
import com.buuz135.industrial.gui.component.FilterGuiComponent;
import com.buuz135.industrial.proxy.block.filter.IFilter;
import com.buuz135.industrial.proxy.network.ConveyorButtonInteractMessage;
import com.buuz135.industrial.utils.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiConveyor extends GuiContainer {

    public static final ResourceLocation BG_TEXTURE =
            new ResourceLocation(Reference.MOD_ID, "textures/gui/conveyor.png");

    private final ConveyorUpgrade upgrade;
    private final List<IGuiComponent> componentList;
    private int x;
    private int y;
    private final List<IFilter.GhostSlot> ghostSlots;

    public GuiConveyor(final Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.upgrade = getContainer().getConveyor().getUpgradeMap().get(getContainer().getFacing());
        this.componentList = new ArrayList<>();
        this.ghostSlots = new ArrayList<>();
    }

    @Override
    public void initGui() {
        super.initGui();
        componentList.clear();
        upgrade.addComponentsToGui(componentList);
        for (final IGuiComponent iGuiComponent : componentList) {
            if (iGuiComponent instanceof FilterGuiComponent) {
                ghostSlots.addAll(Arrays
                        .asList(((FilterGuiComponent) iGuiComponent).getFilter().getFilter()));
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX,
            final int mouseY) {
        this.drawDefaultBackground();
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (upgrade != null) {
            final String localized =
                    new TextComponentTranslation(String.format("conveyor.upgrade.%s.%s.name",
                            upgrade.getFactory().getRegistryName().getNamespace(),
                            upgrade.getFactory().getRegistryName().getPath())).getFormattedText();
            fontRenderer.drawString(localized,
                    x + xSize / 2 - fontRenderer.getStringWidth(localized) / 2, y + 6, 0x404040);
        }
        for (final IGuiComponent iGuiComponent : componentList) {
            iGuiComponent.drawGuiBackgroundLayer(x, y, mouseX, mouseY);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        for (final IGuiComponent iGuiComponent : componentList) {
            iGuiComponent.drawGuiForegroundLayer(x, y, mouseX, mouseY);
        }
        renderHoveredToolTip(mouseX - x, mouseY - y);
        for (final IGuiComponent iGuiComponent : componentList) {
            if (iGuiComponent.isInside(mouseX - x, mouseY - y)) {
                final List<String> tooltips = iGuiComponent.getTooltip(x, y, mouseX, mouseY);
                if (tooltips != null)
                    drawHoveringText(tooltips, mouseX - x, mouseY - y);
            }
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private ContainerConveyor getContainer() {
        return (ContainerConveyor) this.inventorySlots;
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton)
            throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (final IGuiComponent iGuiComponent : componentList) {
            if (iGuiComponent.isInside(mouseX - x, mouseY - y))
                iGuiComponent.handleClick(this, x, y, mouseX, mouseY);
        }
    }

    public void sendMessage(final int id, final NBTTagCompound compound) {
        IndustrialForegoing.NETWORK.sendToServer(new ConveyorButtonInteractMessage(upgrade.getPos(),
                id, upgrade.getSide(), compound));
    }

    public List<IFilter.GhostSlot> getGhostSlots() {
        return ghostSlots;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
