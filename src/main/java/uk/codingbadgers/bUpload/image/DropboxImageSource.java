package uk.codingbadgers.bUpload.image;

import com.dropbox.core.DbxEntry.File;
import uk.codingbadgers.bUpload.manager.TranslationManager;

public class DropboxImageSource extends ImageSource {

    public DropboxImageSource(File uploadedFile) {
        super(ImageSourceType.DROPBOX);
    }

    @Override
    public String getDescription() {
        return TranslationManager.getTranslation("image.history.dropbox");
    }

    @Override
    public String getTooltip() {
        return null;
    }

    @Override
    public void onClick() {
    }
}
