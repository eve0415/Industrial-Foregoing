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
package com.buuz135.industrial.tile;

import java.util.List;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.items.IItemHandler;
import net.ndrei.teslacorelib.containers.BasicTeslaContainer;
import net.ndrei.teslacorelib.containers.FilteredSlot;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.gui.TiledRenderedGuiPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.ColoredItemHandler;

public class CustomColoredItemHandler extends ColoredItemHandler {

    private final int posX;
    private final int posY;
    private final int slotsX;
    private final int slotsY;

    public CustomColoredItemHandler(final IItemHandler handler, final EnumDyeColor color,
            final String name, final int posX, final int posY, final int slotsX, final int slotsY) {
        super(handler, color, name, new BoundingRectangle(posX, posY, 18 * slotsX, 18 * slotsY));
        this.posX = posX;
        this.posY = posY;
        this.slotsX = slotsX;
        this.slotsY = slotsY;
    }

    @Override
    public List<Slot> getSlots(final BasicTeslaContainer container) {
        final List<Slot> slots = super.getSlots(container);
        final BoundingRectangle box = this.getBoundingBox();
        int i = 0;
        for (int y = 0; y < slotsY; y++) {
            for (int x = 0; x < slotsX; x++) {
                slots.add(new FilteredSlot(this.getItemHandlerForContainer(), i,
                        box.getLeft() + 1 + x * 18, box.getTop() + 1 + y * 18));
                ++i;
            }
        }
        return slots;
    }

    @Override
    public List<IGuiContainerPiece> getGuiContainerPieces(final BasicTeslaGuiContainer container) {
        final List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);
        final BoundingRectangle box = this.getBoundingBox();
        pieces.add(new TiledRenderedGuiPiece(box.getLeft(), box.getTop(), 18, 18, slotsX, slotsY,
                BasicTeslaGuiContainer.Companion.getMACHINE_BACKGROUND(), 108, 225, getColor()));
        return pieces;
    }
}
