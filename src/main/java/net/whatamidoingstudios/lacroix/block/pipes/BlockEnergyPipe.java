package net.whatamidoingstudios.lacroix.block.pipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.whatamidoingstudios.lacroix.block.ISteam;
import net.whatamidoingstudios.lacroix.block.ISteamConsumer;
import net.whatamidoingstudios.lacroix.block.ISteamProvider;


@EventBusSubscriber
public class BlockEnergyPipe extends Block {
	public static PropertyBool DOWN = PropertyBool.create("down");
	public static PropertyBool UP = PropertyBool.create("up");
	public static PropertyBool NORTH = PropertyBool.create("north");
	public static PropertyBool SOUTH = PropertyBool.create("south");
	public static PropertyBool EAST = PropertyBool.create("east");
	public static PropertyBool WEST = PropertyBool.create("west");
	
	public static PropertyInteger DOWNIO = PropertyInteger.create("downio", 0, 2);
	public static PropertyInteger UPIO = PropertyInteger.create("upio", 0, 2);
	public static PropertyInteger NORTHIO = PropertyInteger.create("northio", 0, 2);
	public static PropertyInteger SOUTHIO = PropertyInteger.create("southio", 0, 2);
	public static PropertyInteger EASTIO = PropertyInteger.create("eastio", 0, 2);
	public static PropertyInteger WESTIO = PropertyInteger.create("westio", 0, 2);
	
	public BlockEnergyPipe() {
		super(Material.ANVIL);
		setRegistryName("energypipe");
		setUnlocalizedName("energypipe");
		setCreativeTab(CreativeTabs.MISC);
		setHardness(3);
		setResistance(5);
		setHarvestLevel("pickaxe", 2);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DOWN, false).withProperty(UP, false).withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(EAST, false).withProperty(WEST, false).withProperty(UPIO, 0).withProperty(DOWNIO, 0).withProperty(NORTHIO, 0).withProperty(SOUTHIO, 0).withProperty(EASTIO, 0).withProperty(WESTIO, 0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
	    return new BlockStateContainer(this, new IProperty[] {DOWN, UP, NORTH, SOUTH, EAST, WEST, DOWNIO, UPIO, NORTHIO, SOUTHIO, EASTIO, WESTIO});
	}

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		ArrayList<IProperty> properties = new ArrayList<IProperty>();
		
		if(worldIn.getTileEntity(pos.up()) != null ? worldIn.getTileEntity(pos.up()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN) || worldIn.getTileEntity(pos.up()) instanceof TileEntityEnergyPipe : false) {
			properties.add(UP);
		}
		if(worldIn.getTileEntity(pos.down()) != null ? worldIn.getTileEntity(pos.down()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP) || worldIn.getTileEntity(pos.down()) instanceof TileEntityEnergyPipe : false) {
			properties.add(DOWN);
		}
		if(worldIn.getTileEntity(pos.north()) != null ? worldIn.getTileEntity(pos.north()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.SOUTH) || worldIn.getTileEntity(pos.north()) instanceof TileEntityEnergyPipe : false) {
			properties.add(NORTH);
		}
		if(worldIn.getTileEntity(pos.south()) != null ? worldIn.getTileEntity(pos.south()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.NORTH) || worldIn.getTileEntity(pos.south()) instanceof TileEntityEnergyPipe : false) {
			properties.add(SOUTH);
		}
		if(worldIn.getTileEntity(pos.east()) != null ? worldIn.getTileEntity(pos.east()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.WEST) || worldIn.getTileEntity(pos.east()) instanceof TileEntityEnergyPipe : false) {
			properties.add(EAST);
		}
		if(worldIn.getTileEntity(pos.west()) != null ? worldIn.getTileEntity(pos.west()).hasCapability(CapabilityEnergy.ENERGY, EnumFacing.EAST) || worldIn.getTileEntity(pos.west()) instanceof TileEntityEnergyPipe : false) {
			properties.add(WEST);
		}
		
		if(worldIn.getTileEntity(pos) instanceof TileEntityEnergyPipe) {
			state = state.withProperty(UPIO, ((TileEntityEnergyPipe)worldIn.getTileEntity(pos)).TYPE_UP);
			state = state.withProperty(DOWNIO, ((TileEntityEnergyPipe)worldIn.getTileEntity(pos)).TYPE_DOWN);
			state = state.withProperty(NORTHIO, ((TileEntityEnergyPipe)worldIn.getTileEntity(pos)).TYPE_NORTH);
			state = state.withProperty(SOUTHIO, ((TileEntityEnergyPipe)worldIn.getTileEntity(pos)).TYPE_SOUTH);
			state = state.withProperty(EASTIO, ((TileEntityEnergyPipe)worldIn.getTileEntity(pos)).TYPE_EAST);
			state = state.withProperty(WESTIO, ((TileEntityEnergyPipe)worldIn.getTileEntity(pos)).TYPE_WEST);
		}
		
		for(IProperty property : properties) {
			state = state.withProperty(property, true);
		}
		return state;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
		this.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75));
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityEnergyPipe();
	}
}
