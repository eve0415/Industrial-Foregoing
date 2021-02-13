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
package com.buuz135.industrial.proxy;

import com.buuz135.industrial.api.conveyor.ConveyorUpgradeFactory;
import com.buuz135.industrial.proxy.block.upgrade.ConveyorBlinkingUpgrade;
import com.buuz135.industrial.proxy.block.upgrade.ConveyorBouncingUpgrade;
import com.buuz135.industrial.proxy.block.upgrade.ConveyorDetectorUpgrade;
import com.buuz135.industrial.proxy.block.upgrade.ConveyorDroppingUpgrade;
import com.buuz135.industrial.proxy.block.upgrade.ConveyorExtractionUpgrade;
import com.buuz135.industrial.proxy.block.upgrade.ConveyorInsertionUpgrade;
import com.buuz135.industrial.proxy.block.upgrade.ConveyorSplittingUpgrade;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;


public class ConveyorRegistry {

    @SubscribeEvent
    public void register(final RegistryEvent.Register<ConveyorUpgradeFactory> event) {
        final IForgeRegistry<ConveyorUpgradeFactory> registry = event.getRegistry();
        registry.register(new ConveyorExtractionUpgrade.Factory());
        registry.register(new ConveyorInsertionUpgrade.Factory());
        registry.register(new ConveyorDetectorUpgrade.Factory());
        registry.register(new ConveyorBouncingUpgrade.Factory());
        registry.register(new ConveyorDroppingUpgrade.Factory());
        registry.register(new ConveyorBlinkingUpgrade.Factory());
        registry.register(new ConveyorSplittingUpgrade.Factory());
    }
}
