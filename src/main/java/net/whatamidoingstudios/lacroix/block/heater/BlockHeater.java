package net.whatamidoingstudios.lacroix.block.heater;

import java.util.Random;

import javax.annotation.Nullable;

import org.omg.PortableServer.POAManagerPackage.State;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.ModObjects;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.gui.LaCroixGuiHandler;

public class BlockHeater extends Block {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool OPENHATCH = PropertyBool.create("openhatch");
	public static final PropertyInteger TEXTURE = PropertyInteger.create("texture", 0, 3);
	
	public BlockHeater() {
		super(Material.ANVIL);
		setRegistryName("coalheater");
		setUnlocalizedName("coalheater");
		setCreativeTab(CreativeTabs.MISC);
		setHardness(5);
		setResistance(10);
		setHarvestLevel("pickaxe", 2);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPENHATCH, false).withProperty(TEXTURE, 0));
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
	    return new BlockStateContainer(this, new IProperty[] {FACING, OPENHATCH, TEXTURE});
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		int texture = 0;
		if(worldIn.getTileEntity(pos) instanceof TileEntityHeater) {
			switch(((TileEntityHeater)worldIn.getTileEntity(pos)).level) {
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
		return state.withProperty(TEXTURE, texture).withProperty(OPENHATCH, !((TileEntityHeater)worldIn.getTileEntity(pos)).captureSteam);
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
		return new TileEntityHeater();
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
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityHeater) {
        	TileEntityHeater te = (TileEntityHeater)world.getTileEntity(pos);
        	ItemStack heaterBlock = new ItemStack(ModObjects.coalheater, 1);
        	NBTTagCompound tag = new NBTTagCompound();
    		if(heaterBlock.hasTagCompound()) {
    			tag = heaterBlock.getTagCompound();
    		}
    		tag.setString("level", te.level.toString());
    		tag.setInteger("steam", te.steam);
    		heaterBlock.setTagCompound(tag);
    		heaterBlock.clearCustomName();
    		String name = EnumUpgrades.colorOf(heaterBlock.getTagCompound().getString("level") + " " + heaterBlock.getDisplayName(), te.level);
    		heaterBlock.setStackDisplayName(name);
        	drops.add(heaterBlock);
        	drops.add(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH).getStackInSlot(0)); 	
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
				player.openGui(LaCroix.instance, LaCroixGuiHandler.HEATER, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
}