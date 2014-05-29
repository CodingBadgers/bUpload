package uk.codingbadgers.bUpload.handlers.upload;

import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxWriteMode;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.handlers.HistoryHandler;
import uk.codingbadgers.bUpload.handlers.MessageHandler;
import uk.codingbadgers.bUpload.handlers.auth.DropboxAuthHandler;
import uk.codingbadgers.bUpload.image.DropboxImageSource;
import uk.codingbadgers.bUpload.image.Screenshot;
import uk.codingbadgers.bUpload.image.UploadedImage;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

public class DropboxUploadHandler extends UploadHandler {

    public DropboxUploadHandler(Screenshot screen) {
        super(screen);
    }

    @Override
    protected boolean run(Screenshot screenshot) {
        try {
            String title = ConfigHandler.SAVE_DATE_FORMAT.format(new Date());
            String path = "/" + ConfigHandler.formatImagePath().replace(File.separatorChar, '/');

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenshot.image, "png", baos);
            ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());

            DbxEntry.File uploadedFile = DropboxAuthHandler.getInstance().getClient().uploadFile(path, DbxWriteMode.add(), in.available(), in);

            DropboxImageSource source = new DropboxImageSource(uploadedFile);
            HistoryHandler.addUploadedImage(new UploadedImage(title, "", screenshot, source));

            IChatComponent message = new ChatComponentTranslation("image.upload.success");
            IChatComponent url = new ChatComponentText("Dropbox");
            IChatComponent tooltip = new ChatComponentTranslation("image.history.dropbox")
                    .setChatStyle(new ChatStyle()
                            .setColor(EnumChatFormatting.AQUA));

            url.setChatStyle(new ChatStyle()
                    .setColor(EnumChatFormatting.GOLD)
                    .setBold(true)
                    .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip)));

            message.appendSibling(url);

            MessageHandler.sendChatMessage(message);

            return true;
        } catch (Exception ex) {
            MessageHandler.sendChatMessage("image.upload.fail", "Dropbox", "");
            MessageHandler.sendChatMessage(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}
