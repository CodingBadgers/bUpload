package uk.codingbadgers.bUpload.handlers;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;

import net.minecraft.client.shader.Framebuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.BufferUtils;

import uk.codingbadgers.bUpload.handlers.upload.UploadType;
import uk.codingbadgers.bUpload.handlers.upload.UploadWorker;
import uk.codingbadgers.bUpload.image.Screenshot;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public class ScreenshotHandler {

    private static final Logger logger = LogManager.getLogger();

    private static final Marker SCREENSHOT = MarkerManager.getMarker("SCREENSHOT");
    private static final Marker SCREENSHOT_ERROR = MarkerManager.getMarker("SCREENSHOT_ERROR", SCREENSHOT);

    private static final Minecraft minecraft = Minecraft.getMinecraft();

	private static int[] PIXEL_ARRAY = null;
	private static IntBuffer PIXEL_BUFFER = null;

	public static Screenshot createScreenshot() {
		Screenshot shot = new Screenshot();
        Framebuffer frameBuffer = Minecraft.getMinecraft().getFramebuffer();
        boolean framebufferEnabled = OpenGlHelper.isFramebufferEnabled();
        logger.info(SCREENSHOT, "Frame buffer is " + (framebufferEnabled ? "Enabled" : "Disabled"));

		try {
            int width = minecraft.displayWidth;
            int height = minecraft.displayHeight;

            if (framebufferEnabled) {
                width = frameBuffer.framebufferWidth;
                height = frameBuffer.framebufferHeight;
            }

			int screenSize = width * height;

			if (PIXEL_BUFFER == null || PIXEL_BUFFER.capacity() < screenSize) {
				PIXEL_BUFFER = BufferUtils.createIntBuffer(screenSize);
				PIXEL_ARRAY = new int[screenSize];
			}

			glPixelStorei(GL_PACK_ALIGNMENT, 1);
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
			PIXEL_BUFFER.clear();

            if (framebufferEnabled) {
                glBindTexture(GL_TEXTURE_2D, frameBuffer.framebufferTexture);
                glGetTexImage(GL_TEXTURE_2D, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, PIXEL_BUFFER);
            } else {
                glReadPixels(0, 0, width, height, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, PIXEL_BUFFER);
            }

			PIXEL_BUFFER.get(PIXEL_ARRAY);
            TextureUtil.func_147953_a(PIXEL_ARRAY, width, height); // Should be copyScreenBuffer
            BufferedImage image = null;

            if (framebufferEnabled) {
                image = new BufferedImage(frameBuffer.framebufferWidth, frameBuffer.framebufferHeight, 1);
                int l = frameBuffer.framebufferTextureHeight - frameBuffer.framebufferHeight;

                for (int i1 = l; i1 < frameBuffer.framebufferTextureHeight; ++i1)
                {
                    for (int j1 = 0; j1 < frameBuffer.framebufferWidth; ++j1)
                    {
                        image.setRGB(j1, i1 - l, PIXEL_ARRAY[i1 * frameBuffer.framebufferTextureWidth + j1]);
                    }
                }
            } else {
                image = new BufferedImage(minecraft.displayWidth, minecraft.displayHeight, 1);
                image.setRGB(0, 0, minecraft.displayWidth, minecraft.displayHeight, PIXEL_ARRAY, 0, minecraft.displayWidth);
            }

            shot.image = image;
            shot.imageID = TextureUtil.uploadTextureImage(TextureUtil.glGenTextures(), shot.image);
		} catch (Exception ex) {
            MessageHandler.sendChatMessage("image.capture.fail", ex.getMessage());
            logger.warn(SCREENSHOT_ERROR, "Error capturing screenshot", ex);

			shot.image = null;
			shot.imageID = 0;
		}

		return shot;
	}

	public static void handleScreenshot() {
		Screenshot screen = createScreenshot();

        if (screen.image == null) {
            return;
        }

		boolean upload = false;

		if (ConfigHandler.SAVE_FILE) {
			runHandler(UploadType.FILE, screen);
			upload = true;
		}

		if (ConfigHandler.SAVE_IMGUR) {
			runHandler(UploadType.IMGUR, screen);
			upload = true;
		}

		if (ConfigHandler.SAVE_FTP) {
			runHandler(UploadType.FTP, screen);
			upload = true;
		}

		if (ConfigHandler.SAVE_TWITTER) {
			runHandler(UploadType.TWITTER, screen);
			upload = true;
		}

        if (ConfigHandler.SAVE_DROPBOX) {
            runHandler(UploadType.DROPBOX, screen);
            upload = true;
        }
		
		if (!upload) {
			MessageHandler.sendChatMessage("image.upload.noupload");
		}
	}

	private static void runHandler(UploadType type, Screenshot screen) {
        MessageHandler.sendChatMessage("image.upload.start", type.toString().toLowerCase());
        Thread thread = new Thread(new UploadWorker(type, screen));
		thread.start();
	}
}
