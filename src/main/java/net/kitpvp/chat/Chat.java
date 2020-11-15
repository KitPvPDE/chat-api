package net.kitpvp.chat;

import net.kitpvp.chat.api.Connection;
import net.kitpvp.chat.api.MsgFormat;
import net.kitpvp.chat.api.player.ChatConnection;
import net.kitpvp.chat.utils.StringUtils;
import net.kitpvp.network.translation.LocaleManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Chat {

    public static final Pattern NEWLINE = Pattern.compile("[\n\r]");
    public static final String ALL_CODES = "mMnNpP";

    public static void commandResponse(Connection connection, MsgFormat format, String text) {
        commandResponse(connection, ChatMessageType.CHAT, format, text);
    }

    public static void commandResponse(Connection connection, ChatMessageType messageType, MsgFormat format, String text) {
        commandResponse(connection, format, messageType, text);
    }

    public static void commandResponse(Connection connection, MsgFormat format, String text, Object... args) {
        commandResponse(connection, format, ChatMessageType.CHAT, text, args);
    }

    public static void commandResponse(Connection connection, MsgFormat format, ChatMessageType messageType, String text, Object... args) {
        text = text.replace("{%m}", format.getHighlightColor().toString()).
                replace("{%n}", format.getNormalColor().toString());

        text = "" + format.buildPrefix() + text;
        try {
            text = new MessageFormat(text).format(args, new StringBuffer(), new FieldPosition(0)).toString();
        } catch(Throwable t) {
            t.printStackTrace();
        }
        String[] lines = NEWLINE.split(text);
        for(String line : lines) {
            if(connection instanceof ChatConnection) {
                ChatConnection chatConnection = (ChatConnection) connection;
                chatConnection.sendMessage(messageType, TextComponent.fromLegacyText(line));
            } else {
                connection.sendMessage(TextComponent.fromLegacyText(line));
            }
        }
    }

    public static void commandResponse(Connection connection, FormatComponentBuilder builder) {
        commandResponse(connection, ChatMessageType.CHAT, builder);
    }

    public static void commandResponse(Connection connection, ChatMessageType messageType, FormatComponentBuilder builder) {
        if(connection instanceof ChatConnection) {
            ChatConnection chatConnection = (ChatConnection) connection;
            chatConnection.sendMessage(messageType, builder.create());
        } else {
            connection.sendMessage(builder.create());
        }
    }

    public static void localeAnnounce(Iterable<? extends Connection> connections, LocaleComponentBuilder builder) {
        localeAnnounce(connections, ChatMessageType.CHAT, builder);
    }

    public static void localeAnnounce(Iterable<? extends Connection> connections, ChatMessageType messageType, LocaleComponentBuilder builder) {
        BaseComponent[][] text = builder.create();
        for(Connection connection : connections) {
            if(connection instanceof ChatConnection) {
                ChatConnection chatConnection = (ChatConnection) connection;
                chatConnection.sendMessage(messageType, text);
            } else {
                connection.sendMessage(text);
            }
        }
    }

    public static void localeAnnounce(Iterable<? extends Connection> connections, BaseComponent... components) {
        localeAnnounce(connections, ChatMessageType.CHAT, components);
    }

    public static void localeAnnounce(Iterable<? extends Connection> connections, ChatMessageType messageType, BaseComponent... components) {
        for(Connection connection : connections) {
            if(connection instanceof ChatConnection) {
                ChatConnection chatConnection = (ChatConnection) connection;
                chatConnection.sendMessage(messageType, components);
            } else {
                connection.sendMessage(components);
            }
        }
    }

    public static void localeAnnounce(Iterable<? extends Connection> connections, MsgFormat format, String translationKey, Object... args) {
        localeAnnounce(connections, format, ChatMessageType.CHAT, translationKey, args);
    }

    public static void localeAnnounce(Iterable<? extends Connection> connections, MsgFormat format, ChatMessageType messageType, String translationKey, Object... args) {
        Map<Locale, String> translations = new HashMap<>();
        for(Connection connection : connections) {
            Locale locale = connection.getLocale();

            String translatedText = translations.computeIfAbsent(locale, connectionLocale -> format(format, translate(connectionLocale, translationKey, args)));

            if(connection instanceof ChatConnection) {
                ChatConnection chatConnection = (ChatConnection) connection;
                chatConnection.sendMessage(messageType, translateComponents(connection, translatedText));
            } else {
                connection.sendMessage(translateComponents(connection, translatedText));
            }
        }
    }

    public static void localeResponse(Connection connection, LocaleComponentBuilder builder) {
        localeResponse(connection, ChatMessageType.CHAT, builder);
    }

    public static void localeResponse(Connection connection, ChatMessageType messageType, LocaleComponentBuilder builder) {
        if(connection instanceof ChatConnection) {
            ChatConnection chatConnection = (ChatConnection) connection;
            chatConnection.sendMessage(messageType, builder.create());
        } else {
            connection.sendMessage(builder.create());
        }
    }

    public static void localeResponse(Connection connection, BaseComponent... components) {
        localeResponse(connection, ChatMessageType.CHAT, components);
    }

    public static void localeResponse(Connection connection, ChatMessageType messageType, BaseComponent... components) {
        if(connection instanceof ChatConnection) {
            ChatConnection chatConnection = (ChatConnection) connection;
            chatConnection.sendMessage(messageType, components);
        } else {
            connection.sendMessage(components);
        }
    }

    public static void localeResponse(Connection connection, MsgFormat format, String translationKey, Object... args) {
        localeResponse(connection, ChatMessageType.CHAT, format, translationKey, args);
    }

    public static void localeResponse(Connection connection, ChatMessageType messageType, MsgFormat format, String translationKey, Object... args) {
        String translatedText = format(format, translate(connection, translationKey, args));

        if(connection instanceof ChatConnection) {
            ChatConnection chatConnection = (ChatConnection) connection;
            chatConnection.sendMessage(messageType, translateComponents(connection, translatedText));
        } else {
            connection.sendMessage(translateComponents(connection, translatedText));
        }
    }

    private static String format(MsgFormat format, String translatedText) {
        StringBuilder builder = new StringBuilder(format.buildPrefix());
        StringUtils.colorize(translatedText, builder, format);

        return builder.toString();
    }

    private static BaseComponent[][] translateComponents(Connection connection, String translatedText) {
        if(connection.supportsLineFeed()) {
            return new BaseComponent[][] {TextComponent.fromLegacyText(translatedText)};
        }
        String[] lines = translatedText.split("\n", -1);
        BaseComponent[][] texts = new BaseComponent[lines.length][];
        for(int i = 0; i < lines.length; i++) {
            texts[i] = TextComponent.fromLegacyText(lines[i]);
        }
        return texts;
    }

    private static String translate(Connection connection, String translationKey, Object... args) {
        Locale locale = connection.getLocale();
        return translate(locale, translationKey, args);
    }

    private static String translate(Locale locale, String translationKey, Object... args) {
        return LocaleManager.staticTranslate(locale, translationKey, args);
    }
}
