package net.whatamidoingstudios.lacroix.item.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityEnergyPipe;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityFluidPipe;

public class ItemWrench extends Item {
	public ItemWrench() {
		setUnlocalizedName("wrench");
		setRegistryName("wrench");
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			if(player.getHeldItem(hand).getItem() == this) {
				TileEntity te = worldIn.getTileEntity(pos);
				if(te instanceof TileEntityEnergyPipe) {
					((TileEntityEnergyPipe)te).toggleType(facing);
					return EnumActionResult.SUCCESS;
				}
				if(te instanceof TileEntityFluidPipe) {
					((TileEntityFluidPipe)te).toggleType(facing);
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
	}
}
