package net.kitpvp.chat.api;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

public interface MsgFormat {

    String getPrefix();

    ChatColor getPrefixColor();

    ChatColor getHighlightColor();

    default ChatColor getNormalColor() {
        return ChatColor.GRAY;
    }

    default String formatPrefix() {
        return "" + getPrefixColor() + ChatColor.BOLD + getPrefix();
    }

    default String buildPrefix() {
        return formatPrefix() + " " + getNormalColor();
    }

    default ComponentBuilder prefixBuilder() {
        return new ComponentBuilder(this.getPrefix()).
                color(this.getPrefixColor()).
                bold(true).
                append(" ", ComponentBuilder.FormatRetention.NONE).
                color(this.getNormalColor());
    }
}
