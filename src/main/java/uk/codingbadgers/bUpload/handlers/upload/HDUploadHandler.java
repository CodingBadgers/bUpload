package uk.codingbadgers.bUpload.handlers.upload;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import uk.codingbadgers.bUpload.Screenshot;
import uk.codingbadgers.bUpload.UploadedImage;
import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.handlers.HistoryHandler;
import uk.codingbadgers.bUpload.handlers.MessageHandler;

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
					ImageIO.write(screenshot.image, ConfigHandler.IMAGE_FORMAT, outputFile);
				} catch (IOException e) {
					MessageHandler.sendChatMessage("image.upload.fail", e.getMessage());
					e.printStackTrace();
					return false;
				}

				ChatComponentTranslation message = new ChatComponentTranslation("image.upload.success");
				ChatComponentText url = new ChatComponentText("Disk");
				url.func_150255_a(new ChatStyle().func_150238_a(EnumChatFormatting.GOLD));
				message.func_150257_a(url);

				MessageHandler.sendChatMessage(message);
				HistoryHandler.addUploadedImage(new UploadedImage(outputFile.getParent(), path, screenshot, true));
				return true;
			}
		}
		return false;
	}

}
