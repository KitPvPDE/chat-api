package net.kitpvp.chat;

import net.kitpvp.chat.api.Connection;
import net.kitpvp.chat.api.MsgFormat;
import net.md_5.bungee.api.ChatMessageType;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;
import java.util.function.Function;

public class OfflineChat {

    public static void localeAnnounce(Iterable<UUID> uuids, Function<UUID, Connection> mapFunction, MsgFormat format, String languageKey, Object... args) {
        localeAnnounce(uuids, mapFunction, format, ChatMessageType.CHAT, languageKey, args);
    }

    public static void localeAnnounce(Iterable<UUID> uuids, Function<UUID, Connection> mapFunction, MsgFormat format, ChatMessageType messageType, String languageKey, Object... args) {
        Collection<Connection> connections = new LinkedList<>();
        for(UUID uuid : uuids) {
            Connection connection = mapFunction.apply(uuid);

            if(connection != null)
                connections.add(connection);
        }

        if(!connections.isEmpty()) {
            Chat.localeAnnounce(connections, format, messageType, languageKey, args);
        }
    }

}
