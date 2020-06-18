package net.whatamidoingstudios.lacroix.item.itemblocks;

import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.pipes.BlockSteamPipe;
import net.whatamidoingstudios.lacroix.block.steamturbine.TileEntityTurbine;
import net.whatamidoingstudios.lacroix.capabilities.EnergyStorageUpgradeable;

@EventBusSubscriber
public class ItemBlockSteamTurbine extends ItemBlock {

	public ItemBlockSteamTurbine() {
		super(ModObjects.steamturbine);
		setRegistryName("steamturbine");
		setUnlocalizedName("steamturbine");
		this.setMaxStackSize(1);
	}

	@SubscribeEvent
    public static void onItemLoad(AttachCapabilitiesEvent<ItemStack> event)
    {
        if (event.getObject().getItem() == ModObjects.itemblock_steamturbine)
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
		ModelResourceLocation basicModel = new ModelResourceLocation(new ResourceLocation(LaCroix.MODID, "blocks/basic/steamturbine"), "inventory");

        ModelBakery.registerItemVariants(this, basicModel);

        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
            	return basicModel;
            }
        });
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		int level = EnumUpgrades.enumToInt(EnumUpgrades.safeValueOf(stack.getTagCompound().getString("level")));
		if(stack.getTagCompound() != null) {
			tooltip.add("energy: " + stack.getTagCompound().getInteger("energy"));
			tooltip.add("capacity: " + (40000+20000*level));
			tooltip.add("rf/tick: " + (level+1)*250);
			tooltip.add("max inout/tick: " + (40000+20000*level)/100);
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);

        boolean canPlaceOnBlock = worldIn.getBlockState(pos.down()).getBlock().isSideSolid(worldIn.getBlockState(pos.down()).getBlock().getDefaultState(), worldIn, pos, EnumFacing.UP) || worldIn.getBlockState(pos.down()).getBlock() instanceof BlockSteamPipe;
        
        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, (Entity)null) && canPlaceOnBlock)
        {
            int i = this.getMetadata(itemstack.getMetadata());
            IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

            if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                iblockstate1 = worldIn.getBlockState(pos);
                SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
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
            if(te != null && te instanceof TileEntityTurbine && stack.getTagCompound() != null) {
            	((TileEntityTurbine)te).level = EnumUpgrades.safeValueOf(stack.getTagCompound().getString("level"));
            	((EnergyStorageUpgradeable)((TileEntityTurbine)te).getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH)).setEnergy(stack.getTagCompound().getInteger("energy"));
            }
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP) {
            	CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            }
        }

        return true;
	}


}
