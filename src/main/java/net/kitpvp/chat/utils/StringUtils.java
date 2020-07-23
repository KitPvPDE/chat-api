package net.kitpvp.chat.utils;

import net.kitpvp.chat.api.MsgFormat;

public class StringUtils {

    public static void colorize(String string, StringBuilder builder, MsgFormat format) {
        char[] c = string.toCharArray();
        int j;

        for(j = 2; j < c.length - 1; j++) {
            if((c[j - 2] == '<' && c[j - 1] == '$' && c[j + 1] == '>') ||
                    c[j - 2] == '{' && c[j - 1] == '%' && c[j + 1] == '}') {
                char k = c[j];
                switch(k) {
                    case 'M':
                    case 'm':
                        builder.append(format.getHighlightColor());
                        break;
                    case 'N':
                    case 'n':
                        builder.append(format.getNormalColor());
                        break;
                    case 'p':
                    case 'P':
                        builder.append(format.getPrefixColor());
                        break;
                    default:
                        builder.append(c[j - 2]);
                        continue;
                }

                j += 3;
                continue;
            }

            builder.append(c[j - 2]);
        }
        for( ; j < c.length + 2; j++) {
            builder.append(c[j - 2]);
        }
    }

}
