package net.kitpvp.chat.i18n;

import net.kitpvp.chat.Chat;
import net.kitpvp.chat.api.Connection;
import net.kitpvp.chat.api.MsgFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class I18n {

    public static void localeResponse(@NotNull Connection connection, @NotNull MsgFormat format,
                                      @NotNull @PropertyKey(resourceBundle = "") String languageKey, Object... args) {
        Chat.localeResponse(connection, format, languageKey, args);
    }

}
