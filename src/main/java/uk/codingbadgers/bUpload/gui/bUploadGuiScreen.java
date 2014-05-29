package uk.codingbadgers.bUpload.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class bUploadGuiScreen extends GuiScreen {

    private static final float SCALE = 0.00390625F;
    protected bUploadGuiScreen parent;

    public bUploadGuiScreen(bUploadGuiScreen screen) {
        this.parent = screen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
    }

    public void updateLogin() {
    }

    @SuppressWarnings("unchecked")
    public GuiButton addControl(GuiButton button) {
        this.buttonList.add(button);
        return button;
    }

    public void displayGuiScreen(GuiScreen screen) {
        Minecraft.getMinecraft().displayGuiScreen(screen);
    }

    public void drawBackground() {
        super.drawDefaultBackground();
    }

    public void drawTexturedModalRectSized(int x, int y, int u, int v, int width, int height, int uvwidth, int uvheight) {
        Tessellator quad = Tessellator.instance;
        GL11.glColor3f(1f, 1f, 1f);
        quad.startDrawingQuads();
        quad.addVertexWithUV((double) (x), (double) (y + height), (double) zLevel, (double) (u * SCALE), (double) ((v + uvheight) * SCALE));
        quad.addVertexWithUV((double) (x + width), (double) (y + height), (double) zLevel, (double) ((u + uvwidth) * SCALE), (double) ((v + uvheight) * SCALE));
        quad.addVertexWithUV((double) (x + width), (double) (y), (double) zLevel, (double) ((u + uvwidth) * SCALE), (double) (v * SCALE));
        quad.addVertexWithUV((double) (x), (double) (y), (double) zLevel, (double) (u * SCALE), (double) (v * SCALE));
        quad.draw();
    }

    public void resetGui() {
    }


}
