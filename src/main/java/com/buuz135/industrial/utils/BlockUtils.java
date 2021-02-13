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
package com.buuz135.industrial.utils;

import java.util.ArrayList;
import java.util.List;
import com.buuz135.industrial.IndustrialForegoing;
import com.google.common.collect.HashMultimap;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;

public class BlockUtils {

    private static HashMultimap<String, Block> oreDictBlocks = HashMultimap.create();

    public static List<BlockPos> getBlockPosInAABB(final AxisAlignedBB axisAlignedBB) {
        final List<BlockPos> blocks = new ArrayList<BlockPos>();
        for (double y = axisAlignedBB.minY; y < axisAlignedBB.maxY; ++y) {
            for (double x = axisAlignedBB.minX; x < axisAlignedBB.maxX; ++x) {
                for (double z = axisAlignedBB.minZ; z < axisAlignedBB.maxZ; ++z) {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static boolean isBlockOreDict(final World world, final BlockPos pos, final String ore) {
        final IBlockState state = world.getBlockState(pos);
        final Block block = state.getBlock();
        if (oreDictBlocks.containsEntry(ore, block)) {
            return true;
        }
        final Item item = Item.getItemFromBlock(block);
        if (!item.equals(Items.AIR)) {
            final ItemStack stack = new ItemStack(item);
            final int id = OreDictionary.getOreID(ore);
            for (final int i : OreDictionary.getOreIDs(stack)) {
                if (i == id) {
                    oreDictBlocks.put(ore, block);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isLog(final World world, final BlockPos pos) {
        return (world.getBlockState(pos).getBlock().isWood(world, pos)
                || isBlockOreDict(world, pos, "blockSlimeCongealed"));
    }

    public static boolean isLeaves(final World world, final BlockPos pos) {
        return world.getBlockState(pos).getBlock().isLeaves(world.getBlockState(pos), world, pos);
    }

    public static boolean isChorus(final World world, final BlockPos pos) {
        return world.getBlockState(pos).getBlock().equals(Blocks.CHORUS_PLANT)
                || world.getBlockState(pos).getBlock().equals(Blocks.CHORUS_FLOWER);
    }

    public static boolean canBlockBeBroken(final World world, final BlockPos pos) {
        if (world.isAirBlock(pos))
            return false;
        final BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos,
                world.getBlockState(pos), IndustrialForegoing.getFakePlayer(world));
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    public static List<ItemStack> getBlockDrops(final World world, final BlockPos pos) {
        return getBlockDrops(world, pos, 0);
    }

    public static List<ItemStack> getBlockDrops(final World world, final BlockPos pos,
            final int fortune) {
        final IBlockState state = world.getBlockState(pos);
        final NonNullList<ItemStack> stacks = NonNullList.create();
        state.getBlock().getDrops(stacks, world, pos, state, fortune);
        final BlockEvent.HarvestDropsEvent event =
                new BlockEvent.HarvestDropsEvent(world, pos, world.getBlockState(pos), 0, 1f,
                        stacks, IndustrialForegoing.getFakePlayer(world), false);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getDrops();
    }

    public static void renderLaserBeam(final TileEntity tile, final double x, final double y,
            final double z, final EnumFacing direction, final float partialTicks,
            final int length) {
        final Tessellator tess = Tessellator.getInstance();
        GlStateManager.pushMatrix();
        double tempX = x;
        double tempY = y;
        double tempZ = z;
        switch (direction) {
            case NORTH:
                GlStateManager.rotate(270, 1, 0, 0);
                tempY = -z;
                tempZ = y;
                break;
            case SOUTH:
                GlStateManager.rotate(90, 1, 0, 0);
                tempY = z + 1;
                tempZ = -y - 1;
                break;
            case EAST:
                GlStateManager.rotate(270, 0, 0, 1);
                tempY = x + 1;
                tempX = -y - 1;
                break;
            case WEST:
                GlStateManager.rotate(90, 0, 0, 1);
                tempY = -x;
                tempX = y;
                break;
            default:
                tempY -= length;

        }
        RenderHelper.disableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        final BufferBuilder buffer = tess.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        final double d1 = -(tile.getWorld().getWorldTime() % 15) / 15D;
        final double d2 = (tile.getWorld().getWorldTime() % 40) / 2D;
        double pointA = 0.45 - d2 / 200D;
        if (d2 >= 10) {
            pointA = 0.35 + d2 / 200D;
        }
        final double pointB = 1 - pointA;
        final double uStart = 0.0D;
        final double uEnd = 1.0D;
        final double vStart = -1.0F + d1;
        final double vEnd = (length * 2) + vStart;
        buffer.pos(tempX + pointA, tempY + length, tempZ + pointA).tex(uEnd, vEnd).endVertex();
        buffer.pos(tempX + pointA, tempY, tempZ + pointA).tex(uEnd, vStart).endVertex();
        buffer.pos(tempX + pointB, tempY, tempZ + pointA).tex(uStart, vStart).endVertex();
        buffer.pos(tempX + pointB, tempY + length, tempZ + pointA).tex(uStart, vEnd).endVertex();
        buffer.pos(tempX + pointB, tempY + length, tempZ + pointB).tex(uEnd, vEnd).endVertex();
        buffer.pos(tempX + pointB, tempY, tempZ + pointB).tex(uEnd, vStart).endVertex();
        buffer.pos(tempX + pointA, tempY, tempZ + pointB).tex(uStart, vStart).endVertex();
        buffer.pos(tempX + pointA, tempY + length, tempZ + pointB).tex(uStart, vEnd).endVertex();
        buffer.pos(tempX + pointB, tempY + length, tempZ + pointA).tex(uEnd, vEnd).endVertex();
        buffer.pos(tempX + pointB, tempY, tempZ + pointA).tex(uEnd, vStart).endVertex();
        buffer.pos(tempX + pointB, tempY, tempZ + pointB).tex(uStart, vStart).endVertex();
        buffer.pos(tempX + pointB, tempY + length, tempZ + pointB).tex(uStart, vEnd).endVertex();
        buffer.pos(tempX + pointA, tempY + length, tempZ + pointB).tex(uEnd, vEnd).endVertex();
        buffer.pos(tempX + pointA, tempY, tempZ + pointB).tex(uEnd, vStart).endVertex();
        buffer.pos(tempX + pointA, tempY, tempZ + pointA).tex(uStart, vStart).endVertex();
        buffer.pos(tempX + pointA, tempY + length, tempZ + pointA).tex(uStart, vEnd).endVertex();
        tess.draw();
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL11.GL_CULL_FACE);
        // GL11.glEnable(GL11.GL_BLEND);

        GlStateManager.popMatrix();
    }
}
