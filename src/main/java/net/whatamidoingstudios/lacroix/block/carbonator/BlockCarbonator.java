package net.whatamidoingstudios.lacroix.block.carbonator;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.gui.LaCroixGuiHandler;

public class BlockCarbonator extends Block {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyInteger TEXTURE = PropertyInteger.create("texture", 0, 3);
	
	public BlockCarbonator() {
		super(Material.ANVIL);
		setRegistryName("carbonator");
		setUnlocalizedName("carbonator");
		setCreativeTab(CreativeTabs.MISC);
		setHardness(5);
		setResistance(10);
		setHarvestLevel("pickaxe", 2);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TEXTURE, 0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
	    return new BlockStateContainer(this, new IProperty[] {FACING, TEXTURE});
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		int texture = 0;
		if(worldIn.getTileEntity(pos) instanceof TileEntityCarbonator) {
			switch(((TileEntityCarbonator)worldIn.getTileEntity(pos)).level) {
			case Basic:
				texture = 0;
				break;
			case Advanced:
				texture = 1;
				break;
			case Excellent:
				texture = 2;
				break;
			case Perfect:
				texture = 3;
				break;
			default:
				break;
			
			}
		}
		return state.withProperty(TEXTURE, texture);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
	    return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	} 
	
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityCarbonator();
	}
	
	@Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (willHarvest) {
        	return true;
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }
	
	@Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityCarbonator) {
        	TileEntityCarbonator te = (TileEntityCarbonator)world.getTileEntity(pos);
        	ItemStack carbonatorBlock = new ItemStack(ModObjects.carbonator, 1);
        	NBTTagCompound tag = new NBTTagCompound();
    		if(carbonatorBlock.hasTagCompound()) {
    			tag = carbonatorBlock.getTagCompound();
    		}
    		tag.setString("level", te.level.toString());
    		tag.setInteger("energy", te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH).getEnergyStored());
    		carbonatorBlock.setTagCompound(tag);
    		carbonatorBlock.clearCustomName();
    		String name = EnumUpgrades.colorOf(carbonatorBlock.getTagCompound().getString("level") + " " + carbonatorBlock.getDisplayName(), te.level);
    		carbonatorBlock.setStackDisplayName(name);
        	drops.add(carbonatorBlock);
        }
    }
    
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			if(!player.isSneaking()) {
				if(player.getHeldItem(hand).getItem() == Items.WATER_BUCKET && ((TileEntityCarbonator)world.getTileEntity(pos)).fluidHandlerWater.getFluidAmount()+1000 <= ((TileEntityCarbonator)world.getTileEntity(pos)).liquidCapacityPerFluid) {
					if(!player.isCreative()) {	
						player.setHeldItem(hand, new ItemStack(Items.BUCKET, 1));
					}
					if(world.getTileEntity(pos) instanceof TileEntityCarbonator) {
						((TileEntityCarbonator)world.getTileEntity(pos)).fluidHandlerWater.fill(new FluidStack(FluidRegistry.WATER, 1000), true);
					}
				}else if(player.getHeldItem(hand).getItem() == Items.BUCKET && ((TileEntityCarbonator)world.getTileEntity(pos)).fluidHandlerWater.getFluidAmount() >= 1000) {
					if(!player.isCreative()) {
						player.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET, 1));
					}
					if(world.getTileEntity(pos) instanceof TileEntityCarbonator) {
						((TileEntityCarbonator)world.getTileEntity(pos)).fluidHandlerWater.drain(new FluidStack(FluidRegistry.WATER, 1000), true);
					}
				}
			}
		}
		if(world.isRemote) {
			if(!player.isSneaking()) {
				if(!(player.getHeldItem(hand).getItem() == Items.WATER_BUCKET || player.getHeldItem(hand).getItem() == Items.BUCKET)) {
					player.openGui(LaCroix.instance, LaCroixGuiHandler.CARBONATOR, world, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
		return true;
	}
}