package uk.codingbadgers.bUpload.image;

public abstract class ImageSource {

    private ImageSourceType type;

    public ImageSource(ImageSourceType id) {
        this.type = id;
    }

    public ImageSourceType getType() {
        return type;
    }

    public abstract String getDescription();

    public abstract void onClick();

}
