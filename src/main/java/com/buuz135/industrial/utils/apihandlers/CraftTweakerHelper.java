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
package com.buuz135.industrial.utils.apihandlers;

import com.buuz135.industrial.utils.apihandlers.crafttweaker.CTBioReactor;
import com.buuz135.industrial.utils.apihandlers.crafttweaker.CTExtractor;
import com.buuz135.industrial.utils.apihandlers.crafttweaker.CTFluidDictionary;
import com.buuz135.industrial.utils.apihandlers.crafttweaker.CTLaserDrill;
import com.buuz135.industrial.utils.apihandlers.crafttweaker.CTProteinReactor;
import com.buuz135.industrial.utils.apihandlers.crafttweaker.CTSludgeRefiner;
import crafttweaker.CraftTweakerAPI;

public class CraftTweakerHelper {

    public static void register() {
        CraftTweakerAPI.registerClass(CTBioReactor.class);
        CraftTweakerAPI.registerClass(CTLaserDrill.class);
        CraftTweakerAPI.registerClass(CTSludgeRefiner.class);
        CraftTweakerAPI.registerClass(CTProteinReactor.class);
        CraftTweakerAPI.registerClass(CTFluidDictionary.class);
        CraftTweakerAPI.registerClass(CTExtractor.class);
    }

    public CraftTweakerHelper() {
    }
}
