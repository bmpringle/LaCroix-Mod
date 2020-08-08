package net.whatamidoingstudios.lacroix.item.food;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPassionfruitLaCroix extends ItemLaCroixDrink {
	
	static {
		liquid = FluidRegistry.getFluid("passionfruitfluid");
	}
	
	public ItemPassionfruitLaCroix() {
		super("passionfruit");
	}
}