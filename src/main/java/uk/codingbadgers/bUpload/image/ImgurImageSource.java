package uk.codingbadgers.bUpload.image;

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
        return url;
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
