package net.kitpvp.chat;

import net.kitpvp.chat.api.Connection;
import net.kitpvp.chat.api.MsgFormat;
import net.kitpvp.chat.utils.StringUtils;
import net.kitpvp.network.translation.LocaleManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.jetbrains.annotations.PropertyKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleComponentBuilder {

    private final List<BaseComponent[]> lines = new ArrayList<>();
    private final LocaleManager localeManager = LocaleManager.getInstance();
    private final Locale locale;
    private final boolean supportsLineFeed;
    private final MsgFormat format;

    private ComponentBuilder current;
    private boolean currentUsed;

    public LocaleComponentBuilder(Connection connection, MsgFormat format) {
        this.init(format);

        this.locale = connection.getLocale();
        this.supportsLineFeed = connection.supportsLineFeed();
        this.format = format;
    }

    public LocaleComponentBuilder(Locale locale, MsgFormat format) {
        this.init(format);

        this.locale = locale;
        this.supportsLineFeed = false;
        this.format = format;
    }

    public LocaleComponentBuilder(MsgFormat format) {
        this(LocaleManager.DEFAULT, format);
    }

    public BaseComponent[][] create() {
        if(this.currentUsed) {
            this.newline();
        }
        return this.lines.toArray(new BaseComponent[0][0]);
    }

    private void init(MsgFormat format) {
        this.current = format.prefixBuilder();
        this.currentUsed = true;
    }

    public LocaleComponentBuilder newline() {
        this.lines.add(this.current.create());
        this.current = new ComponentBuilder("").color(format.getNormalColor());
        this.currentUsed = false;
        return this;
    }

    public LocaleComponentBuilder translate(@PropertyKey(resourceBundle = "") String languageKey, Object... languageArgs) {
        return this.translate(languageKey, ComponentBuilder.FormatRetention.ALL, languageArgs);
    }

    public LocaleComponentBuilder translate(@PropertyKey(resourceBundle = "") String languageKey, ComponentBuilder.FormatRetention retention, Object... languageArgs) {
        String translation = this.localeManager.translate(this.locale, languageKey, languageArgs);
        StringBuilder builder = new StringBuilder();
        StringUtils.colorize(translation, builder, this.format);
        String text = builder.toString();

        return this.append(text, retention);
    }

    public LocaleComponentBuilder append(String text) {
        return this.append(text, ComponentBuilder.FormatRetention.ALL);
    }

    public LocaleComponentBuilder append(String text, ComponentBuilder.FormatRetention retention) {
        return append(Chat.NEWLINE.split(text), retention);
    }

    public LocaleComponentBuilder append(String[] lines) {
        return this.append(lines, ComponentBuilder.FormatRetention.ALL);
    }

    public LocaleComponentBuilder append(String[] lines, ComponentBuilder.FormatRetention retention) {
        boolean append = false;
        for(String line : lines) {
            if(append)
                this.newline();
            this.current.append(line, retention);
            append = true;
        }
        this.currentUsed = true;
        return this;
    }

    public LocaleComponentBuilder highlight() {
        return this.color(this.format.getHighlightColor());
    }

    public LocaleComponentBuilder normal() {
        return this.color(this.format.getNormalColor());
    }

    public LocaleComponentBuilder color(ChatColor color) {
        this.current.color(color);

        return this;
    }

    public LocaleComponentBuilder bold(boolean bold) {
        this.current.bold(bold);

        return this;
    }

    public LocaleComponentBuilder italic(boolean italic) {
        this.current.italic(italic);

        return this;
    }

    public LocaleComponentBuilder underlined(boolean underlined) {
        this.current.underlined(underlined);

        return this;
    }

    public LocaleComponentBuilder strikethrough(boolean strikethrough) {
        this.current.strikethrough(strikethrough);

        return this;
    }

    public LocaleComponentBuilder obfuscated(boolean obfuscated) {
        this.current.obfuscated(obfuscated);

        return this;
    }

    public LocaleComponentBuilder insertion(String insertion) {
        this.current.insertion(insertion);

        return this;
    }

    public LocaleComponentBuilder event(ClickEvent clickEvent) {
        this.current.event(clickEvent);

        return this;
    }

    public LocaleComponentBuilder event(HoverEvent hoverEvent) {
        this.current.event(hoverEvent);

        return this;
    }

    public LocaleComponentBuilder reset() {
        this.current.reset();

        return this;
    }

    public LocaleComponentBuilder retain(ComponentBuilder.FormatRetention retention) {
        this.current.retain(retention);

        return this;
    }
}
