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
package com.buuz135.industrial.tile.block;

import java.util.List;
import com.buuz135.industrial.api.book.IPage;
import com.buuz135.industrial.book.BookCategory;
import com.buuz135.industrial.config.CustomConfiguration;
import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.tile.world.LaserBaseTile;
import com.buuz135.industrial.utils.RecipeUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.ndrei.teslacorelib.items.MachineCaseItem;

public class LaserBaseBlock extends CustomOrientedBlock<LaserBaseTile> {

    private int workNeeded;
    private int lenseChanceIncrease;

    public LaserBaseBlock() {
        super("laser_base", LaserBaseTile.class, Material.ROCK, 0, 0);
    }


    @Override
    public void getMachineConfig() {
        super.getMachineConfig();
        workNeeded = CustomConfiguration.config.getInt("workNeeded",
                "machines" + Configuration.CATEGORY_SPLITTER
                        + this.getRegistryName().getPath().toString(),
                100, 1, Integer.MAX_VALUE, "Amount of work needed to produce an ore");
        lenseChanceIncrease = CustomConfiguration.config.getInt("lenseChanceIncrease",
                "machines" + Configuration.CATEGORY_SPLITTER
                        + this.getRegistryName().getPath().toString(),
                5, 1, Integer.MAX_VALUE, "How much weight each lense increases to the ore");
    }

    public int getWorkNeeded() {
        return workNeeded;
    }

    public int getLenseChanceIncrease() {
        return lenseChanceIncrease;
    }

    public void createRecipe() {
        RecipeUtils.addShapedRecipe(new ItemStack(this), "pwp", "gwg", "dmd", 'p',
                ItemRegistry.plastic, 'w', Blocks.GLOWSTONE, 'g', "gearGold", 'd', "gearDiamond",
                'm', MachineCaseItem.INSTANCE);
    }

    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity entity = worldIn.getTileEntity(pos);
        if (entity != null && entity instanceof LaserBaseTile) {
            ((LaserBaseTile) entity).onBlockBroken();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public BookCategory getCategory() {
        return BookCategory.RESOURCE_PRODUCTION;
    }

    @Override
    public List<IPage> getBookDescriptionPages() {
        final List<IPage> pages = super.getBookDescriptionPages();
        return pages;
    }

}
