package net.kitpvp.chat.api.player;

import net.kitpvp.chat.api.Connection;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Arrays;

public interface ChatConnection extends Connection {

    /**
     * Sends a message to this connection, encoded as an array of {@link BaseComponent}
     *
     * @param components the message to send
     *
     * @param messageType where to place this message according to {@link ChatMessageType}
     */
    void sendMessage(ChatMessageType messageType, BaseComponent... components);

    default void sendMessage(ChatMessageType messageType, BaseComponent component) {
        this.sendMessage(messageType, new BaseComponent[]{component});
    }

    default void sendMessage(ChatMessageType messageType, BaseComponent[][] arrays) {
        for(BaseComponent[] array : arrays) {
            sendMessage(messageType, array);
        }
    }
}
