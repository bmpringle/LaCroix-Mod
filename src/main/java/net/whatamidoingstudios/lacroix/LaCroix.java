package net.whatamidoingstudios.lacroix;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.advancements.critereon.ItemPredicates;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.whatamidoingstudios.lacroix.advancements.HealtierThanSodaPredicate;
import net.whatamidoingstudios.lacroix.block.canner.BlockCanner;
import net.whatamidoingstudios.lacroix.block.canner.TileEntityCanner;
import net.whatamidoingstudios.lacroix.block.carbonator.BlockCarbonator;
import net.whatamidoingstudios.lacroix.block.carbonator.TileEntityCarbonator;
import net.whatamidoingstudios.lacroix.block.heater.BlockHeater;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;
import net.whatamidoingstudios.lacroix.block.pipes.BlockEnergyPipe;
import net.whatamidoingstudios.lacroix.block.pipes.BlockFluidPipe;
import net.whatamidoingstudios.lacroix.block.pipes.BlockSteamPipe;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityEnergyPipe;
import net.whatamidoingstudios.lacroix.block.pipes.TileEntityFluidPipe;
import net.whatamidoingstudios.lacroix.block.steamturbine.BlockSteamTurbine;
import net.whatamidoingstudios.lacroix.block.steamturbine.TileEntityTurbine;
import net.whatamidoingstudios.lacroix.fluids.LaCroixFluid;
import net.whatamidoingstudios.lacroix.gui.LaCroixGuiHandler;
import net.whatamidoingstudios.lacroix.item.food.ItemAppleCranberryLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemApricotLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemBerryLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemBlackberryCucumberLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemCherryLimeLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemCoconutLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemCranberryRaspberryLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemKeylimeLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemKiwiWatermelonLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemLaColaLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemLemonLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemLimeLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemMangoLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemMelonGrapefruitLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemOrangeLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemPamplemousseLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemPassionfruitLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemPeachPearLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemPineappleLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemPureLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemStrawberryLaCroix;
import net.whatamidoingstudios.lacroix.item.food.ItemTangerineLaCroix;
import net.whatamidoingstudios.lacroix.item.itemblocks.ItemBlockCanner;
import net.whatamidoingstudios.lacroix.item.itemblocks.ItemBlockCarbonator;
import net.whatamidoingstudios.lacroix.item.itemblocks.ItemBlockHeater;
import net.whatamidoingstudios.lacroix.item.itemblocks.ItemBlockSteamTurbine;
import net.whatamidoingstudios.lacroix.item.tools.ItemWrench;
import net.whatamidoingstudios.lacroix.item.upgrades.ItemAdvancedUpgrade;
import net.whatamidoingstudios.lacroix.item.upgrades.ItemExcellentUpgrade;
import net.whatamidoingstudios.lacroix.item.upgrades.ItemPerfectUpgrade;
import net.whatamidoingstudios.lacroix.network.LaCroixNetworkHandler;

@Mod(modid = LaCroix.MODID, version = LaCroix.VERSION)
public class LaCroix {
	public static final String MODID = "lacroix";
	public static final String VERSION = "1.7.0";
	
	@SidedProxy(clientSide = "net.whatamidoingstudios.lacroix.ClientProxyLaCroix", serverSide = "net.whatamidoingstudios.lacroix.ServerProxyLaCroix")
	public static IProxyLaCroix proxy;
	
	@Mod.Instance(MODID)
	public static LaCroix instance;
	
