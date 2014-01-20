package uk.codingbadgers.bUpload.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class MessageHandler {

	private static final Minecraft mc = Minecraft.getMinecraft();

	public static void sendChatMessage(String message) {
		MessageHandler.sendChatMessage(message, true);
	}

	@Deprecated
	public static void sendChatMessage(String key, boolean translate) {
		String message = key;

		if (translate) {
			message = I18n.getStringParams(key);
		}

		if (mc == null || mc.thePlayer == null || mc.ingameGUI == null || mc.ingameGUI.func_146158_b() == null) {
			System.err.println(message);
			return;
		}

		ChatComponentText chat = new ChatComponentText("");
		ChatComponentText prefix = new ChatComponentText("[bUpload] ");
		prefix.func_150255_a(new ChatStyle().func_150238_a(EnumChatFormatting.GOLD));
		ChatComponentText main = new ChatComponentText(message);
		main.func_150255_a(new ChatStyle().func_150238_a(EnumChatFormatting.WHITE));
		chat.func_150257_a(prefix);
		chat.func_150257_a(main);

		mc.ingameGUI.func_146158_b().func_146227_a(chat);
	}

	public static void sendChatMessage(String key, Object... object) {
		sendChatMessage(key, true, object);
	}

	@Deprecated
	public static void sendChatMessage(String key, boolean translate, Object... object) {
		String message = key;

		if (translate) {
			message = I18n.getStringParams(key, object);
		}

		if (mc == null || mc.thePlayer == null || mc.ingameGUI == null || mc.ingameGUI.func_146158_b() == null) {
			System.err.println(message);
			return;
		}

		ChatComponentText chat = new ChatComponentText("");
		ChatComponentText prefix = new ChatComponentText("[bUpload] ");
		prefix.func_150255_a(new ChatStyle().func_150238_a(EnumChatFormatting.GOLD));
		ChatComponentText main = new ChatComponentText(message);
		main.func_150255_a(new ChatStyle().func_150238_a(EnumChatFormatting.GOLD));
		chat.func_150257_a(prefix);
		chat.func_150257_a(main);

		mc.ingameGUI.func_146158_b().func_146227_a(chat);
	}

	public static void sendChatMessage(IChatComponent message) {

		ChatComponentText chat = new ChatComponentText("");
		ChatComponentText prefix = new ChatComponentText("[bUpload] ");
		prefix.func_150255_a(new ChatStyle().func_150238_a(EnumChatFormatting.GOLD));
		chat.func_150257_a(prefix);
		chat.func_150257_a(message);

		mc.ingameGUI.func_146158_b().func_146227_a(chat);
	}

}
