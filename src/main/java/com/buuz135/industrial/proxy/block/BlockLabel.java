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
package com.buuz135.industrial.proxy.block;

import java.util.HashMap;
import java.util.UUID;
import javax.annotation.Nullable;
import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.proxy.block.tile.TileEntityLabel;
import com.buuz135.industrial.proxy.client.infopiece.IHasDisplayStack;
import com.buuz135.industrial.proxy.client.render.LabelTESR;
import com.buuz135.industrial.tile.misc.BlackHoleUnitTile;
import com.buuz135.industrial.utils.RecipeUtils;
import com.buuz135.industrial.utils.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockLabel extends BlockBase {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static AxisAlignedBB NORTH_AABB =
            new AxisAlignedBB(2 / 16D, 2 / 16D, 0, 14 / 16D, 14 / 16D, 1 / 16D);
    public static AxisAlignedBB SOUTH_AABB =
            new AxisAlignedBB(2 / 16D, 2 / 16D, 1, 14 / 16D, 14 / 16D, 1 - 1 / 16D);
    public static AxisAlignedBB EAST_AABB =
            new AxisAlignedBB(1, 2 / 16D, 2 / 16D, 1 - 1 / 16D, 14 / 16D, 14 / 16D);
    public static AxisAlignedBB WEST_AABB =
            new AxisAlignedBB(0, 2 / 16D, 2 / 16D, 1 / 16D, 14 / 16D, 14 / 16D);
    private final HashMap<UUID, Long> INTERACTION_LOGGER = new HashMap<>();

    public BlockLabel() {
        super("black_hole_label");
        this.setHardness(1.5F).setResistance(10.0F);
    }

    @Override
    public void createRecipe() {
        RecipeUtils.addShapedRecipe(new ItemStack(this, 4, 0), "ppp", "iri", "ppp", 'p',
                ItemRegistry.plastic, 'i', Items.PAPER, 'r', Items.REDSTONE);
    }

    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
            enumfacing = EnumFacing.NORTH;
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(final IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(FACING).build();
    }

    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public boolean isFullBlock(final IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
            case EAST:
                return EAST_AABB;
            default:
                break;
        }
        return NORTH_AABB;
    }

    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return true;
    }

    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos,
            final EnumFacing side) {
        return side.getAxis().isHorizontal() && worldIn
                .getTileEntity(pos.offset(side.getOpposite())) instanceof IHasDisplayStack;
    }

    @Override
    public void onNeighborChange(final IBlockAccess world, final BlockPos pos,
            final BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        if (world instanceof World && !((World) world).isRemote) {
            final EnumFacing facing = world.getBlockState(pos).getValue(FACING);
            if (!(world.getTileEntity(pos.offset(facing)) instanceof IHasDisplayStack)) {
                ((World) world).destroyBlock(pos, true);
            }
        }
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public boolean hasTileEntity(final IBlockState state) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state,
            final EntityLivingBase placer, final ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        final TileEntity tileEntity = this.createTileEntity(worldIn, state);
        worldIn.setTileEntity(pos, tileEntity);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(final World world, final IBlockState state) {
        final TileEntityLabel tile = new TileEntityLabel();
        tile.setWorld(world);
        return tile;
    }

    @Override
    public void registerBlock(final IForgeRegistry<Block> blocks) {
        super.registerBlock(blocks);
        GameRegistry.registerTileEntity(TileEntityLabel.class,
                new ResourceLocation(Reference.MOD_ID, this.getRegistryName().getPath() + "_tile"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRender() {
        super.registerRender();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLabel.class, new LabelTESR());
    }

    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos,
            final IBlockState state, final EntityPlayer playerIn, final EnumHand hand,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final TileEntity entity = worldIn.getTileEntity(pos.offset(state.getValue(FACING)));
        final TileEntity self = worldIn.getTileEntity(pos);
        if (!worldIn.isRemote && self instanceof TileEntityLabel
                && entity instanceof IHasDisplayStack
                && !((IHasDisplayStack) entity).getItemStack().hasCapability(
                        CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            if (playerIn.isSneaking()) {
                playerIn.playSound(SoundEvents.BLOCK_LEVER_CLICK, 1, 1);
                if (((TileEntityLabel) self).getFormatType() == TileEntityLabel.FormatType.STACKS)
                    ((TileEntityLabel) self).setFormatType(TileEntityLabel.FormatType.MILL);
                else
                    ((TileEntityLabel) self).setFormatType(TileEntityLabel.FormatType.STACKS);
            } else if (entity instanceof BlackHoleUnitTile && entity.hasCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
                final ItemStack stack = playerIn.getHeldItem(hand);
                if (!stack.isEmpty() && ((BlackHoleUnitTile) entity).canInsertItem(stack)) {
                    playerIn.setHeldItem(hand,
                            entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                                    EnumFacing.NORTH).insertItem(0, stack, false));
                } else if (System.currentTimeMillis() - INTERACTION_LOGGER
                        .getOrDefault(playerIn.getUniqueID(), System.currentTimeMillis()) < 300) {
                    final IItemHandler handler = entity.getCapability(
                            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
                    for (final ItemStack itemStack : playerIn.inventory.mainInventory) {
                        if (!itemStack.isEmpty()
                                && handler.insertItem(0, itemStack, true).isEmpty()) {
                            handler.insertItem(0, itemStack.copy(), false);
                            itemStack.setCount(0);
                        }
                    }
                }
                INTERACTION_LOGGER.put(playerIn.getUniqueID(), System.currentTimeMillis());
            }
        }
        return true;
    }

    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos,
            final EntityPlayer playerIn) {
        if (!worldIn.isRemote) {
            final TileEntity entity =
                    worldIn.getTileEntity(pos.offset(worldIn.getBlockState(pos).getValue(FACING)));
            final TileEntity self = worldIn.getTileEntity(pos);
            if (self instanceof TileEntityLabel && entity instanceof BlackHoleUnitTile
                    && ((BlackHoleUnitTile) entity).getDisplayAmount() > 0
                    && !((IHasDisplayStack) entity).getItemStack().hasCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                ItemHandlerHelper.giveItemToPlayer(playerIn,
                        entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                                EnumFacing.NORTH)
                                .extractItem(0, playerIn.isSneaking() ? 64 : 1, false));
            }
        }
        super.onBlockClicked(worldIn, pos, playerIn);
    }
}
