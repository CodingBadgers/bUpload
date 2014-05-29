package uk.codingbadgers.bUpload.gui.auth;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.handlers.auth.TwitterAuthHandler;
import uk.codingbadgers.bUpload.manager.TranslationManager;

import java.io.IOException;

public class TwitterAuthConfirmScreen extends bUploadGuiScreen {

    private static final int LOG_OUT = 0;
    private static final int CANCEL = 1;

    private GuiScreen screen;

    public TwitterAuthConfirmScreen(bUploadGuiScreen auth, GuiScreen screen) {
        super(auth);
        this.screen = screen;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawBackground();
        drawCenteredString(this.fontRendererObj, EnumChatFormatting.UNDERLINE + TranslationManager.getTranslation("image.twitter.title"), this.width / 2, this.height / 6 + 30, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, TranslationManager.getTranslation("image.confirm.ln1", EnumChatFormatting.GOLD + TwitterAuthHandler.getInstance().getUsername()), this.width / 2, this.height / 6 + 42, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, TranslationManager.getTranslation("image.confirm.ln2"), this.width / 2, this.height / 6 + 54, 0xFFFFFF);
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case LOG_OUT:
                try {
                    TwitterAuthHandler.getInstance().forgetData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                this.parent.resetGui();
                displayGuiScreen(this.parent);
                break;
            case CANCEL:
                displayGuiScreen(screen);
                break;
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        int buttonWidth = 100;
        int buttonHeight = 20;

        addControl(new GuiButton(LOG_OUT, this.width / 2 - buttonWidth - 8, this.height / 6 + 96, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.confirm.logout")));
        addControl(new GuiButton(CANCEL, this.width / 2 + 8, this.height / 6 + 96, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.confirm.cancel")));
    }

}
