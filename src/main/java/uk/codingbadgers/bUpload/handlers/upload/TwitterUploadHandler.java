package uk.codingbadgers.bUpload.handlers.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import twitter4j.Status;
import twitter4j.StatusUpdate;

import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.handlers.HistoryHandler;
import uk.codingbadgers.bUpload.handlers.MessageHandler;
import uk.codingbadgers.bUpload.handlers.auth.TwitterAuthHandler;
import uk.codingbadgers.bUpload.image.Screenshot;
import uk.codingbadgers.bUpload.image.TwitterImageSource;
import uk.codingbadgers.bUpload.image.UploadedImage;

public class TwitterUploadHandler extends UploadHandler implements URLProvider {

    private URL url;

	public TwitterUploadHandler(Screenshot screen) {
		super(screen);
	}

	@Override
	protected boolean run(Screenshot screenshot) {
		try {
			String title = ConfigHandler.SAVE_DATE_FORMAT.format(new Date());
			String description = "A minecraft screenshot ";
		
			if (Minecraft.getMinecraft().isSingleplayer()) {
				description += "in " + Minecraft.getMinecraft().getIntegratedServer().getFolderName();
			} else {
				ServerData data = Minecraft.getMinecraft().func_147104_D();
				description += "on " + data.serverIP + (data.field_82821_f != 25565 ? ":" + data.field_82821_f : "");
			}
		
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(screenshot.image, "png", baos);
			ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
	
			StatusUpdate update = new StatusUpdate(description + " #bUpload");
			update.setMedia(title, in);
			
			Status status = TwitterAuthHandler.getInstance().getTwitterInstance().updateStatus(update);
			TwitterImageSource source = new TwitterImageSource(status);
            this.url = URI.create(source.getUrl()).toURL();
			HistoryHandler.addUploadedImage(new UploadedImage(title, "", screenshot, source));

            IChatComponent message = new ChatComponentTranslation("image.upload.success");
			IChatComponent url = new ChatComponentText("Twitter");
			IChatComponent tooltip = new ChatComponentTranslation("image.history.twitter")
												.setChatStyle(new ChatStyle()
															.setColor(EnumChatFormatting.AQUA));
			
			url.setChatStyle(new ChatStyle()
								.setColor(EnumChatFormatting.GOLD)
								.setBold(true)
								.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip))
								.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, source.getUrl())));
			
			message.appendSibling(url);

			MessageHandler.sendChatMessage(message);

			return true;
		} catch (Exception ex) {
			MessageHandler.sendChatMessage("image.upload.fail", "Twitter", "");
			MessageHandler.sendChatMessage(ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}

    @Override
    public URL getUrl() {
        return url;
    }
}
