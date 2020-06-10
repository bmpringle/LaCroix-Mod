package net.whatamidoingstudios.lacroix.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.whatamidoingstudios.lacroix.block.heater.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityUpgradeable;

public class ItemAdvancedUpgrade extends Item {
	public ItemAdvancedUpgrade() {
		setUnlocalizedName("advancedupgrade");
		setRegistryName("advancedupgrade");
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(player.getHeldItem(hand).getItem() == this) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityUpgradeable) {
				if(((TileEntityUpgradeable)te).upgrade(EnumUpgrades.Advanced)) {
					player.getHeldItem(hand).shrink(1);
					return EnumActionResult.SUCCESS;
				}else {
					return EnumActionResult.FAIL;
				}
			}
		}
		return EnumActionResult.PASS;
	}
}
