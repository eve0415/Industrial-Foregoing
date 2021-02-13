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
package com.buuz135.industrial.proxy.client.render;

import com.buuz135.industrial.proxy.client.ClientProxy;
import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.tile.world.LaserDrillTile;
import com.buuz135.industrial.utils.BlockUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.text.TextComponentTranslation;


public class LaserDrillSpecialRender<L extends CustomElectricMachine>
        extends TileEntitySpecialRenderer<LaserDrillTile> {


    @Override
    public void render(final LaserDrillTile tile, final double x, final double y, final double z,
            final float partialTicks, final int destroyStage, final float alpha) {
        if (tile.getLaserBasePos() == null) {
            drawNameplate(tile,
                    new TextComponentTranslation("text.industrialforegoing.display.no_laser_base")
                            .getUnformattedComponentText(),
                    x, y, z, 16);
            return;
        }
        bindTexture(ClientProxy.beacon);
        BlockUtils.renderLaserBeam(tile, x, y, z, tile.getFacing().getOpposite(), partialTicks, 1);
    }

    // TileEntity temp = tile.getWorld().getTileEntity(tile.getLaserBasePos()); FUTURE CODE
    // DISCARDED
    // if (temp == null || !(temp instanceof LaserBaseTile)) return;
    // LaserBaseTile te = (LaserBaseTile) temp;
    // if (Block.getBlockFromItem(te.getCreatingStack().getItem()) == Blocks.AIR) return;
    // GL11.glPushMatrix();
    // GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
    // GL11.glDepthMask(false);
    // GL11.glEnable(GL11.GL_BLEND);
    // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    // GL11.glDisable(GL11.GL_TEXTURE_2D);
    // GL11.glColorMask(true,true,true,true);
    // Color c = new Color(Color.HSBtoRGB(52 / 360F, 0.6F, 1F));
    // GlStateManager.color(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, 64);
    // GL11.glLineWidth(2F);
    // Tessellator tess = Tessellator.getInstance();
    // VertexBuffer buffer = tess.getBuffer();
    // buffer.setTranslation(x, y, z);
    // //RenderHelper.enableStandardItemLighting();
    // GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    // buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
    // Vector3d v2 = new
    // Vector3d(te.getPos().getX()-tile.getPos().getX(),0,te.getPos().getZ()-tile.getPos().getZ());
    // for (Vector3d v : tile.getPositions()) {
    // drawLine(buffer, v, v2, te);
    // }
    // tess.draw();
    // buffer.setTranslation(0, 0, 0);
    // GL11.glPopMatrix();
    // GL11.glDepthMask(true);
    // GL11.glPopAttrib();
}
