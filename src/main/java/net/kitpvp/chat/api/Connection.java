package net.kitpvp.chat.api;

import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Arrays;
import java.util.Locale;

public interface Connection {

    /**
     * Sends a message to this connection, encoded as an array of {@link BaseComponent}
     *
     * @param components the message to send
     *
     */
    void sendMessage(BaseComponent... components);

    /**
     * Returns whether this connections supports line feeds in its messages,
     * 1.7 clients for example wont break messages properly.
     *
     * @return whether this connection supports line feeds like <code>\n</code>
     */
    boolean supportsLineFeed();

    /**
     * Check the locale of this connection.
     *
     * @return the {@link Locale} this connection is using
     */
    Locale getLocale();

    default void sendMessage(BaseComponent component) {
        sendMessage(new BaseComponent[]{component});
    }

    default void sendMessage(BaseComponent[][] arrays) {
        Arrays.stream(arrays).forEach(this::sendMessage);
    }
}
