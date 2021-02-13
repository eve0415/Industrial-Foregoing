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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import com.buuz135.industrial.IndustrialForegoing;
import com.buuz135.industrial.api.conveyor.ConveyorUpgrade;
import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.proxy.block.tile.TileEntityConveyor;
import com.buuz135.industrial.proxy.client.model.ConveyorModelData;
import com.buuz135.industrial.proxy.client.render.FluidConveyorTESR;
import com.buuz135.industrial.registry.IFRegistries;
import com.buuz135.industrial.utils.RayTraceUtils;
import com.buuz135.industrial.utils.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockConveyor extends BlockBase {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
    public static final PropertyEnum<EnumSides> SIDES =
            PropertyEnum.create("sides", EnumSides.class);
    private static String[] dyes =
            {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray",
                    "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};
    private final ConveyorItem item;

    public BlockConveyor() {
        super("conveyor");
        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.NORTH)
                .withProperty(SIDES, EnumSides.NONE));
        this.item = new ConveyorItem(this);
        this.setCreativeTab(IndustrialForegoing.creativeTab);
        this.setHardness(2);
    }

    @Override
    public void registerItem(final IForgeRegistry<Item> items) {
        items.register(item);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRender() {
        for (int i = 0; i < EnumDyeColor.values().length; ++i) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i,
                    new ModelResourceLocation(this.getRegistryName(), "inventory"));
        }
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConveyor.class,
                new FluidConveyorTESR());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void createRecipe() {
        RecipeUtils.addShapedRecipe(new ItemStack(this, 4, 0), "ppp", "iri", "ppp", 'p',
                ItemRegistry.plastic, 'i', "ingotIron", 'r', Items.REDSTONE);
        for (int i = 0; i < dyes.length; i++) {
            RecipeUtils.addShapedRecipe(new ItemStack(this, 8, 15 - i), "_" + dyes[i].toLowerCase(),
                    new HashMap<>(), "ccc", "cdc", "ccc", 'c',
                    new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE), 'd', "dye" + dyes[i]);
        }
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

    @Override
    public int getWeakPower(final IBlockState blockState, final IBlockAccess world,
            final BlockPos pos, final EnumFacing side) {
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityConveyor) {
            return ((TileEntityConveyor) tileEntity).getPower();
        }
        return blockState.getWeakPower(world, pos, side);
    }

    @Override
    public int getStrongPower(final IBlockState blockState, final IBlockAccess world,
            final BlockPos pos, final EnumFacing side) {
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityConveyor) {
            return side == EnumFacing.UP ? ((TileEntityConveyor) tileEntity).getPower() : 0;
        }
        return blockState.getStrongPower(world, pos, side);
    }

    @Override
    public ItemStack getPickBlock(final IBlockState state, final RayTraceResult target,
            final World world, final BlockPos pos, final EntityPlayer player) {
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityConveyor) {
            if (target instanceof DistanceRayTraceResult) {
                final ConveyorUpgrade upgrade = ((TileEntityConveyor) tileEntity).getUpgradeMap()
                        .get(EnumFacing.byIndex(((Cuboid) target.hitInfo).identifier));
                if (upgrade != null) {
                    return new ItemStack(ItemRegistry.conveyorUpgradeItem, 1,
                            IFRegistries.CONVEYOR_UPGRADE_REGISTRY.getID(upgrade.getFactory()) - 1);
                }
            }
            return new ItemStack(this, 1, 15 - ((TileEntityConveyor) tileEntity).getColor());
        }
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn,
            final BlockPos pos) {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityConveyor) {
            state = state.withProperty(FACING, ((TileEntityConveyor) tileEntity).getFacing())
                    .withProperty(TYPE, ((TileEntityConveyor) tileEntity).getType());
        }
        if (state.getValue(TYPE).equals(EnumType.FLAT)
                || state.getValue(TYPE).equals(EnumType.FLAT_FAST)) {
            final EnumFacing right = state.getValue(FACING).rotateY();
            final EnumFacing left = state.getValue(FACING).rotateYCCW();
            if (isConveyorAndFacing(pos.offset(right), worldIn, left)
                    && isConveyorAndFacing(pos.offset(left), worldIn, right)
                    || (isConveyorAndFacing(pos.offset(right).down(), worldIn, left)
                            && isConveyorAndFacing(pos.offset(left).down(), worldIn, right))) {
                state = state.withProperty(SIDES, EnumSides.BOTH);
            } else if (isConveyorAndFacing(pos.offset(right), worldIn, left)
                    || isConveyorAndFacing(pos.offset(right).down(), worldIn, left)) {
                state = state.withProperty(SIDES, EnumSides.RIGHT);
            } else if (isConveyorAndFacing(pos.offset(left), worldIn, right)
                    || isConveyorAndFacing(pos.offset(left).down(), worldIn, right)) {
                state = state.withProperty(SIDES, EnumSides.LEFT);
            } else {
                state = state.withProperty(SIDES, EnumSides.NONE);
            }
        }
        return state;
    }

    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn,
            final BlockPos pos, final AxisAlignedBB entityBox,
            final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn,
            final boolean isActualState) {
        final TileEntity entity = worldIn.getTileEntity(pos);
        if (entity instanceof TileEntityConveyor) {
            ((TileEntityConveyor) entity).getUpgradeMap().values().forEach(upgrade -> {
                if (upgrade.getBoundingBox().collidable)
                    state.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn,
                            isActualState);
            });
            if (!((TileEntityConveyor) entity).getType().isVertical()) {
                state.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn,
                        isActualState);
                return;
            }
            ((TileEntityConveyor) entity).getCollisionBoxes()
                    .forEach(axisAlignedBB -> state.addCollisionBoxToList(worldIn, pos, entityBox,
                            collidingBoxes, entityIn, isActualState));
        }
    }

    private boolean isConveyorAndFacing(final BlockPos pos, final IBlockAccess world,
            final EnumFacing toFace) {
        return world.getBlockState(pos).getBlock() instanceof BlockConveyor
                && (toFace == null || world.getBlockState(pos).getValue(FACING).equals(toFace));
    }

    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (int i = 0; i < EnumDyeColor.values().length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public IBlockState getExtendedState(final IBlockState state, final IBlockAccess world,
            final BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            final TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityConveyor)
                return ((IExtendedBlockState) state).withProperty(
                        ConveyorModelData.UPGRADE_PROPERTY,
                        new ConveyorModelData(((TileEntityConveyor) tile).getUpgradeMap()));
        }
        return super.getExtendedState(state, world, pos);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state,
            final BlockPos pos, final EnumFacing face) {
        return state.getValue(TYPE).isVertical() ? BlockFaceShape.UNDEFINED
                : face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return !worldIn.getBlockState(pos.down()).getBlock().equals(this);
    }

    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos,
            final Block blockIn, final BlockPos fromPos) {
        state.neighborChanged(worldIn, pos, blockIn, fromPos);
        // if (!worldIn.isRemote && !canPlaceBlockAt(worldIn, pos)) {
        // worldIn.destroyBlock(pos, false);
        // }
    }

    @Override
    public List<Cuboid> getBoundingBoxes(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        final List<Cuboid> cuboids = new ArrayList<>();
        cuboids.add(new Cuboid(getBoundingBox(state, source, pos)));
        final TileEntity tile = source.getTileEntity(pos);
        if (tile instanceof TileEntityConveyor)
            for (final ConveyorUpgrade upgrade : ((TileEntityConveyor) tile).getUpgradeMap()
                    .values())
                if (upgrade != null)
                    cuboids.add(upgrade.getBoundingBox());
        return cuboids;
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(final IBlockState blockState, final World worldIn,
            final BlockPos pos, final Vec3d start, final Vec3d end) {
        return RayTraceUtils.rayTraceBoxesClosest(start, end, pos,
                getBoundingBoxes(blockState, worldIn, pos));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(FACING, SIDES, TYPE)
                .add(ConveyorModelData.UPGRADE_PROPERTY).build();
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
        final EnumFacing direction = placer.getHorizontalFacing();
        ((TileEntityConveyor) tileEntity).setFacing(direction);
        ((TileEntityConveyor) tileEntity).setColor(EnumDyeColor.values()[stack.getMetadata()]);
        updateConveyorPlacing(worldIn, pos, state, true);
    }

    @Override
    public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
        final NonNullList<ItemStack> list = NonNullList.create();
        getDrops(list, world, pos, state, 0);
        for (final ItemStack stack : list) {
            final float f = 0.7F;
            final float d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            final float d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            final float d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            final EntityItem entityitem =
                    new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, stack);
            world.spawnEntity(entityitem);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world,
            final BlockPos pos, final IBlockState state, final int fortune) {
        final TileEntity entity = world.getTileEntity(pos);
        if (entity instanceof TileEntityConveyor) {
            drops.add(new ItemStack(this, 1, EnumDyeColor
                    .byDyeDamage(((TileEntityConveyor) entity).getColor()).getMetadata()));
            for (final ConveyorUpgrade upgrade : ((TileEntityConveyor) entity).getUpgradeMap()
                    .values()) {
                drops.addAll(upgrade.getDrops());
            }
            if (((TileEntityConveyor) entity).getType().isFast()) {
                drops.add(new ItemStack(Items.GLOWSTONE_DUST, 1));
            }
            if (((TileEntityConveyor) entity).isSticky()) {
                drops.add(new ItemStack(ItemRegistry.plastic, 1));
            }
        }
    }

    private void updateConveyorPlacing(final World worldIn, final BlockPos pos,
            final IBlockState state, final boolean first) {
        final TileEntity entity = worldIn.getTileEntity(pos);
        if (entity instanceof TileEntityConveyor) {
            final EnumFacing direction = ((TileEntityConveyor) entity).getFacing();
            final EnumFacing right = state.getValue(FACING).rotateY();
            final EnumFacing left = state.getValue(FACING).rotateYCCW();
            if (((TileEntityConveyor) entity).getUpgradeMap().isEmpty()) {
                if (isConveyorAndFacing(pos.up().offset(direction), worldIn, null)) {// SELF UP
                    ((TileEntityConveyor) entity).setType(
                            ((TileEntityConveyor) entity).getType().getVertical(EnumFacing.UP));
                } else if (isConveyorAndFacing(pos.up().offset(direction.getOpposite()), worldIn,
                        null)) { // SELF DOWN
                    ((TileEntityConveyor) entity).setType(
                            ((TileEntityConveyor) entity).getType().getVertical(EnumFacing.DOWN));
                }
            }
            // UPDATE SURROUNDINGS
            if (!first)
                return;
            if (isConveyorAndFacing(pos.offset(direction.getOpposite()).down(), worldIn,
                    direction)) { // BACK DOWN
                updateConveyorPlacing(worldIn, pos.offset(direction.getOpposite()).down(), state,
                        false);
            }
            if (isConveyorAndFacing(pos.offset(left).down(), worldIn, right)) { // LEFT DOWN
                updateConveyorPlacing(worldIn, pos.offset(left).down(), state, false);
            }
            if (isConveyorAndFacing(pos.offset(right).down(), worldIn, left)) { // RIGHT DOWN
                updateConveyorPlacing(worldIn, pos.offset(right).down(), state, false);
            }
            if (isConveyorAndFacing(pos.offset(direction).down(), worldIn, direction)) { // FRONT
                                                                                         // DOWN
                updateConveyorPlacing(worldIn, pos.offset(direction).down(), state, false);
            }
            worldIn.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos,
            final IBlockState state, final EntityPlayer playerIn, final EnumHand hand,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        final ItemStack handStack = playerIn.getHeldItem(hand);
        if (tileEntity instanceof TileEntityConveyor) {
            if (playerIn.isSneaking()) {
                final Cuboid hit = getCuboidHit(worldIn, pos, playerIn);
                if (hit != null) {
                    final EnumFacing upgradeFacing = EnumFacing.byIndex(hit.identifier);
                    ((TileEntityConveyor) tileEntity).removeUpgrade(upgradeFacing, true);
                    return true;
                }
                return false;
            } else {
                final Cuboid hit = getCuboidHit(worldIn, pos, playerIn);
                if (hit != null) {
                    if (hit.identifier == -1) {
                        if (handStack.getItem().equals(Items.GLOWSTONE_DUST)
                                && !((TileEntityConveyor) tileEntity).getType().isFast()) {
                            ((TileEntityConveyor) tileEntity)
                                    .setType(((TileEntityConveyor) tileEntity).getType().getFast());
                            handStack.shrink(1);
                            return true;
                        }
                        if (handStack.getItem().equals(ItemRegistry.plastic)
                                && !((TileEntityConveyor) tileEntity).isSticky()) {
                            ((TileEntityConveyor) tileEntity).setSticky(true);
                            handStack.shrink(1);
                            return true;
                        }
                    } else {
                        final EnumFacing upgradeFacing = EnumFacing.byIndex(hit.identifier);
                        if (((TileEntityConveyor) tileEntity).hasUpgrade(upgradeFacing)) {
                            final ConveyorUpgrade upgrade = ((TileEntityConveyor) tileEntity)
                                    .getUpgradeMap().get(upgradeFacing);
                            if (upgrade.onUpgradeActivated(playerIn, hand)) {
                                return true;
                            } else if (upgrade.hasGui()) {
                                playerIn.openGui(IndustrialForegoing.instance, 1, worldIn,
                                        pos.getX(), pos.getY(), pos.getZ());
                                return true;
                            }
                        }
                    }
                    return false;
                }
                return false;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY,
                hitZ);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(final World world, final IBlockState state) {
        final TileEntityConveyor tile = new TileEntityConveyor();
        tile.setWorld(world);
        return tile;
    }

    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public void registerBlock(final IForgeRegistry<Block> blocks) {
        super.registerBlock(blocks);
        GameRegistry.registerTileEntity(TileEntityConveyor.class, "conveyor_tile");
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        final TileEntity entity = source.getTileEntity(pos);
        if (entity instanceof TileEntityConveyor) {
            if (((TileEntityConveyor) entity).getType().isVertical()) {
                return new AxisAlignedBB(0, 0, 0, 1, 0.40, 1);
            }
            return new AxisAlignedBB(0, 0, 0, 1, 1 / 16D, 1);
        }
        return FULL_BLOCK_AABB;
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

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state,
            final Entity entityIn) {
        super.onEntityCollision(worldIn, pos, state, entityIn);
        final TileEntity entity = worldIn.getTileEntity(pos);
        if (entity instanceof TileEntityConveyor) {
            ((TileEntityConveyor) entity).handleEntityMovement(entityIn);
        }
    }

    @Override
    public boolean canCreatureSpawn(final IBlockState state, final IBlockAccess world,
            final BlockPos pos, final EntityLiving.SpawnPlacementType type) {
        return true;
    }

    @Override
    public boolean canSpawnInBlock() {
        return true;
    }

    @Override
    public boolean canEntitySpawn(final IBlockState state, final Entity entityIn) {
        return true;
    }

    public ConveyorItem getItem() {
        return item;
    }

    public enum EnumType implements IStringSerializable {


        FLAT(false), UP(false), DOWN(false), FLAT_FAST(true), UP_FAST(true), DOWN_FAST(true);

        private boolean fast;

        EnumType(final boolean fast) {
            this.fast = fast;
        }

        public static EnumType getFromName(final String name) {
            for (final EnumType type : EnumType.values()) {
                if (type.getName().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return FLAT;
        }

        public boolean isFast() {
            return fast;
        }

        public EnumType getFast() {
            switch (this) {
                case FLAT:
                    return FLAT_FAST;
                case UP:
                    return UP_FAST;
                case DOWN:
                    return DOWN_FAST;
                default:
                    return this;
            }
        }

        public EnumType getVertical(final EnumFacing facing) {
            if (this.isFast()) {
                if (facing == EnumFacing.UP) {
                    return UP_FAST;
                }
                if (facing == EnumFacing.DOWN) {
                    return DOWN_FAST;
                }
                return FLAT_FAST;
            } else {
                if (facing == EnumFacing.UP) {
                    return UP;
                }
                if (facing == EnumFacing.DOWN) {
                    return DOWN;
                }
                return FLAT_FAST;
            }
        }

        public boolean isVertical() {
            return isDown() || isUp();
        }

        public boolean isUp() {
            return this.equals(UP) || this.equals(UP_FAST);
        }

        public boolean isDown() {
            return this.equals(DOWN) || this.equals(DOWN_FAST);
        }

        @Override
        public String getName() {
            return this.toString().toLowerCase();
        }

    }

    public enum EnumSides implements IStringSerializable {
        NONE, LEFT, RIGHT, BOTH;

        @Override
        public String getName() {
            return this.toString().toLowerCase();
        }
    }

    private class ConveyorItem extends ItemBlock {

        public ConveyorItem(final Block block) {
            super(block);
            this.setRegistryName(block.getRegistryName());
            this.setHasSubtypes(true);
            this.setCreativeTab(IndustrialForegoing.creativeTab);
        }

        @Override
        public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
            if (this.isInCreativeTab(tab)) {
                for (int i = 0; i < EnumDyeColor.values().length; ++i) {
                    items.add(new ItemStack(this, 1, i));
                }
            }
        }

        @Override
        public String getItemStackDisplayName(final ItemStack stack) {
            return new TextComponentTranslation("item.fireworksCharge." + EnumDyeColor
                    .byMetadata(stack.getMetadata()).getTranslationKey().replaceAll("_", ""))
                            .getFormattedText()
                    + " " + super.getItemStackDisplayName(stack);
        }
    }

}
