package uk.codingbadgers.bUpload.handlers.upload;

import net.minecraft.client.gui.GuiScreen;
import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.handlers.MessageHandler;
import uk.codingbadgers.bUpload.image.Screenshot;

public class UploadWorker implements Runnable {

    private final UploadType type;
    private final UploadHandler handler;

    public UploadWorker(UploadType type, Screenshot screenshot) {
        this.type = type;
        this.handler = type.newHandler(screenshot);
    }

    @Override
    public void run() {
        handler.run();

        if (type == ConfigHandler.SOURCE_TO_COPY) {
            GuiScreen.setClipboardString(((URLProvider) handler).getUrl().toExternalForm());
            MessageHandler.sendChatMessage("image.upload.copy", type.toString());
        }
    }
}
