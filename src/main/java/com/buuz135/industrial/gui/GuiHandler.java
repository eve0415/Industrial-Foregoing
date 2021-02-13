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
package com.buuz135.industrial.gui;

import javax.annotation.Nullable;
import com.buuz135.industrial.book.GUIBookMain;
import com.buuz135.industrial.gui.conveyor.ContainerConveyor;
import com.buuz135.industrial.gui.conveyor.GuiConveyor;
import com.buuz135.industrial.proxy.block.BlockBase;
import com.buuz135.industrial.proxy.block.Cuboid;
import com.buuz135.industrial.proxy.block.tile.TileEntityConveyor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int BOOK = 0;
    public static final int CONVEYOR = 1;

    @Nullable
    @Override
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world,
            final int x, final int y, final int z) {
        if (ID == CONVEYOR) {
            final BlockPos pos = new BlockPos(x, y, z);
            final Block block = world.getBlockState(pos).getBlock();
            final TileEntity entity = world.getTileEntity(pos);
            if (block instanceof BlockBase && entity instanceof TileEntityConveyor) {
                final Cuboid hit = ((BlockBase) block).getCuboidHit(world, pos, player);
                if (hit != null) {
                    final EnumFacing facing = EnumFacing.byIndex(hit.identifier);
                    return new ContainerConveyor((TileEntityConveyor) entity, facing,
                            player.inventory);
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world,
            final int x, final int y, final int z) {
        if (ID == BOOK)
            return new GUIBookMain(world, x, y, z);
        if (ID == CONVEYOR) {
            return new GuiConveyor((Container) getServerGuiElement(ID, player, world, x, y, z));
        }
        return null;
    }
}
