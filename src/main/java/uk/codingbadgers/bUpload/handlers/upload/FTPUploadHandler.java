package uk.codingbadgers.bUpload.handlers.upload;

import com.google.common.base.Splitter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.handlers.HistoryHandler;
import uk.codingbadgers.bUpload.handlers.MessageHandler;
import uk.codingbadgers.bUpload.handlers.auth.FTPAuthHandler;
import uk.codingbadgers.bUpload.handlers.auth.FTPAuthHandler.FTPUserData;
import uk.codingbadgers.bUpload.image.FTPImageSource;
import uk.codingbadgers.bUpload.image.Screenshot;
import uk.codingbadgers.bUpload.image.UploadedImage;
import uk.codingbadgers.bUpload.manager.TranslationManager;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class FTPUploadHandler extends UploadHandler {

    private static final Minecraft minecraft = Minecraft.getMinecraft();

    public FTPUploadHandler(Screenshot screen) {
        super(screen);
    }

    @Override
    public boolean run(Screenshot screenshot) {
        FTPClient client = new FTPClient();

        try {
            FTPAuthHandler auth = FTPAuthHandler.getInstance();
            FTPUserData data = auth.getUserData();
            client.connect(data.host, data.port);

            int reply = client.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                throw new IOException(TranslationManager.getTranslation("image.upload.ftp.cannotconnect", reply));
            }

            if (!client.login(data.username, new String(data.password))) {
                client.logout();
                throw new IOException(TranslationManager.getTranslation("image.upload.ftp.incorrectlogin", auth.getUserData().username));
            }

            client.setListHiddenFiles(false);
            client.setFileType(FTP.BINARY_FILE_TYPE);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(screenshot.image, ConfigHandler.SAVE_FORMAT, os);
            InputStream fis = new ByteArrayInputStream(os.toByteArray());

            String path = ConfigHandler.formatImagePath();
            path = path.substring(0, path.lastIndexOf(File.separatorChar));
            navToDir(client, path);

            boolean uploaded = client.storeFile(ConfigHandler.SAVE_DATE_FORMAT.format(new Date()) + ".png", fis);

            if (uploaded) {
                ChatComponentTranslation message = new ChatComponentTranslation("image.upload.success");
                ChatComponentText url = new ChatComponentText("FTP server");
                url.setChatStyle(new ChatStyle().setBold(true).setColor(EnumChatFormatting.GOLD));
                message.appendSibling(url);

                MessageHandler.sendChatMessage(message);
                HistoryHandler.addUploadedImage(new UploadedImage(path.substring(path.lastIndexOf(File.separator), path.length() - 4), path, screenshot, new FTPImageSource()));
            } else {
                MessageHandler.sendChatMessage("image.upload.fail", "FTP server", client.getReplyString());
            }
        } catch (Exception e) {
            MessageHandler.sendChatMessage("image.upload.fail", "FTP server", e.getMessage());
            e.printStackTrace();
        } finally {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private void navToDir(FTPClient client, String dir) throws IOException {
        Splitter splitter = Splitter.on(File.separatorChar).omitEmptyStrings();

        for (String sting : splitter.split(dir)) {
            if (!client.changeWorkingDirectory(sting)) {
                client.makeDirectory(sting);
                client.changeWorkingDirectory(sting);
            }
        }
    }
}
