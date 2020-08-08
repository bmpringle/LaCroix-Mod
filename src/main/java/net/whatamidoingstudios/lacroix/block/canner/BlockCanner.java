package net.whatamidoingstudios.lacroix.block.canner;

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
import net.minecraftforge.items.CapabilityItemHandler;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.carbonator.TileEntityCarbonator;
import net.whatamidoingstudios.lacroix.gui.LaCroixGuiHandler;

public class BlockCanner extends Block {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyInteger TEXTURE = PropertyInteger.create("texture", 0, 3);
	
	public BlockCanner() {
		super(Material.ANVIL);
		setRegistryName("canner");
		setUnlocalizedName("canner");
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
		if(worldIn.getTileEntity(pos) instanceof TileEntityCanner) {
			switch(((TileEntityCanner)worldIn.getTileEntity(pos)).level) {
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
		return new TileEntityCanner();
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
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityCanner) {
        
        	ItemStack canner = new ItemStack(ModObjects.itemblock_canner, 1);
        
        	NBTTagCompound tag = new NBTTagCompound();
    		if(canner.hasTagCompound()) {
    			tag = canner.getTagCompound();
    		}
    		tag.setString("level", ((TileEntityCanner)world.getTileEntity(pos)).level.toString());
    		tag.setInteger("energy", ((TileEntityCanner)world.getTileEntity(pos)).getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH).getEnergyStored());
        	canner.clearCustomName();
    		String name = EnumUpgrades.colorOf(canner.getTagCompound().getString("level") + " " + canner.getDisplayName(), ((TileEntityCanner)world.getTileEntity(pos)).level);
    		canner.setStackDisplayName(name);  
    		
    		drops.add(canner);
        }
    }
    
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
    		EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	if(!worldIn.isRemote) {
			if(!playerIn.isSneaking()) {
				playerIn.openGui(LaCroix.instance, LaCroixGuiHandler.CANNER, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		}
    	return true;
    }
}
