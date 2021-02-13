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
package com.buuz135.industrial.tile;

import java.util.Collections;
import java.util.List;
import com.buuz135.industrial.item.addon.RangeAddonItem;
import com.buuz135.industrial.proxy.CommonProxy;
import com.buuz135.industrial.proxy.client.ClientProxy;
import com.buuz135.industrial.utils.WorkUtils;
import org.jetbrains.annotations.NotNull;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.gui.ToggleButtonPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.render.IWorkAreaProvider;
import net.ndrei.teslacorelib.render.WorkingAreaRenderer;
import net.ndrei.teslacorelib.utils.BlockCube;

public abstract class WorkingAreaElectricMachine extends CustomElectricMachine
        implements IWorkAreaProvider {

    private final int color;
    private boolean showArea;

    protected WorkingAreaElectricMachine(final int typeId) {
        super(typeId);
        color = CommonProxy.random.nextInt();
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
    }

    @Override
    public List<IGuiContainerPiece> getGuiContainerPieces(final BasicTeslaGuiContainer container) {
        final List<IGuiContainerPiece> list = super.getGuiContainerPieces(container);
        list.add(new ToggleButtonPiece(136, 84, 13, 13, 0) {
            @Override
            protected int getCurrentState() {
                return showArea ? 1 : 0;
            }

            @Override
            protected void renderState(final BasicTeslaGuiContainer container, final int state,
                    final BoundingRectangle box) {

            }

            @Override
            public void drawBackgroundLayer(final BasicTeslaGuiContainer container, final int guiX,
                    final int guiY, final float partialTicks, final int mouseX, final int mouseY) {
                super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);
                if (getCurrentState() == 0) {
                    container.mc.getTextureManager().bindTexture(ClientProxy.GUI);
                    container.drawTexturedRect(this.getLeft() - 1, this.getTop() - 1, 78, 1, 16,
                            16);
                } else {
                    container.mc.getTextureManager().bindTexture(ClientProxy.GUI);
                    container.drawTexturedRect(this.getLeft() - 1, this.getTop() - 1, 78, 17, 16,
                            16);
                }
            }

            @Override
            protected void clicked() {
                showArea = !showArea;
            }


            @NotNull
            @Override
            protected List<String> getStateToolTip(final int state) {
                return Collections.singletonList(state == 0
                        ? new TextComponentTranslation("text.industrialforegoing.button.show_area")
                                .getFormattedText()
                        : new TextComponentTranslation("text.industrialforegoing.button.hide_area")
                                .getFormattedText());
            }

        });

        return list;
    }

    public abstract float work();

    @Override
    protected float performWork() {
        float work = 0;
        for (int i = 0; i < getActionsWork(); ++i) {
            final float temp = work();
            if (temp < 0)
                return 1;
            if (temp > work) {
                work = temp;
            }
        }
        return work;
    }

    public BlockCube getWorkArea() {
        return new BlockCube(
                new BlockPos(getWorkingArea().minX, getWorkingArea().minY, getWorkingArea().minZ),
                new BlockPos(getWorkingArea().maxX - 1, getWorkingArea().maxY - 1,
                        getWorkingArea().maxZ - 1));
    }

    @SideOnly(Side.CLIENT)
    public int getWorkAreaColor() {
        return color;
    }

    public int getRadius() {
        return Math.min(WorkUtils.getMachineWidth(world, pos) + 1,
                (this.hasAddon(RangeAddonItem.class)
                        ? this.getAddonStack(RangeAddonItem.class).getMetadata() + 1
                        : 0));
    }

    public int getHeight() {
        return WorkUtils.getMachineHeight(world, pos);
    }

    public boolean canAcceptRangeUpgrades() {
        return WorkUtils.acceptsRangeAddons(world, pos);
    }

    public int getActionsWork() {
        return 1 + (getRadius() / 4) * speedUpgradeLevel();
    }

    @Override
    public List<TileEntitySpecialRenderer<TileEntity>> getRenderers() {
        final List<TileEntitySpecialRenderer<TileEntity>> renderers = super.getRenderers();
        if (showArea)
            renderers.add(WorkingAreaRenderer.INSTANCE);
        return renderers;
    }

    public AxisAlignedBB getWorkingArea() {
        return WorkUtils.getMachineWorkingArea(world, pos, getRadius(), getHeight(), getFacing());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return getWorkingArea();
    }
}
