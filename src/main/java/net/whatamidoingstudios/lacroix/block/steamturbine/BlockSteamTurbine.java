package net.whatamidoingstudios.lacroix.block.steamturbine;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;
import net.whatamidoingstudios.lacroix.block.utils.AxisAlignedBBUtils;
import net.whatamidoingstudios.lacroix.gui.LaCroixGuiHandler;

public class BlockSteamTurbine extends Block {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyInteger TEXTURE = PropertyInteger.create("texture", 0, 3);
			
	public BlockSteamTurbine() {
		super(Material.ANVIL);
		setRegistryName("steamturbine");
		setUnlocalizedName("steamturbine");
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
		if(worldIn.getTileEntity(pos) instanceof TileEntityTurbine) {
			switch(((TileEntityTurbine)worldIn.getTileEntity(pos)).level) {
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityTurbine();
	}
	
	@Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityTurbine) {
        	TileEntityTurbine te = (TileEntityTurbine)world.getTileEntity(pos);
        	ItemStack turbineBlock = new ItemStack(ModObjects.steamturbine, 1);
        	NBTTagCompound tag = new NBTTagCompound();
    		if(turbineBlock.hasTagCompound()) {
    			tag = turbineBlock.getTagCompound();
    		}
    		tag.setString("level", te.level.toString());
    		tag.setInteger("energy", te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH).getEnergyStored());
    		turbineBlock.setTagCompound(tag);
    		turbineBlock.clearCustomName();
    		String name = EnumUpgrades.colorOf(turbineBlock.getTagCompound().getString("level") + " " + turbineBlock.getDisplayName(), te.level);
    		turbineBlock.setStackDisplayName(name);
        	drops.add(turbineBlock);
        }
    }
	
	@Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (willHarvest) {
        	return true;
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }
	
	@Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
	    return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			if(!player.isSneaking()) {
				player.openGui(LaCroix.instance, LaCroixGuiHandler.STEAMTURBINE, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {	
		switch(state.getValue(FACING).getIndex()) {
		case 3:
			return new AxisAlignedBB(0.25, 0, 0.3125, 0.8125, 0.375, 0.6875);
		case 4:
			return new AxisAlignedBB(0.3125, 0, 0.25, 0.6875, 0.375, 0.8125);
		case 5:
			return new AxisAlignedBB(0.3125, 0, 0.1875, 0.6875, 0.375, 0.75);
		default:
			return new AxisAlignedBB(0.1875, 0, 0.3125, 0.75, 0.375, 0.6875);
		}
	}
}
