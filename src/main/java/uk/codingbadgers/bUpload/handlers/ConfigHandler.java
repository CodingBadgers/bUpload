package uk.codingbadgers.bUpload.handlers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import uk.codingbadgers.bUpload.handlers.upload.UploadType;

public class ConfigHandler {

    private static Configuration config;

	/* Internal config counter */
	public static int CONFIG_VERSION = 5;
	public static boolean COPY_URL_TO_CLIPBOARD = false;
    public static UploadType SOURCE_TO_COPY = UploadType.IMGUR;

	/* save */
	public static boolean SAVE_FTP = false;
	public static boolean SAVE_IMGUR = false;
	public static boolean SAVE_FILE = false;
	public static boolean SAVE_TWITTER = false;
	public static boolean SAVE_DROPBOX = false;

	public static String SAVE_PATH = "";
	public static String SAVE_FORMAT = "";
    public static String SAVE_DESCRIPTION = "";
	public static SimpleDateFormat SAVE_DATE_FORMAT = null;

	/* Auth */
	public static boolean ENCRYPT_DATA = true;

	/* keybindings */
	public static String KEYBIND_ADV_SS = "";
	public static String KEYBIND_HISTORY = "";


	public static void loadConfig(File file) throws IOException {
		ConfigHandler.config = new Configuration(file);
		config.load();

		Property version = config.get(Configuration.CATEGORY_GENERAL, "version", CONFIG_VERSION);
		version.comment = "Do not change this value, it will reset your config";

		if (version.getInt() != CONFIG_VERSION) {
			FileUtils.copyFile(file, new File(file + ".out." + version.getInt()));
			for (String string : config.getCategoryNames()) {
				config.removeCategory(config.getCategory(string));
			}
			version.set(CONFIG_VERSION);
		}

		Property encrypt = config.get("auth", "encrypt", true);
		encrypt.comment = "This will leave your passwords open to attack, do not use unless you know what you are doing";

		COPY_URL_TO_CLIPBOARD = config.get(Configuration.CATEGORY_GENERAL, "copy-to-clipboard", false).getBoolean(false);
        SOURCE_TO_COPY = UploadType.valueOf(config.get(Configuration.CATEGORY_GENERAL, "source-to-copy", "IMGUR").getString().toUpperCase());

		SAVE_FTP = config.get("sources", "ftp", false).getBoolean(false);
		SAVE_IMGUR = config.get("sources", "imgur", false).getBoolean(false);
		SAVE_FILE = config.get("sources", "file", false).getBoolean(false);
		SAVE_TWITTER = config.get("sources", "twitter", false).getBoolean(false);
		SAVE_DROPBOX = config.get("sources", "dropbox", false).getBoolean(false);

		SAVE_PATH = config.get("save", "path", "${player}/${mode}/${server}/${date}${ext}").getString();
		SAVE_DESCRIPTION = config.get("save", "description", "A minecraft screenshot taken by ${player} in ${gamemode} on ${server} at ${date}").getString();
		SAVE_FORMAT = config.get("save", "format", "PNG").getString();
		SAVE_DATE_FORMAT = new SimpleDateFormat(config.get("save", "dateformat", "yyyy-MM-dd_HH.mm.ss").getString());

		ENCRYPT_DATA = encrypt.getBoolean(true);

		KEYBIND_ADV_SS = config.get("keybindings", "advanced_screenshot", "F12").getString();
		KEYBIND_HISTORY = config.get("keybindings", "history", "EQUALS").getString();

		save();
	}

	public static void save() {
		config.load();
		config.get(Configuration.CATEGORY_GENERAL, "version", CONFIG_VERSION).set(CONFIG_VERSION);
		config.get(Configuration.CATEGORY_GENERAL, "copy-to-clipboard", false).set(COPY_URL_TO_CLIPBOARD);
        config.get(Configuration.CATEGORY_GENERAL, "source-to-copy", 0).set(SOURCE_TO_COPY.name());

		config.get("sources", "ftp", false).set(SAVE_FTP);
		config.get("sources", "imgur", false).set(SAVE_IMGUR);
		config.get("sources", "file", false).set(SAVE_FILE);
		config.get("sources", "twitter", false).set(SAVE_TWITTER);
		config.get("sources", "dropbox", false).set(SAVE_DROPBOX);

		config.get("save", "path", "${player}/${mode}/${server}/${date}${ext}").set(SAVE_PATH);
		config.get("save", "description", "A minecraft screenshot taken by ${player} in ${gamemode} on ${server} at ${date}").set(SAVE_DESCRIPTION);
		config.get("save", "format", "PNG");
		config.get("save", "dateformat", "yyyy-MM-dd_HH.mm.ss").set(SAVE_DATE_FORMAT.toPattern());

		config.get("auth", "encrypt", true).set(ENCRYPT_DATA);

		config.get("keybindings", "advanced_screenshot", "F12").set(KEYBIND_ADV_SS);
		config.get("keybindings", "history", "EQUALS").set(KEYBIND_HISTORY);

		config.save();
	}

	public static String formatImagePath(Minecraft minecraft) {
		String player = "";
		String mode = "";
		String server = "";
		String date = SAVE_DATE_FORMAT.format(new Date()).toString();
		
		// player is null in the menu
		if (minecraft.thePlayer == null) {
			player = "";
			mode = "menu";
			server = "";
		} else {
			player = minecraft.thePlayer.getDisplayName();

			if (minecraft.isSingleplayer()) {
				mode = "single player";
				server = minecraft.getIntegratedServer().getFolderName();
			} else {
				mode = "multiplayer";
				server = Minecraft.getMinecraft().func_147104_D().serverName;
			}
		}

		String path = SAVE_PATH;
		path = path.replace('/', File.separatorChar);
		path = path.replace("${player}", player);
		path = path.replace("${mode}", mode);
		path = path.replace("${server}", server);
		path = path.replace("${date}", date);
		path = path.replace("${format}", SAVE_FORMAT.toLowerCase());
		path = path.replace("${ext}", "." + SAVE_FORMAT.toLowerCase());
		return "screenshots" + File.separatorChar + path;
	}
}
