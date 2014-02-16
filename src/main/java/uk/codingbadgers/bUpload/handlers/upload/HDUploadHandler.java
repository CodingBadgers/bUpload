package uk.codingbadgers.bUpload.handlers.upload;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.handlers.HistoryHandler;
import uk.codingbadgers.bUpload.handlers.MessageHandler;
import uk.codingbadgers.bUpload.image.HDImageSource;
import uk.codingbadgers.bUpload.image.Screenshot;
import uk.codingbadgers.bUpload.image.UploadedImage;

public class HDUploadHandler extends UploadHandler {

	public HDUploadHandler(Screenshot screen) {
		super(screen);
	}

	@Override
	public boolean run(Screenshot screenshot) {
		Minecraft minecraft = Minecraft.getMinecraft();

		if (screenshot != null && screenshot.imageID != 0) {

			String path = ConfigHandler.formatImagePath(minecraft);
			File outputFile = new File(minecraft.mcDataDir, path);

			if (outputFile != null) {
				if (!outputFile.getParentFile().exists()) {
					outputFile.getParentFile().mkdirs();
				}

				try {
					ImageIO.write(screenshot.image, ConfigHandler.SAVE_FORMAT, outputFile);
				} catch (IOException e) {
					MessageHandler.sendChatMessage("image.upload.fail", e.getMessage());
					e.printStackTrace();
					return false;
				}

				IChatComponent message = new ChatComponentTranslation("image.upload.success");
				IChatComponent url = new ChatComponentText("Disk");
				
				url.setChatStyle(new ChatStyle()
									.setColor(EnumChatFormatting.GOLD)
									.setBold(true)
									.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentTranslation("image.disk.click")))
									.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, outputFile.getAbsolutePath())));
				
				message.appendSibling(url);

				MessageHandler.sendChatMessage(message);
				HistoryHandler.addUploadedImage(new UploadedImage(outputFile.getName().substring(0, outputFile.getName().length() - 4), path, screenshot, new HDImageSource(outputFile)));
				return true;
			}
		}
		return false;
	}

}
