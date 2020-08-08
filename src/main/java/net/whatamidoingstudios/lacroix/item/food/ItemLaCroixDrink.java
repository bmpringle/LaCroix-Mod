package net.whatamidoingstudios.lacroix.item.food;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class ItemLaCroixDrink extends ItemFood {
	
	public static Fluid liquid = null;
	
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