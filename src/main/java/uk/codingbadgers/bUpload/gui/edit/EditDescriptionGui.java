package uk.codingbadgers.bUpload.gui.edit;

import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.manager.TranslationManager;

public class EditDescriptionGui extends EditStringGui {
    public EditDescriptionGui(bUploadGuiScreen parent) {
        super(parent, TranslationManager.getTranslation("image.settings.upload.description"), TranslationManager.getTranslation("image.edit.description.desc"));
    }

    @Override
    public void initGui() {
        super.initGui();
        textField.setText(ConfigHandler.SAVE_DESCRIPTION);
    }

    @Override
    public void saveString(String string) {
        ConfigHandler.SAVE_DESCRIPTION = string;
        ConfigHandler.save();
    }
}
