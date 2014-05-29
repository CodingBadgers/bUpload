package uk.codingbadgers.bUpload.gui.edit;

import uk.codingbadgers.bUpload.gui.bUploadGuiScreen;
import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.manager.TranslationManager;

public class EditPathGui extends EditStringGui {
    public EditPathGui(bUploadGuiScreen parent) {
        super(parent, TranslationManager.getTranslation("image.settings.upload.path"), TranslationManager.getTranslation("image.edit.path.desc"));
    }

    @Override
    public void initGui() {
        super.initGui();
        textField.setText(ConfigHandler.SAVE_PATH);
    }

    @Override
    public void saveString(String string) {
        ConfigHandler.SAVE_PATH = string;
        ConfigHandler.save();
    }
}
