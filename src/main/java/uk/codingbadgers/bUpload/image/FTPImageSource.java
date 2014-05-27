package uk.codingbadgers.bUpload.image;

import uk.codingbadgers.bUpload.manager.TranslationManager;

public class FTPImageSource extends ImageSource {

	public FTPImageSource() {
		super(ImageSourceType.FTP);
	}

	@Override
	public String getDescription() {
		return TranslationManager.getTranslation("image.history.ftp");
	}

    @Override
    public String getTooltip() {
        return null;
    }

    @Override
	public void onClick() {
	}

}
