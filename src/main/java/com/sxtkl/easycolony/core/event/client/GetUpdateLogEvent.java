package com.sxtkl.easycolony.core.event.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sxtkl.easycolony.Easycolony;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GetUpdateLogEvent {
    private static final String UPDATE_URL = "https://sxtkl123.github.io/EasyColony/latest.json";
    private static final int HTTP_TIMEOUT_MS = 5000;
    private static final String FALLBACK_LANGUAGE = "zh_cn";

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        checkAndSendUpdateMessage(player);
    }

    private static void checkAndSendUpdateMessage(Player player) {
        final String modVersion = getModVersion();

        CompletableFuture.runAsync(() -> {
            try {
                JsonObject responseJson = fetchUpdateInfo();
                if (responseJson == null) return;
                processUpdateInfo(player, modVersion, responseJson);
            } catch (Exception e) {
                Easycolony.LOGGER.error("[Update Check] Failed to process update info", e);
            }
        });
    }

    private static String getModVersion() {
        return ModList.get()
                .getModContainerById(Easycolony.MODID)
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("UNKNOWN");
    }

    @Nullable
    private static JsonObject fetchUpdateInfo() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(UPDATE_URL).openConnection();
            configureConnection(connection);

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Easycolony.LOGGER.warn("[Update Check] HTTP {}: {}", connection.getResponseCode(), UPDATE_URL);
                return null;
            }

            return parseResponse(connection);
        } catch (IOException e) {
            Easycolony.LOGGER.error("[Update Check] Network error", e);
            return null;
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private static void configureConnection(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(HTTP_TIMEOUT_MS);
        connection.setReadTimeout(HTTP_TIMEOUT_MS);
    }

    private static JsonObject parseResponse(HttpURLConnection connection) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            Easycolony.LOGGER.error("[Update Check] Invalid JSON response", e);
            return null;
        }
    }

    private static void processUpdateInfo(Player player, String currentVersion, JsonObject responseJson) {
        if (!isValidResponse(responseJson)) return;

        String latestVersion = responseJson.get("version").getAsString();
        if (currentVersion.equals(latestVersion)) return;

        JsonObject content = responseJson.getAsJsonObject("content");
        String message = getLocalizedMessage(content);
        if (message == null) return;

        sendClientMessage(player, message, latestVersion);
    }

    private static boolean isValidResponse(JsonObject json) {
        if (!json.has("version") || !json.has("content")) {
            Easycolony.LOGGER.error("[Update Check] Missing required JSON fields");
            return false;
        }
        return true;
    }

    @Nullable
    private static String getLocalizedMessage(JsonObject content) {
        String language = getClientLanguage();

        if (content.has(language)) {
            return content.get(language).getAsString();
        }
        if (content.has(FALLBACK_LANGUAGE)) {
            return content.get(FALLBACK_LANGUAGE).getAsString();
        }

        Easycolony.LOGGER.warn("[Update Check] No valid messages found");
        return null;
    }

    private static String getClientLanguage() {
        return Minecraft.getInstance().getLanguageManager().getSelected();
    }

    private static void sendClientMessage(Player player, String message, String version) {
        Minecraft.getInstance().execute(() -> {
            if (player.isAlive()) {
                Component updateMessage = Component.translatable("com.sxtkl.easycolony.event.update_log.latest", version).withStyle(ChatFormatting.GREEN);
                player.sendSystemMessage(updateMessage);
                Component updateContent = Component.literal(message).withStyle(ChatFormatting.WHITE);
                player.sendSystemMessage(updateContent);
            }
        });
    }
}
