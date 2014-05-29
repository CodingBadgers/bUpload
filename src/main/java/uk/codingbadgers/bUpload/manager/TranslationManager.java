package uk.codingbadgers.bUpload.manager;

import net.minecraft.client.resources.I18n;

public class TranslationManager {

    public static String getTranslation(String key, Object... parts) {
        return I18n.format(key, parts);
    }

}
