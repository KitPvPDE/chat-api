package net.kitpvp.chat.api;

import net.md_5.bungee.api.ChatColor;

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
}
