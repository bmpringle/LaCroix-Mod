package net.whatamidoingstudios.lacroix.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLaCroixDrink extends ItemFood {
	public ItemLaCroixDrink(String type) {
		super(5, 0.3f, false);
		setUnlocalizedName(type+"lacroix");
		setRegistryName(type+"lacroix");
		setCreativeTab(CreativeTabs.FOOD);
		setMaxStackSize(1);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.DRINK;
	}
}