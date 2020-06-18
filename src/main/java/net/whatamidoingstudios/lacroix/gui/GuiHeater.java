package net.whatamidoingstudios.lacroix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.whatamidoingstudios.lacroix.LaCroix;
import net.whatamidoingstudios.lacroix.block.EnumUpgrades;
import net.whatamidoingstudios.lacroix.block.heater.BlockHeater;
import net.whatamidoingstudios.lacroix.block.heater.ContainerHeater;
import net.whatamidoingstudios.lacroix.block.heater.TileEntityHeater;
import net.whatamidoingstudios.lacroix.network.heater.HeaterMessageServer;

public class GuiHeater extends GuiContainer {
	private InventoryPlayer playerInv;
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(LaCroix.MODID, "textures/gui/heater.png");
	public boolean serverSideTextToggle = true;
	public BlockPos blockpos;
	public float maxSteam = 10000;
	public float steam = 0;
	public String typename = "";
	
	public GuiHeater(Container container, InventoryPlayer playerInv) {
		super(container);
		this.playerInv = playerInv;
		typename = ((ContainerHeater)container).typeText;
		blockpos = ((ContainerHeater)container).pos;
	}
	
	public void modifyButtonInList(boolean toggle) {
		((GuiHeaterButton)this.buttonList.get(0)).textToggle = toggle;
	}
	
	@Override
	public void initGui() {
		this.buttonList.add(new GuiHeaterButton(0, 0, 0, "C", "R"));
		LaCroix.networkHandler.channel.sendToServer(new HeaterMessageServer(blockpos, serverSideTextToggle, true));
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		for(GuiButton _button : buttonList) {
			GuiHeaterButton button = (GuiHeaterButton)_button;
			button.x = guiLeft+50;
			button.y = guiTop+34;
			
			button.drawButton(mc, mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		for(GuiButton _button : buttonList) {
			GuiHeaterButton button = (GuiHeaterButton)_button;
			if(button.getHovering(mouseX, mouseY) == 2) {
				if(button.textToggle) {
					drawHoveringText("Collecting CO2 from burning", mouseX, mouseY);
				}else {
					drawHoveringText("Releasing CO2 from burning", mouseX, mouseY);
				}
				if(button.textToggle != serverSideTextToggle) {
					serverSideTextToggle = button.textToggle;
					LaCroix.networkHandler.channel.sendToServer(new HeaterMessageServer(blockpos, serverSideTextToggle, false));
				}
			}
		}
		
		this.renderHoveredToolTip(mouseX, mouseY);
		
		if(mouseX >= guiLeft+140 && mouseX < guiLeft+160) {
			if(mouseY >= guiTop+20 && mouseY < guiTop+73) {
				drawHoveringText((int)steam+"/"+(int)maxSteam+" steam", mouseX, mouseY);
			}
		}
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = I18n.format(new BlockHeater().getUnlocalizedName() + ".name");
		fontRenderer.drawString(typename + " " + name, xSize / 2 - (fontRenderer.getStringWidth(typename+" "+name)) / 2, 6, EnumUpgrades.mcFormatcolorOfToHex(EnumUpgrades.colorOf("", EnumUpgrades.safeValueOf(typename))));
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
		
		mc.getTextureManager().bindTexture(new ResourceLocation(LaCroix.MODID, "textures/gui/widgets.png"));
		int bars = (int)(steam/(maxSteam/50));
		
		if(steam > 0) {
			++bars;
		}
		int startY = 52-bars;
		int endY = 53-startY;
		
		drawTexturedModalRect(140, 20, 21, 0, 20, 53);
		drawTexturedModalRect(140, 20+startY, 42, startY, 20, endY);
	}

}
