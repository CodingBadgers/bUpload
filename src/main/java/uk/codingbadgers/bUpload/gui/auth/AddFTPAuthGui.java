package uk.codingbadgers.bUpload.gui.auth;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import uk.codingbadgers.Gui.GuiPasswordField;
import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.handlers.auth.FTPAuthHandler;
import uk.codingbadgers.bUpload.handlers.auth.FTPAuthHandler.FTPUserData;
import uk.codingbadgers.bUpload.manager.TranslationManager;

public class AddFTPAuthGui extends AddAuthGui {

    private static final int ACCEPT = 0;
    private static final int CANCEL = 1;

    private GuiTextField userTxtBox;
    private GuiPasswordField passTxtBox;
    private GuiTextField hostTxtBox;
    private GuiTextField portTxtBox;

    public AddFTPAuthGui(bUploadGuiScreen screen) {
        super(screen);
    }

    @Override
    protected void keyTyped(char c, int i) {

        if (i == Keyboard.KEY_TAB) {
            if (userTxtBox.isFocused()) {
                updateFocus(passTxtBox);
            } else if (passTxtBox.isFocused()) {
                updateFocus(hostTxtBox);
            } else if (hostTxtBox.isFocused()) {
                updateFocus(portTxtBox);
            } else if (portTxtBox.isFocused()) {
                updateFocus(userTxtBox);
            } else {
                updateFocus(userTxtBox);
            }

            return;
        }

        if (userTxtBox.isFocused()) {
            userTxtBox.textboxKeyTyped(c, i);
        } else if (passTxtBox.isFocused()) {
            passTxtBox.textboxKeyTyped(c, i);
        } else if (hostTxtBox.isFocused()) {
            hostTxtBox.textboxKeyTyped(c, i);
        } else if (portTxtBox.isFocused()) {
            portTxtBox.textboxKeyTyped(c, i);
        }

        super.keyTyped(c, i);
    }

    private void updateFocus(GuiTextField field) {
        userTxtBox.setFocused(false);
        passTxtBox.setFocused(false);
        hostTxtBox.setFocused(false);
        portTxtBox.setFocused(false);

        field.setFocused(true);
    }

    private void updateFocus(GuiPasswordField field) {
        userTxtBox.setFocused(false);
        passTxtBox.setFocused(false);
        hostTxtBox.setFocused(false);
        portTxtBox.setFocused(false);

        field.setFocused(true);
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);

        this.userTxtBox.mouseClicked(par1, par2, par3);
        this.passTxtBox.mouseClicked(par1, par2, par3);
        this.hostTxtBox.mouseClicked(par1, par2, par3);
        this.portTxtBox.mouseClicked(par1, par2, par3);
    }

    @Override
    public void drawScreen(int i, int j, float k) {
        drawBackground();
        int textOffset = 24 / 2 - fontRendererObj.FONT_HEIGHT / 2;
        int ypos = (this.height / 4) - 12;

        drawCenteredString(this.fontRendererObj, EnumChatFormatting.UNDERLINE + TranslationManager.getTranslation("image.ftp.title"), this.width / 2, ypos, 0xFFFFFF);
        ypos += 24;
        fontRendererObj.drawString(TranslationManager.getTranslation("image.auth.username"), this.width / 2 - 108, ypos + textOffset, 0xFFFFFF);
        ypos += 24;
        fontRendererObj.drawString(TranslationManager.getTranslation("image.auth.password"), this.width / 2 - 108, ypos + textOffset, 0xFFFFFF);
        ypos += 24;
        fontRendererObj.drawString(TranslationManager.getTranslation("image.auth.host"), this.width / 2 - 108, ypos + textOffset, 0xFFFFFF);
        ypos += 24;
        fontRendererObj.drawString(TranslationManager.getTranslation("image.auth.port"), this.width / 2 - 108, ypos + textOffset, 0xFFFFFF);

        userTxtBox.drawTextBox();
        passTxtBox.drawTextBox();
        hostTxtBox.drawTextBox();
        portTxtBox.drawTextBox();
        super.drawScreen(i, j, k);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        switch (par1GuiButton.id) {
            case ACCEPT:
                FTPAuthHandler handler = FTPAuthHandler.getInstance();
                FTPUserData data = handler.getUserData();
                data.username = userTxtBox.getText();
                data.password = passTxtBox.getText().toCharArray();
                data.host = hostTxtBox.getText();
                data.port = Integer.parseInt(portTxtBox.getText());

                try {
                    handler.saveData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                displayGuiScreen(parent);
                break;
            case CANCEL:
                displayGuiScreen(parent);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        int buttonWidth = 160;
        int buttonHeight = 20;
        int ypos = height / 4;

        FTPUserData data = FTPAuthHandler.getInstance().getUserData();

        ypos += 12;
        userTxtBox = new GuiTextField(this.fontRendererObj, this.width / 2 - (buttonWidth - 108), ypos, buttonWidth, buttonHeight);
        userTxtBox.setText(data.username == null ? "" : data.username);
        ypos += 24;
        passTxtBox = new GuiPasswordField(fontRendererObj, this.width / 2 - (buttonWidth - 108), ypos, buttonWidth, buttonHeight);
        passTxtBox.setText(new String(data.password == null ? new char[0] : data.password));
        ypos += 24;
        hostTxtBox = new GuiTextField(fontRendererObj, this.width / 2 - (buttonWidth - 108), ypos, buttonWidth, buttonHeight);
        hostTxtBox.setText(data.host == null ? "" : data.host);
        ypos += 24;
        portTxtBox = new GuiTextField(fontRendererObj, this.width / 2 - (buttonWidth - 108), ypos, buttonWidth, buttonHeight);
        portTxtBox.setText("" + (data.port == -1 ? 21 : data.port));
        ypos += 48;

        buttonWidth = 100;
        this.buttonList.add(new GuiButton(ACCEPT, this.width / 2 - buttonWidth - 8, ypos, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.auth.accept")));
        this.buttonList.add(new GuiButton(CANCEL, this.width / 2 + 8, ypos, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.auth.cancel")));
    }

}
