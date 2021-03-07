package net.kitpvp.chat;

import lombok.RequiredArgsConstructor;
import net.kitpvp.chat.api.MsgFormat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.*;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class MultiLocaleComponentBuilder {

    private final MsgFormat format;
    private final Map<Locale, LocaleComponentBuilder> built = new HashMap<>();
    private final List<AbstractPart> parts = new LinkedList<>();

    public BaseComponent[][] create(Locale locale) {
        if(this.built.containsKey(locale)) {
            return this.built.get(locale).create();
        } else {
            LocaleComponentBuilder builder = new LocaleComponentBuilder(locale, this.format);
            this.parts.forEach(part -> part.apply(builder));
            this.built.put(locale, builder);
            return builder.create();
        }
    }

    public final MultiLocaleComponentBuilder newline() {
        this.parts.add(new NewlinePart());
        return this;
    }

    public final MultiLocaleComponentBuilder translate(String languageKey, Object... languageArgs) {
        return this.translate(languageKey, ComponentBuilder.FormatRetention.ALL, languageArgs);
    }

    public MultiLocaleComponentBuilder translate(String languageKey, ComponentBuilder.FormatRetention retention, Object... languageArgs) {
        this.parts.add(new I18nPart(languageKey, retention, languageArgs));
        return this;
    }

    public MultiLocaleComponentBuilder append(String text) {
        return this.append(text, ComponentBuilder.FormatRetention.ALL);
    }

    public MultiLocaleComponentBuilder append(String text, ComponentBuilder.FormatRetention retention) {
        this.parts.add(new TextPart(text, retention));
        return this;
    }

    public MultiLocaleComponentBuilder highlight() {
        return this.color(this.format.getHighlightColor());
    }

    public MultiLocaleComponentBuilder normal() {
        return this.color(this.format.getNormalColor());
    }

    public MultiLocaleComponentBuilder color(ChatColor color) {
        this.parts.add(new ParameterizedPart<>(LocaleComponentBuilder::color, color));
        return this;
    }

    public MultiLocaleComponentBuilder bold(boolean bold) {
        this.parts.add(new BooleanPart(LocaleComponentBuilder::bold, bold));
        return this;
    }

    public MultiLocaleComponentBuilder italic(boolean italic) {
        this.parts.add(new BooleanPart(LocaleComponentBuilder::italic, italic));
        return this;
    }

    public MultiLocaleComponentBuilder underlined(boolean underlined) {
        this.parts.add(new BooleanPart(LocaleComponentBuilder::underlined, underlined));
        return this;
    }

    public MultiLocaleComponentBuilder strikethrough(boolean strikethrough) {
        this.parts.add(new BooleanPart(LocaleComponentBuilder::strikethrough, strikethrough));
        return this;
    }

    public MultiLocaleComponentBuilder obfuscated(boolean obfuscated) {
        this.parts.add(new BooleanPart(LocaleComponentBuilder::obfuscated, obfuscated));
        return this;
    }

    public MultiLocaleComponentBuilder insertion(String insertion) {
        this.parts.add(new ParameterizedPart<>(LocaleComponentBuilder::insertion, insertion));
        return this;
    }

    public MultiLocaleComponentBuilder event(ClickEvent clickEvent) {
        this.parts.add(new ParameterizedPart<>(LocaleComponentBuilder::event, clickEvent));
        return this;
    }

    public MultiLocaleComponentBuilder event(HoverEvent hoverEvent) {
        this.parts.add(new ParameterizedPart<>(LocaleComponentBuilder::event, hoverEvent));
        return this;
    }

    public MultiLocaleComponentBuilder reset() {
        this.parts.clear();
        return this;
    }

    public MultiLocaleComponentBuilder retain(ComponentBuilder.FormatRetention retention) {
        this.parts.add(new ParameterizedPart<>(LocaleComponentBuilder::retain, retention));
        return this;
    }

    private abstract class AbstractPart {

        public abstract void apply(LocaleComponentBuilder builder);
    }

    @RequiredArgsConstructor
    private class BooleanPart extends AbstractPart {

        private final BooleanBinaryOperator<LocaleComponentBuilder> function;
        private final boolean value;

        @Override
        public void apply(LocaleComponentBuilder builder) {
            this.function.apply(builder, this.value);
        }
    }

    @FunctionalInterface
    private interface BooleanBinaryOperator<T> {

        T apply(T t, boolean bool);
    }

    @RequiredArgsConstructor
    private class ParameterizedPart<T> extends AbstractPart {

        private final BiFunction<LocaleComponentBuilder, T, LocaleComponentBuilder> function;
        private final T t;

        @Override
        public void apply(LocaleComponentBuilder builder) {
            this.function.apply(builder, this.t);
        }
    }

    private class NewlinePart extends AbstractPart {

        @Override
        public void apply(LocaleComponentBuilder builder) {
            builder.newline();
        }
    }

    @RequiredArgsConstructor
    private class TextPart extends AbstractPart {

        private final String text;
        private final ComponentBuilder.FormatRetention retention;

        @Override
        public void apply(LocaleComponentBuilder builder) {
            builder.append(this.text, this.retention);
        }
    }

    @RequiredArgsConstructor
    private class I18nPart extends AbstractPart {

        private final String languageKey;
        private final ComponentBuilder.FormatRetention retention;
        private final Object[] args;

        @Override
        public void apply(LocaleComponentBuilder builder) {
            builder.translate(this.languageKey, this.retention, this.args);
        }
    }

}
