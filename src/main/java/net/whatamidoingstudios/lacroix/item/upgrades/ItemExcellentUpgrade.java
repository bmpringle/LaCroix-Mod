package net.whatamidoingstudios.lacroix.item.upgrades;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.TileEntityUpgradeable;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;

public class ItemExcellentUpgrade extends Item {
	public ItemExcellentUpgrade() {
		setUnlocalizedName("excellentupgrade");
		setRegistryName("excellentupgrade");
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(player.getHeldItem(hand).getItem() == this) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityUpgradeable) {
				if(((TileEntityUpgradeable)te).upgrade(EnumUpgrades.Excellent)) {
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
