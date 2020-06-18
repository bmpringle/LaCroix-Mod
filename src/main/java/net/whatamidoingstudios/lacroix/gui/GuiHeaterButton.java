package net.whatamidoingstudios.lacroix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.whatamidoingstudios.lacroix.LaCroix;

public class GuiHeaterButton extends GuiButton {
	static int width = 18;
	static int height = 18;
	static ResourceLocation HEATER_BUTTON_TEXTURES = new ResourceLocation(LaCroix.MODID, "textures/gui/widgets.png");
	
	private int u = 1;
	private int v = 1;
	
	private String text1 = "";
	private String text2 = "";
	public boolean textToggle = true;
	
	public GuiHeaterButton(int buttonId, int x, int y, String buttonText1, String buttonText2) {
		super(buttonId, x, y, width, height, "");
		text1 = buttonText1;
		text2 = buttonText2;		
	}
	
	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {
		textToggle = !textToggle;
		super.playPressSound(soundHandlerIn);
	}	
	
	public int getHovering(int mouseX, int mouseY) {
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        int i = this.getHoverState(this.hovered);
        return i;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(HEATER_BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      
            int i = getHovering(mouseX, mouseY);
            
            if(i < 2) {
            	v = 1;
            }else {
            	v = 21;
            }
            
            this.drawTexturedModalRect(this.x, this.y, u, v, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }
            
            if(textToggle) {
            	this.drawCenteredString(fontrenderer, text1, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
            }else {
            	this.drawCenteredString(fontrenderer, text2, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
            }
        }
    }
}
