package uk.codingbadgers.bUpload.image;

import uk.codingbadgers.bUpload.manager.TranslationManager;

import java.awt.*;
import java.net.URI;

public class ImgurImageSource extends ImageSource {

    private String url;

    public ImgurImageSource(String url) {
        super(ImageSourceType.IMGUR);
        this.url = url;
    }

    @Override
    public String getDescription() {
        return TranslationManager.getTranslation("image.history.imgur");
    }

    @Override
    public String getTooltip() {
        return TranslationManager.getTranslation("image.history.imgur.tooltip");
    }

    public String getUrl() {
        return url;
    }

    public void onClick() {
        try {
            Desktop dt = Desktop.getDesktop();
            dt.browse(URI.create(getUrl()));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
