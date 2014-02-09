package uk.codingbadgers.bUpload.handlers;

import uk.codingbadgers.bUpload.manager.TranslationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class MessageHandler {

	private static final Minecraft mc = Minecraft.getMinecraft();

	public static void sendChatMessage(String key) {
		
		if (mc == null || mc.thePlayer == null || mc.ingameGUI == null || mc.ingameGUI.getChatGUI() == null) {
			System.err.println(TranslationManager.getTranslation(key));
			return;
		}

		IChatComponent chat = new ChatComponentText("");
		IChatComponent prefix = new ChatComponentText("[bUpload] ");
		prefix.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
		IChatComponent main = new ChatComponentTranslation(key);
		main.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
		chat.appendSibling(prefix);
		chat.appendSibling(main);

		mc.ingameGUI.getChatGUI().printChatMessage(chat);
	}

	public static void sendChatMessage(String key, Object... elements) {
		
		if (mc == null || mc.thePlayer == null || mc.ingameGUI == null || mc.ingameGUI.getChatGUI() == null) {
			System.err.println(TranslationManager.getTranslation(key, elements));
			return;
		}

		IChatComponent chat = new ChatComponentText("");
		IChatComponent prefix = new ChatComponentText("[bUpload] ");
		prefix.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
		IChatComponent main = new ChatComponentTranslation(key, elements);
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
