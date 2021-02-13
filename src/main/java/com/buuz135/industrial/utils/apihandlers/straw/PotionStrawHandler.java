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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import com.buuz135.industrial.utils.Triple;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

@ParametersAreNonnullByDefault
public class PotionStrawHandler extends StrawHandlerBase {
    private final List<Triple<Potion, Integer, Integer>> potions = new ArrayList<>();

    public PotionStrawHandler(final String fluidName) {
        super(fluidName);
    }

    public PotionStrawHandler(final Fluid fluid) {
        super(fluid.getName());
    }

    public PotionStrawHandler addPotion(final PotionEffect effect) {
        return addPotion(effect.getPotion(), effect.getDuration(), effect.getAmplifier());
    }

    public PotionStrawHandler addPotion(final Potion potion, final Integer duration,
            final Integer amplifier) {
        potions.add(new Triple<>(potion, duration, amplifier));
        return this;
    }

    @Override
    public void onDrink(final World world, final BlockPos pos, final FluidStack stack,
            final EntityPlayer player, final boolean fromFluidContainer) {
        for (final Triple<Potion, Integer, Integer> triple : potions) {
            player.addPotionEffect(new PotionEffect(triple.getA(), triple.getB(), triple.getC()));
        }
    }
}
