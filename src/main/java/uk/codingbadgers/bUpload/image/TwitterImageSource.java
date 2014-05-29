package uk.codingbadgers.bUpload.image;

import twitter4j.Status;
import uk.codingbadgers.bUpload.manager.TranslationManager;

import java.awt.*;
import java.net.URI;

public class TwitterImageSource extends ImageSource {

    private String url;
    private Status status;

    public TwitterImageSource(Status status) {
        super(ImageSourceType.TWITTER);
        this.status = status;
        this.url = "http://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId();
    }

    @Override
    public String getDescription() {
        return TranslationManager.getTranslation("image.history.twitter");
    }

    @Override
    public String getTooltip() {
        return TranslationManager.getTranslation("image.history.twitter.tooltip");
    }

    public String getUrl() {
        return url;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public void onClick() {
        try {
            Desktop dt = Desktop.getDesktop();
            dt.browse(URI.create(getUrl()));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

}
