package net.whatamidoingstudios.lacroix.capabilities;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankLaCroix extends FluidTank {

	public FluidTankLaCroix(Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
	}
	
	public void setFluidAmount(int amount ) {
		fluid.amount = amount;
	}
	
	public int fill(int amount) {
		return super.fill(new FluidStack(fluid, amount),  true);
	}
}
