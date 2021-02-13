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
package com.buuz135.industrial.item;

import java.util.List;
import javax.annotation.Nullable;
import com.buuz135.industrial.proxy.BlockRegistry;
import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.utils.RecipeUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class MobImprisonmentToolItem extends IFCustomItem {

    public MobImprisonmentToolItem() {
        super("mob_imprisonment_tool");
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn,
            final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX,
            final float hitY, final float hitZ) {
        final ItemStack stack = player.getHeldItem(hand);
        if (player.getEntityWorld().isRemote)
            return EnumActionResult.FAIL;
        if (!containsEntity(stack))
            return EnumActionResult.FAIL;
        final Entity entity = getEntityFromStack(stack, worldIn, true);
        final BlockPos blockPos = pos.offset(facing);
        entity.setPositionAndRotation(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5,
                0, 0);
        stack.setTagCompound(new NBTTagCompound());
        player.setHeldItem(hand, stack);
        worldIn.spawnEntity(entity);
        if (entity instanceof EntityLiving)
            ((EntityLiving) entity).playLivingSound();
        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn,
            final EntityLivingBase target, final EnumHand hand) {
        if (target.getEntityWorld().isRemote)
            return false;
        if (target instanceof EntityPlayer || !target.isNonBoss() || !target.isEntityAlive())
            return false;
        if (containsEntity(stack))
            return false;
        final String entityID = EntityList.getKey(target).toString();
        if (isBlacklisted(entityID))
            return false;
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("entity", entityID);
        nbt.setInteger("id", EntityList.getID(target.getClass()));
        target.writeToNBT(nbt);
        stack.setTagCompound(nbt);
        playerIn.swingArm(hand);
        playerIn.setHeldItem(hand, stack);
        target.setDead();
        return true;
    }


    public boolean isBlacklisted(final String entity) {
        return false;
    }

    public boolean containsEntity(final ItemStack stack) {
        return !stack.isEmpty() && stack.hasTagCompound()
                && stack.getTagCompound().hasKey("entity");
    }

    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn,
            final List<String> tooltip, final ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (containsEntity(stack)
                && EntityList.getTranslationName(new ResourceLocation(getID(stack))) != null) {
            tooltip.add("Mob: " + new TextComponentTranslation(
                    EntityList.getTranslationName(new ResourceLocation(getID(stack))))
                            .getUnformattedComponentText());
            tooltip.add("Health: " + stack.getTagCompound().getDouble("Health"));
            if (BlockRegistry.mobDuplicatorBlock.blacklistedEntities
                    .contains(stack.getTagCompound().getString("entity")))
                tooltip.add(TextFormatting.RED + "Entity blacklisted in the Mob Duplicator");
        }
    }

    public Entity getEntityFromStack(final ItemStack stack, final World world,
            final boolean withInfo) {
        final Entity entity = EntityList.createEntityByIDFromName(
                new ResourceLocation(stack.getTagCompound().getString("entity")), world);
        if (withInfo)
            entity.readFromNBT(stack.getTagCompound());
        return entity;
    }

    public String getID(final ItemStack stack) {
        return stack.getTagCompound().getString("entity");
    }

    public void createRecipe() {
        RecipeUtils.addShapedRecipe(new ItemStack(this), " p ", "pgp", " p ", 'p',
                ItemRegistry.plastic, 'g', new ItemStack(Items.GHAST_TEAR));
    }

    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (!containsEntity(stack))
            return new TextComponentTranslation(super.getTranslationKey(stack) + ".name")
                    .getUnformattedComponentText();
        return new TextComponentTranslation(super.getTranslationKey(stack) + ".name")
                .getUnformattedComponentText() + " ("
                + EntityList.getTranslationName(new ResourceLocation(getID(stack))) + ")";
    }
}
