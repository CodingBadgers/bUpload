package uk.codingbadgers.bUpload.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import uk.codingbadgers.Gui.GuiCheckBox;
import uk.codingbadgers.Gui.GuiComboBox;
import uk.codingbadgers.Gui.GuiComboBox.ComboBoxChangeListener;
import uk.codingbadgers.bUpload.gui.edit.EditDescriptionGui;
import uk.codingbadgers.bUpload.gui.edit.EditPathGui;
import uk.codingbadgers.bUpload.handlers.ConfigHandler;
import uk.codingbadgers.bUpload.handlers.upload.UploadType;
import uk.codingbadgers.bUpload.manager.TranslationManager;

public class UploadSettingsGui extends bUploadGuiScreen {

    private static final int CANCEL = 1;
    private static final int COPY_TO_CLIPBOARD = 2;

    private static final int EDIT_PATH = 4;
    private static final int EDIT_DESCRIPTION = 5;
    private static final int EDIT_SOURCES = 6;

    private GuiCheckBox m_copyToClipboard;
    private GuiComboBox<UploadType> m_comboBox;

    public UploadSettingsGui(bUploadGuiScreen parent) {
        super(parent);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        if (m_comboBox.mouseClicked(x, y, button)) {
            return;
        }

        super.mouseClicked(x, y, button);
    }

    @Override
    public void initGui() {
        int ypos = (height / 5) - 2;
        int buttonWidth = 160;

        m_copyToClipboard = new GuiCheckBox(COPY_TO_CLIPBOARD, width / 2 - (buttonWidth / 2), ypos, buttonWidth, 20, TranslationManager.getTranslation("image.options.copy"));
        m_copyToClipboard.setChecked(ConfigHandler.COPY_URL_TO_CLIPBOARD);
        addControl(m_copyToClipboard);
        ypos += 26;

        m_comboBox = new GuiComboBox<UploadType>(width / 2 - (buttonWidth / 2), ypos, "Copy url from");
        m_comboBox.setChangeListener(new ComboBoxChangeListener<UploadType>() {
            @Override
            public void onSelectedChanged(UploadType uploadType) {
                ConfigHandler.SOURCE_TO_COPY = uploadType;
                ConfigHandler.save();
            }
        });

        for (int i = 0; i < UploadType.values().length; i++) {
            UploadType type = UploadType.getById(i);
            if (!type.providesUrl()) {
                continue;
            }

            m_comboBox.addItem(type);

            if (type == ConfigHandler.SOURCE_TO_COPY) {
                m_comboBox.setSelectedItem(type);
            }
        }

        ypos += 48;

        addControl(new GuiButton(EDIT_PATH, width / 2 - (buttonWidth / 2), ypos, buttonWidth, 20, TranslationManager.getTranslation("image.settings.upload.path")));
        ypos += 24;

        addControl(new GuiButton(EDIT_DESCRIPTION, width / 2 - (buttonWidth / 2), ypos, buttonWidth, 20, TranslationManager.getTranslation("image.settings.upload.description")));
        ypos += 24;

        addControl(new GuiButton(EDIT_SOURCES, width / 2 - (buttonWidth / 2), ypos, buttonWidth, 20, TranslationManager.getTranslation("image.settings.upload.source")));
        ypos = (height / 5) * 4;

        addControl(new GuiButton(CANCEL, width / 2 - (buttonWidth / 2), ypos, buttonWidth, 20, TranslationManager.getTranslation("image.settings.cancel")));
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        drawBackground();
        drawCenteredString(this.fontRendererObj, EnumChatFormatting.UNDERLINE + TranslationManager.getTranslation("image.settings.upload.title"), width / 2, height / 5 - 20, 0xffffff);
        m_comboBox.drawGuiComboBox(this.mc, i, j);
        super.drawScreen(i, j, f);
        m_comboBox.drawPopup(this.mc, i, j);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case COPY_TO_CLIPBOARD: {
                ConfigHandler.COPY_URL_TO_CLIPBOARD = m_copyToClipboard.getChecked();
                updatedSettings();
                break;
            }

            case EDIT_PATH: {
                displayGuiScreen(new EditPathGui(this));
                break;
            }

            case EDIT_DESCRIPTION: {
                displayGuiScreen(new EditDescriptionGui(this));
                break;
            }

            case CANCEL: {
                displayGuiScreen(this.parent);
                break;
            }
        }
    }

    private void updatedSettings() {
        ConfigHandler.save();
    }
}
