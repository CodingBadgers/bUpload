package uk.codingbadgers.bUpload.gui.edit;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.manager.TranslationManager;

public abstract class EditStringGui extends bUploadGuiScreen {

    private static final int OK = 1;
    private static final int CANCEL = 2;

    private final String title;
    private final String description;

    protected GuiTextField textField;

    public EditStringGui(bUploadGuiScreen parent, String title, String description) {
        super(parent);

        this.title = title;
        this.description = description;
    }

    public static interface EditStringCallback {
        public void run(String string);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case OK: {
                saveString(textField.getText());
                displayGuiScreen(this.parent);
                break;
            }

            case CANCEL: {
                displayGuiScreen(this.parent);
                break;
            }
        }
    }

    @Override
    public void initGui() {
        int yPos = (height / 5) + 24;
        int buttonWidth = 300;

        textField = new GuiTextField(mc.fontRenderer, this.width / 2 - (buttonWidth / 2), yPos, buttonWidth, 20);
        textField.setMaxStringLength(Integer.MAX_VALUE);
        textField.setFocused(true);
        textField.setCanLoseFocus(false);

        yPos = (height / 5) * 4;
        buttonWidth = 145;
        addControl(new GuiButton(OK, (this.width / 2) - buttonWidth - 5, yPos, buttonWidth, 20, TranslationManager.getTranslation("image.settings.ok")));
        addControl(new GuiButton(CANCEL, (this.width / 2) + 5, yPos, buttonWidth, 20, TranslationManager.getTranslation("image.settings.cancel")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        this.drawBackground();
        super.drawScreen(mouseX, mouseY, par3);

        this.drawCenteredString(mc.fontRenderer, EnumChatFormatting.UNDERLINE + this.title, this.width / 2, this.height / 5, -1);
        textField.drawTextBox();
        drawDescription();
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (!textField.isFocused()) {
            textField.setFocused(true);
        }

        textField.textboxKeyTyped(par1, par2);
        super.keyTyped(par1, par2);
    }

    public abstract void saveString(String string);

    public void drawDescription() {
        int x = (this.width / 2);
        int y = ((this.height / 5) * 2) + 5;
        drawCenteredString(mc.fontRenderer, description, x, y, -1);

        x = (this.width / 2) - 150;
        y = ((this.height / 5) * 2) + 24;
        int xOffset = mc.fontRenderer.getStringWidth("${gamemode} ");

        drawString(mc.fontRenderer, "${player} ", x, y, -1);
        drawString(mc.fontRenderer, "- " + TranslationManager.getTranslation("image.edit.macro.player"), x + xOffset, y, -1);
        y += 14;
        drawString(mc.fontRenderer, "${gamemode} ", x, y, -1);
        drawString(mc.fontRenderer, "- " + TranslationManager.getTranslation("image.edit.macro.gamemode"), x + xOffset, y, -1);
        y += 14;
        drawString(mc.fontRenderer, "${server} ", x, y, -1);
        drawString(mc.fontRenderer, "- " + TranslationManager.getTranslation("image.edit.macro.server"), x + xOffset, y, -1);

        x = (this.width / 2) + 10;
        y = ((this.height / 5) * 2) + 24;
        xOffset = mc.fontRenderer.getStringWidth("${format} ");

        drawString(mc.fontRenderer, "${date} ", x, y, -1);
        drawString(mc.fontRenderer, "- " + TranslationManager.getTranslation("image.edit.macro.date"), x + xOffset, y, -1);
        y += 14;
        drawString(mc.fontRenderer, "${format} ", x, y, -1);
        drawString(mc.fontRenderer, "- " + TranslationManager.getTranslation("image.edit.macro.format"), x + xOffset, y, -1);
        y += 14;
        drawString(mc.fontRenderer, "${ext} ", x, y, -1);
        drawString(mc.fontRenderer, "- " + TranslationManager.getTranslation("image.edit.macro.ext"), x + xOffset, y, -1);
    }

}
