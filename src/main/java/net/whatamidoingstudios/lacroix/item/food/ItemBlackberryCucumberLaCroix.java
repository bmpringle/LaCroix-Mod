package net.whatamidoingstudios.lacroix.item.food;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFood;
import net.minecraft.item.Item;
import net.minecraft.item.EnumAction;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ItemBlackberryCucumberLaCroix extends ItemLaCroixDrink {
	
	static {
		liquid = FluidRegistry.getFluid("blackberrycucumberfluid");
	}
	
	public ItemBlackberryCucumberLaCroix() {
		super("blackberrycucumber");
	}
}
