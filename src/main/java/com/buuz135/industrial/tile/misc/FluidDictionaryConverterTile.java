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
package com.buuz135.industrial.tile.misc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.buuz135.industrial.api.recipe.FluidDictionaryEntry;
import com.buuz135.industrial.proxy.client.infopiece.ArrowInfoPiece;
import com.buuz135.industrial.tile.CustomSidedTileEntity;
import com.buuz135.industrial.utils.Reference;
import com.buuz135.industrial.utils.WorkUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.ndrei.teslacorelib.TeslaCoreLib;
import net.ndrei.teslacorelib.gui.BasicRenderedGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.inventory.FluidTankType;
import net.ndrei.teslacorelib.inventory.SyncProviderLevel;
import net.ndrei.teslacorelib.netsync.SimpleNBTMessage;

public class FluidDictionaryConverterTile extends CustomSidedTileEntity {

    public static List<String> INPUT_LIST = FluidDictionaryEntry.FLUID_DICTIONARY_RECIPES.stream()
            .map(FluidDictionaryEntry::getFluidOrigin).distinct().collect(Collectors.toList());
    private IFluidTank input;
    private IFluidTank output;
    private int inputPointer;
    private int outputPointer;

    public FluidDictionaryConverterTile() {
        super(FluidDictionaryConverterTile.class.getName().hashCode());
        inputPointer = 0;
        outputPointer = 0;
    }

    public static List<String> getOutputListFromFluid(final String input) {
        return FluidDictionaryEntry.FLUID_DICTIONARY_RECIPES.stream()
                .filter(entry -> entry.getFluidOrigin().equals(input))
                .map(FluidDictionaryEntry::getFluidResult).collect(Collectors.toList());
    }

    public static FluidDictionaryEntry getRecipe(final String input, final String output) {
        return FluidDictionaryEntry.FLUID_DICTIONARY_RECIPES.stream()
                .filter(entry -> entry.getFluidOrigin().equals(input)
                        && entry.getFluidResult().equals(output))
                .findFirst().orElse(null);
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        input = this.addSimpleFluidTank(8000, "input", EnumDyeColor.BLUE, 60, 25,
                FluidTankType.INPUT, fluidStack -> true, fluidStack -> false);
        output = this.addSimpleFluidTank(8000, "output", EnumDyeColor.ORANGE, 115, 25,
                FluidTankType.OUTPUT, fluidStack -> false, fluidStack -> true);
        this.registerSyncIntPart("input", nbtTagInt -> inputPointer = nbtTagInt.getInt(),
                () -> new NBTTagInt(inputPointer), SyncProviderLevel.GUI);
        this.registerSyncIntPart("output", nbtTagInt -> outputPointer = nbtTagInt.getInt(),
                () -> new NBTTagInt(outputPointer), SyncProviderLevel.GUI);
    }

    @Override
    protected void innerUpdate() {
        if (this.world.isRemote)
            return;
        if (WorkUtils.isDisabled(this.blockType))
            return;
        if (inputPointer > INPUT_LIST.size() || inputPointer < 0) {
            inputPointer = 0;
            partialSync("input", true);
        }
        if (inputPointer == 0 && outputPointer != 0) {
            outputPointer = 0;
            partialSync("output", true);
        }
        if (inputPointer == 0 || outputPointer == 0)
            return;
        if (input.getFluidAmount() >= 100) {
            if (!input.getFluid().getFluid().getName().equals(INPUT_LIST.get(inputPointer - 1)))
                return;
            final FluidDictionaryEntry entry = getRecipe(INPUT_LIST.get(inputPointer - 1),
                    getOutputListFromFluid(INPUT_LIST.get(inputPointer - 1))
                            .get(outputPointer - 1));
            if (entry != null) {
                final int fillAmount = (int) Math.floor(100 * entry.getRatio());
                if (output.fill(FluidRegistry.getFluidStack(entry.getFluidResult(), fillAmount),
                        false) == fillAmount) {
                    input.drain(100, true);
                    output.fill(FluidRegistry.getFluidStack(entry.getFluidResult(), fillAmount),
                            true);
                }
            }
        }
    }

    @Override
    protected boolean supportsAddons() {
        return false;
    }

    @Override
    protected boolean shouldAddFluidItemsInventory() {
        return false;
    }

    @Override
    public void readFromNBT(@NotNull final NBTTagCompound compound) {
        inputPointer = compound.getInteger("PointerInput");
        outputPointer = compound.getInteger("PointerOutput");
        super.readFromNBT(compound);
    }

