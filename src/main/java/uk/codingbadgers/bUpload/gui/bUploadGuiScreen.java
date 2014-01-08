package uk.codingbadgers.bUpload.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;

public class bUploadGuiScreen extends GuiScreen {

	protected bUploadGuiScreen parent;

	public bUploadGuiScreen(bUploadGuiScreen screen) {
		this.parent = screen;
	}

	protected void actionPerformed(GuiButton button) {
	}

	protected void updateLogin() {
	}

	@SuppressWarnings("unchecked")
	public void addControl(GuiButton button) {
		this.field_146292_n.add(button);
	}

	public void displayGuiScreen(GuiScreen screen) {
		Minecraft.getMinecraft().func_147108_a(screen);
	}

	@Override
	protected void func_146284_a(GuiButton button) {
		this.actionPerformed(button);
	}

	public void drawBackground() {
		func_146276_q_();
	}

	public void drawTexturedModalRectSized(int x, int y, int u, int v, int width, int height, int uvwidth, int uvheight) {
		final float var7 = 0.00390625F;
		final float var8 = 0.00390625F;
		Tessellator quad = Tessellator.instance;
		quad.startDrawingQuads();
		quad.addVertexWithUV((double) (x), (double) (y + height), (double) zLevel, (double) (u * var7), (double) ((v + uvheight) * var8));
		quad.addVertexWithUV((double) (x + width), (double) (y + height), (double) zLevel, (double) ((u + uvwidth) * var7), (double) ((v + uvheight) * var8));
		quad.addVertexWithUV((double) (x + width), (double) (y), (double) zLevel, (double) ((u + uvwidth) * var7), (double) (v * var8));
		quad.addVertexWithUV((double) (x), (double) (y), (double) zLevel, (double) (u * var7), (double) (v * var8));
		quad.draw();
	}


}
