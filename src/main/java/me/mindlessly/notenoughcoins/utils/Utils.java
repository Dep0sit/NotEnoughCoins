package me.mindlessly.notenoughcoins.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import gg.essential.universal.UChat;
import gg.essential.universal.wrappers.message.UTextComponent;
import me.mindlessly.notenoughcoins.Reference;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public class Utils {

	static JsonElement getJson(String jsonUrl) {
		try {
			URL url = new URL(jsonUrl);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Connection", "close");
			return new JsonParser().parse(new InputStreamReader(conn.getInputStream()));
		} catch (Exception e) {
			Reference.logger.error(e.getMessage(), e);
			return null;
		}
	}

	private static String formatValue(final long amount, final long div, final char suffix) {
		return new DecimalFormat(".##").format(amount / (double) div) + suffix;
	}

	public static String formatValue(final long amount) {
		if (amount >= 1_000_000_000_000_000L) {
			return formatValue(amount, 1_000_000_000_000_000L, 'q');
		} else if (amount >= 1_000_000_000_000L) {
			return formatValue(amount, 1_000_000_000_000L, 't');
		} else if (amount >= 1_000_000_000L) {
			return formatValue(amount, 1_000_000_000L, 'b');
		} else if (amount >= 1_000_000L) {
			return formatValue(amount, 1_000_000L, 'm');
		} else if (amount >= 100_000L) {
			return formatValue(amount, 1000L, 'k');
		}

		return NumberFormat.getInstance().format(amount);
	}

	public static void sendMessageWithPrefix(String message) {
		UChat.chat(EnumChatFormatting.GOLD + ("NEC ") + message.replaceAll("&", "§"));
	}

	public static void sendMessageWithPrefix(String message, ClickEvent clickEvent) {
		UTextComponent result = new UTextComponent(EnumChatFormatting.GOLD + ("NEC ") + message.replaceAll("&", "§"));
		result.setChatStyle(new ChatStyle().setChatClickEvent(clickEvent));
		UChat.chat(result);
	}

	public static void checkForUpdate() {
		String latestVersion = Utils.getJson("https://api.github.com/repos/mindlesslydev/NotEnoughCoins/releases")
				.getAsJsonArray().get(0).getAsJsonObject().get("tag_name").getAsString();
		if (!Objects.equals(latestVersion, Reference.VERSION)) {
			Utils.sendMessageWithPrefix(
					"&aAn update (" + latestVersion
							+ ") is available at https://github.com/mindlesslydev/NotEnoughCoins/releases",
					new ClickEvent(ClickEvent.Action.OPEN_URL,
							"https://github.com/mindlesslydev/NotEnoughCoins/releases"));
		}
	}
}
