package uk.codingbadgers.bUpload.handlers;

import uk.codingbadgers.bUpload.manager.TranslationManager;
import net.minecraft.client.Minecraft;
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
			message = TranslationManager.getTranslation(key);
		}

		if (mc == null || mc.thePlayer == null || mc.ingameGUI == null || mc.ingameGUI.getChatGUI() == null) {
			System.err.println(message);
			return;
		}

		ChatComponentText chat = new ChatComponentText("");
		ChatComponentText prefix = new ChatComponentText("[bUpload] ");
		prefix.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
		ChatComponentText main = new ChatComponentText(message);
		main.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
		chat.appendSibling(prefix);
		chat.appendSibling(main);

		mc.ingameGUI.getChatGUI().printChatMessage(chat);
	}

	public static void sendChatMessage(String key, Object... object) {
		sendChatMessage(key, true, object);
	}

	@Deprecated
	public static void sendChatMessage(String key, boolean translate, Object... object) {
		String message = key;

		if (translate) {
			message = TranslationManager.getTranslation(key, object);
		}

		if (mc == null || mc.thePlayer == null || mc.ingameGUI == null || mc.ingameGUI.getChatGUI() == null) {
			System.err.println(message);
			return;
		}

		ChatComponentText chat = new ChatComponentText("");
		ChatComponentText prefix = new ChatComponentText("[bUpload] ");
		prefix.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
		ChatComponentText main = new ChatComponentText(message);
		main.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
		chat.appendSibling(prefix);
		chat.appendSibling(main);
		
		mc.ingameGUI.getChatGUI().printChatMessage(chat);
	}

	public static void sendChatMessage(IChatComponent message) {

		ChatComponentText chat = new ChatComponentText("");
		ChatComponentText prefix = new ChatComponentText("[bUpload] ");
		prefix.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
		chat.appendSibling(prefix);
		chat.appendSibling(message);

		mc.ingameGUI.getChatGUI().printChatMessage(chat);
	}

}
