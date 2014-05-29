package uk.codingbadgers.Gui;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static org.lwjgl.opengl.GL11.glColor4f;

@SideOnly(Side.CLIENT)
public class GuiComboBox<T> extends Gui {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("bUpload:textures/gui/combobox.png");
    private static final int WIDTH = 160;
    private static final int HEIGHT = 20;

    private final int x;
    private final int y;
    private final String name;

    private ComboBoxChangeListener<T> listener;
    private boolean enabled = true;
    private boolean visible = true;
    private boolean mouseOver;

    private boolean popupOpen = false;
    private T selected = null;
    private List<T> items = Lists.newArrayList();

    public GuiComboBox(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void addItem(T item) {
        items.add(item);
    }

    public T getSelectedItem() {
        return selected;
    }

    public void setSelectedItem(T selected) {
        this.selected = selected;

        if (this.listener != null) {
            this.listener.onSelectedChanged(selected);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isPopupOpen() {
        return popupOpen;
    }

    public void setChangeListener(ComboBoxChangeListener<T> listener) {
        this.listener = listener;
    }

    protected int getHoverState(boolean isMouseOver) {
        if (!this.enabled) {
            return 0;
        }

        if (isMouseOver) {
            return 2;
        }

        return 1;
    }

    public void drawGuiComboBox(Minecraft minecraft, int mouseX, int mouseY) {
        if (!isVisible()) {
            return;
        }

        if (selected == null) {
            selected = this.items.get(0);
        }

        final int fontHeight = minecraft.fontRenderer.FONT_HEIGHT + 2;
        this.mouseOver = mouseX >= this.x + 140 && mouseY >= this.y + fontHeight && mouseX < this.x + WIDTH && mouseY < this.y + fontHeight + HEIGHT;
        final int hoverState = getHoverState(this.mouseOver);

        final int u = 0;
        final int v = hoverState == 1 ? 20 : (hoverState == 2 ? 40 : 0);
        final int hoverColor = hoverState == 1 ? 0xE0E0E0 : hoverState == 2 ? 0xFFFFAA : 0xA0A0A0;

        drawString(minecraft.fontRenderer, this.name, x, y, 0xE0E0E0);
        minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(x, y + fontHeight, u, v, WIDTH, HEIGHT);
        drawString(minecraft.fontRenderer, this.selected.toString(), x + 6, y + fontHeight + 6, hoverColor);
    }

    public void drawPopup(Minecraft mc, int mouseX, int mouseY) {
        if (!popupOpen) {
            return;
        }

        int hover = this.items.indexOf(selected);

        if (mouseX > this.x && mouseY > this.y + HEIGHT + 10 && mouseX < this.x + WIDTH && mouseY < this.y + (this.items.size() * HEIGHT) + HEIGHT + 10) {
            hover = (int) Math.floor((mouseY - (this.y + HEIGHT + 10)) / 20);
        }

        int v = 60;
        int xPos = this.x;
        int yPos = this.y + HEIGHT + mc.fontRenderer.FONT_HEIGHT + 2;
        final int hoverColor = 0xE0E0E0;

        for (int i = 0; i < this.items.size(); i++) {
            v = hover == i ? 80 : 60;

            mc.renderEngine.bindTexture(TEXTURE_LOCATION);
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexturedModalRect(xPos, yPos, 0, v, WIDTH, HEIGHT);
            drawString(mc.fontRenderer, this.items.get(i).toString(), xPos + 6, yPos + 6, hoverColor);

            yPos += HEIGHT;
        }
    }

    public boolean mouseClicked(int x, int y, int button) {
        if (button != 0 || !this.enabled) {
            return false;
        }

        if (x > this.x + 140 && y > this.y + 10 && x < this.x + WIDTH && y < this.y + HEIGHT + 10) {
            popupOpen = !popupOpen;
            return true;
        } else if (popupOpen) {
            if (x > this.x + HEIGHT && y > this.y && x < this.x + WIDTH && y < this.y + (this.items.size() * HEIGHT) + HEIGHT) {
                int item = (int) Math.floor((y - (this.y + HEIGHT)) / 20);
                setSelectedItem(this.items.get(item));
                popupOpen = false;
            } else {
                popupOpen = false;
            }
            return true;
        }

        return false;
    }

    public static interface ComboBoxChangeListener<T> {
        public void onSelectedChanged(T t);
    }
}
