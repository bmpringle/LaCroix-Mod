package net.whatamidoingstudios.lacroix.item.itemblocks;

import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.canner.TileEntityCanner;
import net.whatamidoingstudios.lacroix.block.carbonator.TileEntityCarbonator;

@EventBusSubscriber
public class ItemBlockCanner extends ItemBlock {

	public ItemBlockCanner() {
		super(ModObjects.canner);
		setRegistryName("canner");
		setUnlocalizedName("canner");
		this.setMaxStackSize(1);
	}
	
	@SubscribeEvent
    public static void onItemLoad(AttachCapabilitiesEvent<ItemStack> event)
    {
        if (event.getObject().getItem() == ModObjects.itemblock_canner)
        {
        	NBTTagCompound tag = new NBTTagCompound();
    		if(event.getObject().hasTagCompound()) {
    			tag = event.getObject().getTagCompound();
    		}
    		if(tag.getString("level") == "") {
    			tag.setString("level", EnumUpgrades.Basic.toString());
    		}
    		if(tag.getInteger("energy") == 0) {
    			tag.setInteger("energy", 0);
    		}
    		event.getObject().setTagCompound(tag);
    		event.getObject().clearCustomName();
    		String name = EnumUpgrades.colorOf(event.getObject().getTagCompound().getString("level") + " " + event.getObject().getDisplayName(), EnumUpgrades.safeValueOf(event.getObject().getTagCompound().getString("level")));
    		event.getObject().setStackDisplayName(name);
        }
    }
	
	@SideOnly(Side.CLIENT)
	public void initModels() {
		ModelResourceLocation basicModel = new ModelResourceLocation(new ResourceLocation(LaCroix.MODID, "blocks/basic/canner"), "inventory");
        ModelResourceLocation advancedModel = new ModelResourceLocation(new ResourceLocation(LaCroix.MODID, "blocks/advanced/canner"), "inventory");
        ModelResourceLocation excellentModel = new ModelResourceLocation(new ResourceLocation(LaCroix.MODID, "blocks/excellent/canner"), "inventory");
        ModelResourceLocation perfectModel = new ModelResourceLocation(new ResourceLocation(LaCroix.MODID, "blocks/perfect/canner"), "inventory");

        ModelBakery.registerItemVariants(this, basicModel, advancedModel, excellentModel, perfectModel);

        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
            	if(stack.getTagCompound() == null) return basicModel;
            	if(stack.getTagCompound().getString("level") == "") return basicModel;
            	
                switch(EnumUpgrades.safeValueOf(stack.getTagCompound().getString("level"))) {
                case Basic:
                	return basicModel;
                case Advanced:
                	return advancedModel;
				case Excellent:
					return excellentModel;
				case Perfect:
					return perfectModel;
				default:
					System.out.println("LEVEL IS NOT IN ITEMSTACK NBT. THIS WILL NOT CAUSE A CRASH, \n "
							+ "BUT INDICATES SOMETHING IS VERY VERY WRONG. GET IN CONTACT WITH MOD AUTHOR ABOUT THIS.");
					return basicModel;
                }
            }
        });
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.getTagCompound() != null) {
			tooltip.add("energy: " + stack.getTagCompound().getInteger("energy") + "/" + (int) Math.pow(2, EnumUpgrades.enumToInt(EnumUpgrades.safeValueOf(stack.getTagCompound().getString("level")))+2)*10000);
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
            if(te != null && te instanceof TileEntityCanner && stack.getTagCompound() != null) {
            	((TileEntityCanner)te).level = EnumUpgrades.safeValueOf(stack.getTagCompound().getString("level"));
            	((TileEntityCanner)te).resetEnergyStorage(stack.getTagCompound().getInteger("energy"), (int) (Math.pow(2, EnumUpgrades.enumToInt(EnumUpgrades.safeValueOf(stack.getTagCompound().getString("level")))+2)*10000));
            }
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP) {
            	CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            }
        }

        return true;
	}
	
}
