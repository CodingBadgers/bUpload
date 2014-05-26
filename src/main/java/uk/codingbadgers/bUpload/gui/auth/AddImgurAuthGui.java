package uk.codingbadgers.bUpload.gui.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.handlers.auth.ImgurAuthHandler;
import uk.codingbadgers.bUpload.manager.TranslationManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class AddImgurAuthGui extends AddAuthGui {

    private static final String LOGIN_URL = "https://api.imgur.com/oauth2/authorize?client_id=" + ImgurAuthHandler.CLIENT_ID + "&response_type=pin&state=bUpload";
    private static final int ACCEPT = 0;
    private static final int CANCEL = 1;
    private GuiTextField pinCode;
    private GuiButton btnAccept;
    private GuiButton btnCancel;

    private boolean linkGiven = false;
    private boolean busy = false;
    private boolean init = false;

    public AddImgurAuthGui(bUploadGuiScreen parent) {
        super(parent);
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

    private void getAccessToken() throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost("https://api.imgur.com/oauth2/token");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("client_id", ImgurAuthHandler.CLIENT_ID));
        nameValuePairs.add(new BasicNameValuePair("client_secret", "d435f03cf62b7ec5589ae4f122354d4a435105d7"));
        nameValuePairs.add(new BasicNameValuePair("grant_type", "pin"));
        nameValuePairs.add(new BasicNameValuePair("pin", pinCode.getText()));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse resp = client.execute(post);
        String result = EntityUtils.toString(resp.getEntity());

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(result).getAsJsonObject();

        if (json.has("success") && !json.get("success").getAsBoolean()) {
            displayGuiScreen(new ImgurAuthSuccessScreen(parent, json.get("data").getAsJsonObject().get("error").getAsString()));
            return;
        }

        System.out.println(json);
        String refresh = json.get("refresh_token").getAsString();
        ImgurAuthHandler.getInstance().setTokens(refresh);
        displayGuiScreen(new ImgurAuthSuccessScreen(parent, TranslationManager.getTranslation("image.imgur.success", EnumChatFormatting.GOLD + ImgurAuthHandler.getInstance().getUsername())));
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
            dt.browse(URI.create(LOGIN_URL));
            linkGiven = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initGui() {
        if (ImgurAuthHandler.getInstance().isLoggedIn()) {
            displayGuiScreen(new ImgurAuthConfirmScreen(this, this.parent));
            return;
        }

        if (!linkGiven) {
            if (this.mc.gameSettings.chatLinksPrompt) {
                displayGuiScreen(new GuiConfirmOpenLink(this, LOGIN_URL, 0, false));
            } else {
                openLink();
            }
        }

        int buttonWidth = 100;
        int buttonHeight = 20;

        btnAccept = addControl(new GuiButton(ACCEPT, this.width / 2 - buttonWidth - 8, this.height / 6 + 96, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.auth.accept")));
        btnCancel = addControl(new GuiButton(CANCEL, this.width / 2 + 8, this.height / 6 + 96, buttonWidth, buttonHeight, TranslationManager.getTranslation("image.auth.cancel")));

        pinCode = new GuiTextField(this.fontRendererObj, this.width / 2 - buttonWidth - 8, this.height / 6 + 64, buttonWidth * 2 + 16, buttonHeight);
        init = true;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawBackground();

        drawCenteredString(this.fontRendererObj, EnumChatFormatting.UNDERLINE + TranslationManager.getTranslation("image.imgur.title"), this.width / 2, this.height / 6 + 20, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, TranslationManager.getTranslation("image.imgur.login.ln1"), this.width / 2, this.height / 6 + 32, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, TranslationManager.getTranslation("image.imgur.login.ln2"), this.width / 2, this.height / 6 + 44, 0xFFFFFF);

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
