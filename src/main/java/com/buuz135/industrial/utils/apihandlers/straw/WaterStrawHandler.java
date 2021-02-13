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
package com.buuz135.industrial.utils.apihandlers.straw;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

@ParametersAreNonnullByDefault
public class WaterStrawHandler extends StrawHandlerBase {

    public WaterStrawHandler() {
        super("water");
        setRegistryName("water");
    }

    @Override
    public void onDrink(final World world, final BlockPos pos, final FluidStack stack,
            final EntityPlayer player, final boolean fromFluidContainer) {
        player.extinguish();
        final NBTTagCompound tag = player.getEntityData();
        if (tag.hasKey("lavaDrink") && world.getTotalWorldTime() - tag.getLong("lavaDrink") < 120) { // 6
                                                                                                     // Seconds
                                                                                                     // to
                                                                                                     // drink
                                                                                                     // water
                                                                                                     // after
                                                                                                     // drinking
                                                                                                     // lava
                                                                                                     // to
                                                                                                     // create
                                                                                                     // obsidian
            player.entityDropItem(new ItemStack(Blocks.OBSIDIAN), player.getEyeHeight());

            tag.setLong("lavaDrink", 0);
            world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.5F,
                    world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

}
