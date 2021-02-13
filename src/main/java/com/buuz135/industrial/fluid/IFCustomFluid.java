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
package com.buuz135.industrial.fluid;

import java.awt.Color;
import com.buuz135.industrial.utils.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class IFCustomFluid extends Fluid {

    public IFCustomFluid(final String fluidName, final int temperature) {
        super(fluidName, new ResourceLocation(Reference.MOD_ID, "blocks/fluids/base_still"),
                new ResourceLocation(Reference.MOD_ID, "blocks/fluids/base_flow"));
        this.setDensity(3000).setViscosity(6000).setTemperature(temperature);
    }

    public IFCustomFluid(final String fluidName, final int temperature,
            final ResourceLocation still, final ResourceLocation flow) {
        super(fluidName, still, flow);
        this.setDensity(3000).setViscosity(6000).setTemperature(temperature);
    }

    public IFCustomFluid(final String fluidName, final int temperature, final Color color) {
        this(fluidName, temperature);
        this.setColor(color);
    }

    public void register() {
        FluidRegistry.registerFluid(this);
        FluidRegistry.addBucketForFluid(this);
    }
}