    @NotNull
    @Override
    public NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("PointerInput", inputPointer);
        compound.setInteger("PointerOutput", outputPointer);
        return compound;
    }

    @Nullable
    @Override
    protected SimpleNBTMessage processClientMessage(@Nullable final String messageType,
            @NotNull final NBTTagCompound compound) {
        if (messageType == null)
            return super.processClientMessage(messageType, compound);
        if (messageType.equals("NEXT_INPUT")) {
            inputPointer = (inputPointer + 1) % (INPUT_LIST.size() + 1);
            outputPointer = 0;
            partialSync("input", true);
            partialSync("output", true);
        }
        if (messageType.equals("PREV_INPUT")) {
            --inputPointer;
            if (inputPointer < 0)
                inputPointer = INPUT_LIST.size();
            outputPointer = 0;
            partialSync("input", true);
            partialSync("output", true);
        }
        if (messageType.equals("NEXT_OUTPUT") && inputPointer != 0) {
            outputPointer = (outputPointer + 1)
                    % (getOutputListFromFluid(INPUT_LIST.get(inputPointer - 1)).size() + 1);
            partialSync("output", true);
        }
        if (messageType.equals("PREV_OUTPUT") && inputPointer != 0) {
            --outputPointer;
            if (outputPointer < 0)
                outputPointer = getOutputListFromFluid(INPUT_LIST.get(inputPointer - 1)).size();
            partialSync("output", true);
        }
        return super.processClientMessage(messageType, compound);
    }

    @NotNull
    @Override
    public List<IGuiContainerPiece> getGuiContainerPieces(
            @NotNull final BasicTeslaGuiContainer<?> container) {
        final List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);
        pieces.add(new BasicRenderedGuiPiece(84, 45, 25, 18,
                new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 24, 5));
        pieces.add(new BasicRenderedGuiPiece(40, 43, 18, 18,
                new ResourceLocation(Reference.MOD_ID, "textures/gui/machines.png"), 1, 168) {
            @Override
            public void drawBackgroundLayer(@NotNull final BasicTeslaGuiContainer<?> container,
                    final int guiX, final int guiY, final float partialTicks, final int mouseX,
                    final int mouseY) {
                super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);
                if (inputPointer == 0) {
                    container.mc.getTextureManager().bindTexture(
                            new ResourceLocation("teslacorelib", "textures/gui/basic-machine.png"));
                    container.drawTexturedRect(this.getLeft() + 2, this.getTop() + 2, 146, 210, 14,
                            14);
                } else {
                    if (inputPointer - 1 < INPUT_LIST.size()) {
                        final String fluidName = INPUT_LIST.get(inputPointer - 1);
                        if (FluidRegistry.isFluidRegistered(fluidName)) {
                            final ItemStack stack = FluidUtil
                                    .getFilledBucket(FluidRegistry.getFluidStack(fluidName, 1000));
                            if (!stack.isEmpty()) {
                                container.mc.getRenderItem().renderItemIntoGUI(stack,
                                        guiX + this.getLeft() + 1, guiY + this.getTop() + 1);
                            }
                        }
                    }
                }
            }

            @Override
            public void drawForegroundTopLayer(@NotNull final BasicTeslaGuiContainer<?> container,
                    final int guiX, final int guiY, final int mouseX, final int mouseY) {
                super.drawForegroundTopLayer(container, guiX, guiY, mouseX, mouseY);
                if (!isInside(container, mouseX, mouseY))
                    return;
                if (inputPointer <= 0) {
                    container.drawTooltip(Arrays.asList(
                            new TextComponentTranslation("text.industrialforegoing.button.none")
                                    .getUnformattedComponentText()),
                            mouseX - guiX, mouseY - guiY);
                } else {
                    if (inputPointer - 1 < INPUT_LIST.size()) {
                        final String fluidName = INPUT_LIST.get(inputPointer - 1);
                        if (FluidRegistry.isFluidRegistered(fluidName)) {
                            final ItemStack stack = FluidUtil
                                    .getFilledBucket(FluidRegistry.getFluidStack(fluidName, 1000));
                            if (!stack.isEmpty()) {
                                container.drawTooltip(container.getItemToolTip(stack),
                                        mouseX - guiX, mouseY - guiY);
                            } else {
                                container
                                        .drawTooltip(
                                                Arrays.asList(
                                                        FluidRegistry.getFluidStack(fluidName, 1000)
                                                                .getLocalizedName()),
                                                mouseX - guiX, mouseY - guiY);
                            }
                        }
                    }
                }
            }
        });
        pieces.add(new BasicRenderedGuiPiece(136, 43, 18, 18,
                new ResourceLocation(Reference.MOD_ID, "textures/gui/machines.png"), 1, 168) {
            @Override
            public void drawBackgroundLayer(@NotNull final BasicTeslaGuiContainer<?> container,
                    final int guiX, final int guiY, final float partialTicks, final int mouseX,
                    final int mouseY) {
                super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);
                if (outputPointer <= 0) {
                    container.mc.getTextureManager().bindTexture(
                            new ResourceLocation("teslacorelib", "textures/gui/basic-machine.png"));
                    container.drawTexturedRect(this.getLeft() + 2, this.getTop() + 2, 146, 210, 14,
                            14);
                } else {
                    if (inputPointer > 0 && inputPointer - 1 < INPUT_LIST.size()) {
                        final List<String> names =
                                getOutputListFromFluid(INPUT_LIST.get(inputPointer - 1));
                        if (outputPointer - 1 < names.size()) {
                            final String fluidName = names.get(outputPointer - 1);
                            if (FluidRegistry.isFluidRegistered(fluidName)) {
                                final ItemStack stack = FluidUtil.getFilledBucket(
                                        FluidRegistry.getFluidStack(fluidName, 1000));
                                if (!stack.isEmpty()) {
                                    container.mc.getRenderItem().renderItemIntoGUI(stack,
                                            guiX + this.getLeft() + 1, guiY + this.getTop() + 1);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void drawForegroundTopLayer(@NotNull final BasicTeslaGuiContainer<?> container,
                    final int guiX, final int guiY, final int mouseX, final int mouseY) {
                super.drawForegroundTopLayer(container, guiX, guiY, mouseX, mouseY);
                if (!isInside(container, mouseX, mouseY))
                    return;
                if (outputPointer == 0) {
                    container.drawTooltip(Arrays.asList(
                            new TextComponentTranslation("text.industrialforegoing.button.none")
                                    .getUnformattedComponentText()),
                            mouseX - guiX, mouseY - guiY);
                } else {
                    final List<String> names =
                            getOutputListFromFluid(INPUT_LIST.get(inputPointer - 1));
                    if (outputPointer - 1 < names.size()) {
                        final String fluidName = names.get(outputPointer - 1);
                        if (FluidRegistry.isFluidRegistered(fluidName)) {
                            final ItemStack stack = FluidUtil
                                    .getFilledBucket(FluidRegistry.getFluidStack(fluidName, 1000));
                            if (!stack.isEmpty()) {
                                container.drawTooltip(container.getItemToolTip(stack),
                                        mouseX - guiX, mouseY - guiY);
                            }
                        }
                    }
                }
            }
        });
        pieces.add(new ArrowInfoPiece(42, 26, 17, 72,
                "text.industrialforegoing.button.decrease_fluid") {
            @Override
            protected void clicked() {
                if (TeslaCoreLib.INSTANCE.isClientSide()) {
                    FluidDictionaryConverterTile.this.sendToServer(
                            FluidDictionaryConverterTile.this.setupSpecialNBTMessage("PREV_INPUT"));
                }
            }
        });
        pieces.add(new ArrowInfoPiece(42, 65, 33, 72,
                "text.industrialforegoing.button.increase_fluid") {
            @Override
            protected void clicked() {
                if (TeslaCoreLib.INSTANCE.isClientSide()) {
                    FluidDictionaryConverterTile.this.sendToServer(
                            FluidDictionaryConverterTile.this.setupSpecialNBTMessage("NEXT_INPUT"));
                }
            }
        });
        pieces.add(new ArrowInfoPiece(138, 26, 17, 72,
                "text.industrialforegoing.button.decrease_fluid") {
            @Override
            protected void clicked() {
                if (TeslaCoreLib.INSTANCE.isClientSide()) {
                    FluidDictionaryConverterTile.this.sendToServer(FluidDictionaryConverterTile.this
                            .setupSpecialNBTMessage("PREV_OUTPUT"));
                }
            }
        });
        pieces.add(new ArrowInfoPiece(138, 65, 33, 72,
                "text.industrialforegoing.button.increase_fluid") {
            @Override
            protected void clicked() {
                if (TeslaCoreLib.INSTANCE.isClientSide()) {
                    FluidDictionaryConverterTile.this.sendToServer(FluidDictionaryConverterTile.this
                            .setupSpecialNBTMessage("NEXT_OUTPUT"));
                }
            }
        });
        return pieces;
    }

}
