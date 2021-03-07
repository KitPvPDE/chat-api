package net.kitpvp.chat.i18n;

import lombok.RequiredArgsConstructor;
import net.kitpvp.network.translation.LocaleManager;
import net.kitpvp.network.translation.substitute.Substitution;
import org.jetbrains.annotations.Nls;

import java.util.Locale;

@RequiredArgsConstructor
public class I18n implements Substitution<String> {

    public static I18n translate(@Nls String languageKey, Object... args) {
        return new I18n(languageKey, args);
    }

    private final String languageKey;
    private final Object[] args;

    @Override
    public String replace(Locale locale, LocaleManager localeManager) {
        return localeManager.translate(locale, this.languageKey, this.args);
    }
}
