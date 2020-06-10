package net.whatamidoingstudios.lacroix.item;

import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.heater.BlockHeater;
import net.whatamidoingstudios.lacroix.block.heater.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;

@EventBusSubscriber
public class ItemBlockHeater extends ItemBlock {

	public ItemBlockHeater() {
		super(BlockHeater.blockHeater);
		setRegistryName("coalheater");
		setUnlocalizedName("coalheater");
		this.setMaxStackSize(1);
	}
	
	@SubscribeEvent
    public static void onItemLoad(AttachCapabilitiesEvent<ItemStack> event)
    {
        if (event.getObject().getItem() == ModObjects.itemblock_coalheater)
        {
        	NBTTagCompound tag = new NBTTagCompound();
    		if(event.getObject().hasTagCompound()) {
    			tag = event.getObject().getTagCompound();
    		}
    		if(tag.getString("level") == "") {
    			tag.setString("level", EnumUpgrades.Basic.toString());
    		}
    		if(tag.getInteger("steam") == 0) {
    			tag.setInteger("steam", 0);
    		}
    		event.getObject().setTagCompound(tag);
    		event.getObject().clearCustomName();
    		String name = "§f" + event.getObject().getTagCompound().getString("level") + " " + event.getObject().getDisplayName();
    		event.getObject().setStackDisplayName(name);
        }
    }
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.getTagCompound() != null) {
			tooltip.add("steam: " + stack.getTagCompound().getInteger("steam"));
		}
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!world.setBlockState(pos, newState, 11)) {
			return false;
		}
		

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            setTileEntityNBT(world, player, pos, stack);
            TileEntity te = world.getTileEntity(pos);
            if(te != null && te instanceof TileEntityHeater) {
            	((TileEntityHeater)te).level = EnumUpgrades.valueOf(stack.getTagCompound().getString("level"));
            	((TileEntityHeater)te).steam = stack.getTagCompound().getInteger("steam");
            }
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP) {
            	CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            }
        }

        return true;
	}
	
}
