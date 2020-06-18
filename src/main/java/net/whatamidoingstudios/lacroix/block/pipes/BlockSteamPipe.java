package net.whatamidoingstudios.lacroix.block.pipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.whatamidoingstudios.lacroix.block.ISteam;
import net.whatamidoingstudios.lacroix.block.ISteamConsumer;
import net.whatamidoingstudios.lacroix.block.ISteamProvider;


@EventBusSubscriber
public class BlockSteamPipe extends Block {
	public static PropertyBool DOWN = PropertyBool.create("down");
	public static PropertyBool UP = PropertyBool.create("up");
	public static PropertyBool NORTH = PropertyBool.create("north");
	public static PropertyBool SOUTH = PropertyBool.create("south");
	public static PropertyBool EAST = PropertyBool.create("east");
	public static PropertyBool WEST = PropertyBool.create("west");
	
	public BlockSteamPipe() {
		super(Material.ANVIL);
		setRegistryName("steampipe");
		setUnlocalizedName("steampipe");
		setCreativeTab(CreativeTabs.MISC);
		setHardness(3);
		setResistance(5);
		setHarvestLevel("pickaxe", 2);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DOWN, false).withProperty(UP, false).withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(EAST, false).withProperty(WEST, false));
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
	    return new BlockStateContainer(this, new IProperty[] {DOWN, UP, NORTH, SOUTH, EAST, WEST});
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
		
		if(worldIn.getBlockState(pos.down()).getBlock() == this) {
			properties.add(DOWN);
		}
		if(worldIn.getBlockState(pos.up()).getBlock() == this) {
			properties.add(UP);
		}
		if(worldIn.getBlockState(pos.north()).getBlock() == this) {
			properties.add(NORTH);
		}
		if(worldIn.getBlockState(pos.south()).getBlock() == this) {
			properties.add(SOUTH);
		}
		if(worldIn.getBlockState(pos.east()).getBlock() == this) {
			properties.add(EAST);
		}
		if(worldIn.getBlockState(pos.west()).getBlock() == this) {
			properties.add(WEST);
		}
		
		if(worldIn.getTileEntity(pos.down()) instanceof ISteam) {
			if(((ISteam)worldIn.getTileEntity(pos.down())).canConnectOnSide(EnumFacing.UP)) {
				properties.add(DOWN);
			}
		}
		if(worldIn.getTileEntity(pos.up()) instanceof ISteam) {
			if(((ISteam)worldIn.getTileEntity(pos.up())).canConnectOnSide(EnumFacing.DOWN)) {
				properties.add(UP);
			}
		}
		if(worldIn.getTileEntity(pos.north()) instanceof ISteam) {
			if(((ISteam)worldIn.getTileEntity(pos.north())).canConnectOnSide(EnumFacing.SOUTH)) {
				properties.add(NORTH);
			}
		}
		if(worldIn.getTileEntity(pos.south()) instanceof ISteam) {
			if(((ISteam)worldIn.getTileEntity(pos.south())).canConnectOnSide(EnumFacing.NORTH)) {
				properties.add(SOUTH);
			}
		}
		if(worldIn.getTileEntity(pos.east()) instanceof ISteam) {
			if(((ISteam)worldIn.getTileEntity(pos.east())).canConnectOnSide(EnumFacing.WEST)) {
				properties.add(EAST);
			}
		}
		if(worldIn.getTileEntity(pos.west()) instanceof ISteam) {
			if(((ISteam)worldIn.getTileEntity(pos.west())).canConnectOnSide(EnumFacing.EAST)) {
				properties.add(WEST);
			}
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
}
