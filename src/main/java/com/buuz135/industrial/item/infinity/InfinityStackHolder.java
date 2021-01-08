package com.buuz135.industrial.item.infinity;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.capability.ItemStackHolderCapability;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class InfinityStackHolder extends ItemStackHolderCapability implements IScreenAddonProvider {

    public static int SLOT = 0;

    public InfinityStackHolder() {
        super(() -> Minecraft.getInstance().player.inventory.getStackInSlot(SLOT));
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> factory = new ArrayList<>();
        if (getHolder().get().getItem() instanceof IInfinityDrillScreenAddons) {
            factory.addAll(((IInfinityDrillScreenAddons) getHolder().get().getItem()).getScreenAddons(getHolder()));
        }
        return factory;
    }

}