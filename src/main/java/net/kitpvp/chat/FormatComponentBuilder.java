package net.kitpvp.chat;

import net.kitpvp.chat.api.MsgFormat;
import net.kitpvp.chat.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class FormatComponentBuilder {

    private final List<BaseComponent[]> lines = new ArrayList<>();
    private final MsgFormat format;

    private ComponentBuilder current;
    private boolean currentUsed;

    public FormatComponentBuilder(MsgFormat format, boolean prefix) {
        this.init(format, prefix);

        this.format = format;
    }

    public FormatComponentBuilder(MsgFormat format) {
        this(format, true);
    }

    public BaseComponent[][] create() {
        if(this.currentUsed){
            this.newline();
        }
        return this.lines.toArray(new BaseComponent[0][0]);
    }

    private void init(MsgFormat format, boolean prefix) {
        if(prefix) {
            this.current = new ComponentBuilder(format.getPrefix()).
                    color(format.getPrefixColor()).
                    bold(true).
                    append(" ", ComponentBuilder.FormatRetention.NONE).
                    color(format.getNormalColor());
        } else {
            this.current = new ComponentBuilder("").color(format.getNormalColor());
        }

        this.currentUsed = true;
    }

    public FormatComponentBuilder newline() {
        this.lines.add(this.current.create());
        this.current = new ComponentBuilder("").color(format.getNormalColor());
        this.currentUsed = false;
        return this;
    }

    public FormatComponentBuilder translate(String text, Object... textArgs) {
        return this.translate(text, ComponentBuilder.FormatRetention.ALL, textArgs);
    }

    public FormatComponentBuilder translate(String text, ComponentBuilder.FormatRetention retention, Object... textArgs) {
        text = new MessageFormat(text).format(textArgs, new StringBuffer(), new FieldPosition(0)).toString();
        StringBuilder builder = new StringBuilder();
        StringUtils.colorize(text, builder, this.format);
        text = builder.toString();

        return this.append(text, retention);
    }

    public FormatComponentBuilder append(String text) {
        return this.append(text, ComponentBuilder.FormatRetention.ALL);
    }

    public FormatComponentBuilder append(String text, ComponentBuilder.FormatRetention retention) {
        return append(Chat.NEWLINE.split(text), retention);
    }

    public FormatComponentBuilder append(String[] lines) {
        return this.append(lines, ComponentBuilder.FormatRetention.ALL);
    }

    public FormatComponentBuilder append(String[] lines, ComponentBuilder.FormatRetention retention) {
        boolean append = false;
        for(String line : lines){
            if(append)
                this.newline();
            this.current.append(line, retention);
            append = true;
        }
        this.currentUsed = true;
        return this;
    }

    public FormatComponentBuilder highlight() {
        return this.color(this.format.getHighlightColor());
    }

    public FormatComponentBuilder normal() {
        return this.color(this.format.getNormalColor());
    }

    public FormatComponentBuilder color(ChatColor color) {
        this.current.color(color);

        return this;
    }

    public FormatComponentBuilder bold(boolean bold) {
        this.current.bold(bold);

        return this;
    }

    public FormatComponentBuilder italic(boolean italic) {
        this.current.italic(italic);

        return this;
    }

    public FormatComponentBuilder underlined(boolean underlined) {
        this.current.underlined(underlined);

        return this;
    }

    public FormatComponentBuilder strikethrough(boolean strikethrough) {
        this.current.strikethrough(strikethrough);

        return this;
    }

    public FormatComponentBuilder obfuscated(boolean obfuscated) {
        this.current.obfuscated(obfuscated);

        return this;
    }

    public FormatComponentBuilder insertion(String insertion) {
        this.current.insertion(insertion);

        return this;
    }

    public FormatComponentBuilder event(ClickEvent clickEvent) {
        this.current.event(clickEvent);

        return this;
    }

    public FormatComponentBuilder event(HoverEvent hoverEvent) {
        this.current.event(hoverEvent);

        return this;
    }

    public FormatComponentBuilder reset() {
        this.current.reset();

        return this;
    }

    public FormatComponentBuilder retain(ComponentBuilder.FormatRetention retention) {
        this.current.retain(retention);

        return this;
    }

}
