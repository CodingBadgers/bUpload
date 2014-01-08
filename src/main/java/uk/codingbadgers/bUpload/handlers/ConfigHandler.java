package uk.codingbadgers.bUpload.handlers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigHandler {

	private static Configuration config;

	/* Internal config counter */
	public static int CONFIG_VERSION = 2;
	public static boolean COPY_URL_TO_CLIPBOARD = false;

	/* save */
	public static boolean SAVE_FTP = false;
	public static boolean SAVE_IMGUR = false;
	public static boolean SAVE_FILE = false;
	public static String SAVE_FORMAT = "";
	public static SimpleDateFormat SAVE_DATE_FORMAT = null;

	/* Auth */
	public static boolean ENCRYPT_DATA = true;

	/* keybindings */
	public static String KEYBIND_ADV_SS = "";
	public static String KEYBIND_HISTORY = "";

	/* image */
	public static String IMAGE_FORMAT = "";

	public static void loadConfig(File file) throws IOException {
		ConfigHandler.config = new Configuration(file);
		config.load();

		Property version = config.get(Configuration.CATEGORY_GENERAL, "version", 1);
		version.comment = "Do not change this value, it will reset your config";

		if (version.getInt() != CONFIG_VERSION) {
			FileUtils.copyFile(file, new File(file + ".outdated"));
			for (String string : config.getCategoryNames()) {
				config.removeCategory(config.getCategory(string));
			}
		}

		Property encrypt = config.get("auth", "encrypt", true);
		encrypt.comment = "This will leave your passwords open to attack, do not use unless you know what you are doing";

		COPY_URL_TO_CLIPBOARD = config.get(Configuration.CATEGORY_GENERAL, "copy-to-clipboard", false).getBoolean(false);

		SAVE_FTP = config.get("save", "ftp", false).getBoolean(false);
		SAVE_IMGUR = config.get("save", "imgur", false).getBoolean(false);
		SAVE_FILE = config.get("save", "file", false).getBoolean(false);
		SAVE_FORMAT = config.get("save", "format", "${player}/${mode}/${server}/${date}${extention}").getString();
		SAVE_DATE_FORMAT = new SimpleDateFormat(config.get("save", "dateformat", "yyyy-MM-dd_HH.mm.ss").getString());

		ENCRYPT_DATA = encrypt.getBoolean(true);

		KEYBIND_ADV_SS = config.get("keybindings", "advanced_screenshot", "F12").getString();
		KEYBIND_HISTORY = config.get("keybindings", "history", "EQUALS").getString();

		IMAGE_FORMAT = config.get("image", "format", "PNG").getString();
		save();
	}

	public static void save() {
		config.load();
		config.get(Configuration.CATEGORY_GENERAL, "version", 1).set(CONFIG_VERSION);
		config.get(Configuration.CATEGORY_GENERAL, "copy-to-clipboard", false).getBoolean(COPY_URL_TO_CLIPBOARD);

		config.get("save", "ftp", false).set(SAVE_FTP);
		config.get("save", "imgur", false).set(SAVE_IMGUR);
		config.get("save", "file", false).set(SAVE_FILE);
		config.get("save", "format", "${player}/${mode}/${server}/${date}${extention}").set(SAVE_FORMAT);
		config.get("save", "dateformat", "yyyy-MM-dd_HH.mm.ss").set(SAVE_DATE_FORMAT.toPattern());

		config.get("auth", "encrypt", true).set(ENCRYPT_DATA);

		config.get("keybindings", "advanced_screenshot", "F12").set(KEYBIND_ADV_SS);
		config.get("keybindings", "history", "EQUALS").set(KEYBIND_HISTORY);

		config.get("image", "format", "").set(IMAGE_FORMAT);
		config.save();
	}

	public static String formatImagePath(Minecraft minecraft) {
		String player = "";
		String mode = "";
		String server = "";
		String date = SAVE_DATE_FORMAT.format(new Date()).toString();
		;

		// for some reason player is null in the menu
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
				server = Minecraft.getMinecraft().func_147104_D().serverIP;
			}
		}

		String path = SAVE_FORMAT;
		path = path.replace('/', File.separatorChar);
		path = path.replace("${player}", player);
		path = path.replace("${mode}", mode);
		path = path.replace("${server}", server);
		path = path.replace("${date}", date);
		path = path.replace("${format}", SAVE_FORMAT.toLowerCase());
		path = path.replace("${extention}", "." + SAVE_FORMAT.toLowerCase());
		return path;
	}
}