	public static LaCroixNetworkHandler networkHandler = new LaCroixNetworkHandler();
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new LaCroixGuiHandler());
		ItemPredicates.register(new ResourceLocation(MODID, "healthierthansoda"), HealtierThanSodaPredicate::new);
		proxy.preInit(event);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		proxy.serverLoad(event);
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(
				new BlockHeater(),
				new BlockSteamTurbine(),
				new BlockSteamPipe(),
				new BlockCarbonator(),
				new BlockEnergyPipe(),
				new BlockCanner(),
				new BlockFluidPipe()
		);
		
		GameRegistry.registerTileEntity(TileEntityHeater.class, "lacroix:heatertileentity");
		GameRegistry.registerTileEntity(TileEntityTurbine.class, "lacroix:steamturbinetileentity");
		GameRegistry.registerTileEntity(TileEntityCarbonator.class, "lacroix:carbonatortileentity");
		GameRegistry.registerTileEntity(TileEntityEnergyPipe.class, "lacroix:energypipetileentity");
		GameRegistry.registerTileEntity(TileEntityCanner.class, "lacroix:cannertileentity");
		GameRegistry.registerTileEntity(TileEntityFluidPipe.class, "lacroix:fluidpipetileentity");
		
		registerFluids();
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
			new ItemAppleCranberryLaCroix(),
			new ItemApricotLaCroix(),
			new ItemBerryLaCroix(),
			new ItemBlackberryCucumberLaCroix(),
			new ItemCherryLimeLaCroix(),
			new ItemCoconutLaCroix(),
			new ItemCranberryRaspberryLaCroix(),
			new ItemKeylimeLaCroix(),
			new ItemKiwiWatermelonLaCroix(),
			new ItemLaColaLaCroix(),
			new ItemLemonLaCroix(),
			new ItemLimeLaCroix(),
			new ItemMangoLaCroix(),
			new ItemMelonGrapefruitLaCroix(),
			new ItemOrangeLaCroix(),
			new ItemPamplemousseLaCroix(),
			new ItemPassionfruitLaCroix(),
			new ItemPeachPearLaCroix(),
			new ItemPineappleLaCroix(),
			new ItemPureLaCroix(),
			new ItemStrawberryLaCroix(),
			new ItemTangerineLaCroix(),
			new ItemAdvancedUpgrade(),
			new ItemExcellentUpgrade(),
			new ItemPerfectUpgrade(),
			new ItemWrench(),
			new ItemBlockHeater(),
			new ItemBlockSteamTurbine(),
			new ItemBlock(ModObjects.steampipe).setRegistryName(MODID, "steampipe").setUnlocalizedName("steampipe").setCreativeTab(CreativeTabs.MISC),
			new ItemBlockCarbonator(),
			new ItemBlock(ModObjects.energypipe).setRegistryName(MODID, "energypipe").setUnlocalizedName("energypipe").setCreativeTab(CreativeTabs.MISC),
			new ItemBlockCanner(),
			new Item().setRegistryName(MODID, "emptycan").setUnlocalizedName("emptycan").setCreativeTab(CreativeTabs.MISC),
			new ItemBlock(ModObjects.fluidpipe).setRegistryName(MODID, "fluidpipe").setUnlocalizedName("fluidpipe").setCreativeTab(CreativeTabs.MISC)
		);
	}
	
	@SubscribeEvent
	public void registerBiomes(RegistryEvent.Register<Biome> event) {

	}

	@SubscribeEvent
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {

	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {

	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<net.minecraft.util.SoundEvent> event) {

	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(ModObjects.applecranberrylacroix, 0, new ModelResourceLocation(ModObjects.applecranberrylacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.apricotlacroix, 0, new ModelResourceLocation(ModObjects.apricotlacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.coconutlacroix, 0, new ModelResourceLocation(ModObjects.coconutlacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.peachpearlacroix, 0, new ModelResourceLocation(ModObjects.peachpearlacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.pineapplelacroix, 0, new ModelResourceLocation(ModObjects.pineapplelacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.tangerinelacroix, 0, new ModelResourceLocation(ModObjects.tangerinelacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.cherrylimelacroix, 0, new ModelResourceLocation(ModObjects.cherrylimelacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.limelacroix, 0, new ModelResourceLocation(ModObjects.limelacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.keylimelacroix, 0, new ModelResourceLocation(ModObjects.keylimelacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.berrylacroix, 0, new ModelResourceLocation(ModObjects.berrylacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.cranberryraspberrylacroix, 0, new ModelResourceLocation(ModObjects.cranberryraspberrylacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.orangelacroix, 0, new ModelResourceLocation(ModObjects.orangelacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.blackberrycucumberlacroix, 0, new ModelResourceLocation(ModObjects.blackberrycucumberlacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.purelacroix, 0, new ModelResourceLocation(ModObjects.purelacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.lacolalacroix, 0, new ModelResourceLocation(ModObjects.lacolalacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.passionfruitlacroix, 0, new ModelResourceLocation(ModObjects.passionfruitlacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.pamplemousselacroix, 0, new ModelResourceLocation(ModObjects.pamplemousselacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.mangolacroix, 0, new ModelResourceLocation(ModObjects.mangolacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.melongrapefruitlacroix, 0, new ModelResourceLocation(ModObjects.melongrapefruitlacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.strawberrylacroix, 0, new ModelResourceLocation(ModObjects.strawberrylacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.kiwiwatermelonlacroix, 0, new ModelResourceLocation(ModObjects.kiwiwatermelonlacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.lemonlacroix, 0, new ModelResourceLocation(ModObjects.lemonlacroix.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.advancedupgrade, 0, new ModelResourceLocation(ModObjects.advancedupgrade.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.excellentupgrade, 0, new ModelResourceLocation(ModObjects.excellentupgrade.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.perfectupgrade, 0, new ModelResourceLocation(ModObjects.perfectupgrade.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.emptycan, 0, new ModelResourceLocation(ModObjects.emptycan.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.wrench, 0, new ModelResourceLocation(ModObjects.wrench.getRegistryName(), "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(ModObjects.itemblock_steampipe, 0, new ModelResourceLocation("lacroix:blocks/pipes/steampipe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.itemblock_energypipe, 0, new ModelResourceLocation("lacroix:blocks/pipes/energypipe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModObjects.itemblock_fluidpipe, 0, new ModelResourceLocation("lacroix:blocks/pipes/fluidpipe", "inventory"));
		
		ModObjects.itemblock_coalheater.initModels();
		ModObjects.itemblock_steamturbine.initModels();
		ModObjects.itemblock_carbonator.initModels();
		ModObjects.itemblock_canner.initModels();
	}
	
	public void registerFluids() {
		LaCroixFluid applecranberryfluid = new LaCroixFluid("applecranberryfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid apricotfluid = new LaCroixFluid("apricotfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid berryfluid = new LaCroixFluid("berryfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid blackberrycucumberfluid = new LaCroixFluid("blackberrycucumberfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid cherrylimefluid = new LaCroixFluid("cherrylimefluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid coconutfluid = new LaCroixFluid("coconutfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid cranberryraspberryfluid = new LaCroixFluid("cranberryraspberryfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid keylimefluid = new LaCroixFluid("keylimefluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid kiwiwatermelonfluid = new LaCroixFluid("kiwiwatermelonfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid lacolafluid = new LaCroixFluid("lacolafluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid lemonfluid = new LaCroixFluid("lemonfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid limefluid = new LaCroixFluid("limefluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid mangofluid = new LaCroixFluid("mangofluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid melongrapefruitfluid = new LaCroixFluid("melongrapefruitfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid orangefluid = new LaCroixFluid("orangefluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid pamplemoussefluid = new LaCroixFluid("pamplemoussefluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid passionfruitfluid = new LaCroixFluid("passionfruitfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid peachpearfluid = new LaCroixFluid("peachpearfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid pineapplefluid = new LaCroixFluid("pineapplefluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid purefluid = new LaCroixFluid("purefluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid strawberryfluid = new LaCroixFluid("strawberryfluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		LaCroixFluid tangerinefluid = new LaCroixFluid("tangerinefluid", new ResourceLocation(MODID, ""), new ResourceLocation(MODID, ""));
		
		FluidRegistry.registerFluid(applecranberryfluid);
		FluidRegistry.registerFluid(apricotfluid);
		FluidRegistry.registerFluid(berryfluid);
		FluidRegistry.registerFluid(blackberrycucumberfluid);
		FluidRegistry.registerFluid(cherrylimefluid);
		FluidRegistry.registerFluid(coconutfluid);
		FluidRegistry.registerFluid(cranberryraspberryfluid);
		FluidRegistry.registerFluid(keylimefluid);
		FluidRegistry.registerFluid(kiwiwatermelonfluid);
		FluidRegistry.registerFluid(lacolafluid);
		FluidRegistry.registerFluid(lemonfluid);
		FluidRegistry.registerFluid(limefluid);
		FluidRegistry.registerFluid(mangofluid);
		FluidRegistry.registerFluid(melongrapefruitfluid);
		FluidRegistry.registerFluid(orangefluid);
		FluidRegistry.registerFluid(pamplemoussefluid);
		FluidRegistry.registerFluid(passionfruitfluid);
		FluidRegistry.registerFluid(peachpearfluid);
		FluidRegistry.registerFluid(pineapplefluid);
		FluidRegistry.registerFluid(purefluid);
		FluidRegistry.registerFluid(strawberryfluid);
		FluidRegistry.registerFluid(tangerinefluid);
	}
}
