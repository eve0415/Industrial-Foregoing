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
package com.buuz135.industrial.proxy.client.entity;

import static com.buuz135.industrial.proxy.client.entity.RenderPinkSlime.NAMES;
import java.awt.Color;
import com.buuz135.industrial.entity.EntityPinkSlime;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerPinkGel implements LayerRenderer<EntityPinkSlime> {

    private final RenderPinkSlime slimeRenderer;
    private final ModelBase slimeModel = new ModelSlime(0);

    public LayerPinkGel(final RenderPinkSlime slimeRenderer) {
        this.slimeRenderer = slimeRenderer;
    }

    @Override
    public void doRenderLayer(final EntityPinkSlime entitylivingbaseIn, final float limbSwing,
            final float limbSwingAmount, final float partialTicks, final float ageInTicks,
            final float netHeadYaw, final float headPitch, final float scale) {
        if (!entitylivingbaseIn.isInvisible()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
            if (entitylivingbaseIn.hasCustomName() && NAMES.contains(
                    entitylivingbaseIn.getDisplayName().getUnformattedText().toLowerCase())) {
                final float speed = 360 * 0.2f;
                final int hsb = (int) (entitylivingbaseIn.world.getTotalWorldTime() % speed);
                final Color color = Color.getHSBColor(hsb / speed, 0.75f, 0.75f);
                GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f,
                        color.getBlue() / 255f, 1.0f);
            }
            this.slimeModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks,
                    netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
