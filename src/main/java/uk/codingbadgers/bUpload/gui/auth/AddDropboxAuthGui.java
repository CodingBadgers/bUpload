package uk.codingbadgers.bUpload.gui.auth;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuthNoRedirect;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.handlers.auth.DropboxAuthHandler;
import uk.codingbadgers.bUpload.manager.TranslationManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class AddDropboxAuthGui extends AddAuthGui {

    private static final int ACCEPT = 0;
    private static final int CANCEL = 1;
    private GuiTextField pinCode;
    private GuiButton btnAccept;
    private GuiButton btnCancel;

    private boolean linkGiven = false;
    private boolean busy = false;
    private boolean init = false;

    private DbxWebAuthNoRedirect authRequest = null;
    private String authUrl;

    public AddDropboxAuthGui(bUploadGuiScreen screen) {
        super(screen);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (!pinCode.isFocused()) {
            pinCode.setFocused(true);
        }

        pinCode.textboxKeyTyped(par1, par2);
        super.keyTyped(par1, par2);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case ACCEPT:
                busy = true;

                this.pinCode.setEnabled(false);
                this.btnAccept.enabled = false;
                this.btnCancel.enabled = false;

                Thread worker = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            getAccessToken();
                        } catch (Exception e) {
                            e.printStackTrace();
                            displayGuiScreen(new AuthSuccessScreen(parent, TranslationManager.getTranslation("image.dropbox.title"), TranslationManager.getTranslation("image.login.fail", TranslationManager.getTranslation("image.auth.type.dropbox"))));
                            return;
                        }

                        parent.updateLogin();
                    }

                });
                worker.start();

                break;
            case CANCEL:
                parent.updateLogin();
                displayGuiScreen(parent);
                break;
            default:
                break;
        }
    }

    private void getAccessToken() throws DbxException {
        DbxAuthFinish authFinish = authRequest.finish(pinCode.getText());

        DropboxAuthHandler.getInstance().setAccessToken(authFinish.accessToken);
        displayGuiScreen(new AuthSuccessScreen(parent, TranslationManager.getTranslation("image.dropbox.title"), TranslationManager.getTranslation("image.login.success", EnumChatFormatting.GOLD + DropboxAuthHandler.getInstance().getClient().getAccountInfo().displayName)));
    }

    @Override
    public void confirmClicked(boolean accepted, int par2) {
        if (accepted) {
            openLink();
            displayGuiScreen(this);
        } else {
            displayGuiScreen(parent);
        }
    }

    private void openLink() {
        try {
            Desktop dt = Desktop.getDesktop();
            dt.browse(URI.create(authUrl));
            linkGiven = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initGui() {
        if (DropboxAuthHandler.getInstance().isLoggedIn()) {
            displayGuiScreen(new DropboxAuthConfirmScreen(this, this.parent));
            return;
        }

        if (!linkGiven) {
            authRequest = new DbxWebAuthNoRedirect(DropboxAuthHandler.getInstance().getRequestConfig(), DropboxAuthHandler.getInstance().getAppInfo());
            authUrl = authRequest.start();

            if (this.mc.gameSettings.chatLinksPrompt) {
                displayGuiScreen(new GuiConfirmOpenLink(this, authUrl, 0, false));
            } else {
                openLink();
            }
        }

        int buttonWidth = 100;
        int buttonHeight = 20;

        btnAccept = addControl(new GuiButton(ACCEPT, this.width / 2 - buttonWidth - 8, this.height / 6 + 96, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.auth.accept")));
        btnCancel = addControl(new GuiButton(CANCEL, this.width / 2 + 8, this.height / 6 + 96, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.auth.cancel")));

        pinCode = new GuiTextField(this.fontRendererObj, this.width / 2 - buttonWidth - 8, this.height / 6 + 64, buttonWidth * 2 + 16, buttonHeight);
        pinCode.setMaxStringLength(43);
        init = true;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawBackground();

        drawCenteredString(this.fontRendererObj, EnumChatFormatting.UNDERLINE + TranslationManager.getTranslation("image.dropbox.title"), this.width / 2, this.height / 6 + 20, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, TranslationManager.getTranslation("image.login.ln1"), this.width / 2, this.height / 6 + 32, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, TranslationManager.getTranslation("image.login.ln2", TranslationManager.getTranslation("image.auth.type.dropbox")), this.width / 2, this.height / 6 + 44, 0xFFFFFF);

        pinCode.drawTextBox();
        super.drawScreen(par1, par2, par3);

        if (busy) {
            String message = EnumChatFormatting.BOLD + TranslationManager.getTranslation("image.auth.process").trim();
            int width = this.fontRendererObj.getStringWidth(message);

            int x = (this.width / 2) - ((width / 2) + 5);
            int y = (this.height / 6) + 66;

            Gui.drawRect(x, y, x + width + 10, y + (this.fontRendererObj.FONT_HEIGHT * 2) - 2, 0xBEBEBEFF);

            drawCenteredString(this.fontRendererObj, message, this.width / 2, y + 4, 0xFFFFFF);
        }
    }

    @Override
    public void resetGui() {
        if (!init) {
            return;
        }

        this.linkGiven = false;
        this.busy = false;

        this.pinCode.setEnabled(true);
        this.btnAccept.enabled = true;
        this.btnCancel.enabled = true;
    }
}
