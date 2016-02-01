package com.firewallsol.smartcollege.Funciones;

import android.util.Base64;
import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created on 18/11/15.
 */
public class EmojiHandler {

    public static String decodeJava(String text) {
        return StringEscapeUtils.unescapeJava(unescapeJavaString(text));
    }

    public static String encodeJava(String text) {
        return StringEscapeUtils.escapeJava(text);
    }

    public static String unescapeJavaString(String st) {

        StringBuilder sb = new StringBuilder(st.length());

        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch == '\\') {
                char nextChar = (i == st.length() - 1) ? '\\' : st
                        .charAt(i + 1);
                // Octal escape?
                if (nextChar >= '0' && nextChar <= '7') {
                    String code = "" + nextChar;
                    i++;
                    if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                            && st.charAt(i + 1) <= '7') {
                        code += st.charAt(i + 1);
                        i++;
                        if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                                && st.charAt(i + 1) <= '7') {
                            code += st.charAt(i + 1);
                            i++;
                        }
                    }
                    sb.append((char) Integer.parseInt(code, 8));
                    continue;
                }
                switch (nextChar) {
                    case '\\':
                        ch = '\\';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
                    // Hex Unicode: u????
                    case 'u':
                        if (i >= st.length() - 5) {
                            ch = 'u';
                            break;
                        }
                        int code = Integer.parseInt(
                                "" + st.charAt(i + 2) + st.charAt(i + 3)
                                        + st.charAt(i + 4) + st.charAt(i + 5), 16);
                        sb.append(Character.toChars(code));
                        i += 5;
                        continue;
                }
                i++;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    public static String toUnicode(char c) {
        return String.format("\\u%04x", (int) c);
    }

    public String decodeHtml(String emoji) {
        return StringEscapeUtils.unescapeHtml3(emoji);
    }

    public String encodeHtml(String emoji) {
        return StringEscapeUtils.escapeHtml3(emoji);
    }

    public String base64Encode(String text) {
        try {
            byte[] data = text.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

    public String base64Decode(String text) {
        try {
            byte[] data = Base64.decode(text, Base64.DEFAULT);
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

    public String encodeToUNICODE(String text) {
        String encoding = "UNICODE";
        try {
            byte[] bytes = (text).getBytes();
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            Log.i("UE", encoding);
            return text;
        }
    }

    public String encodeToUSASCII(String text) {
        String encoding = "US-ASCII";
        try {
            byte[] bytes = (text).getBytes();
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            Log.i("UE", encoding);
            return text;
        }
    }

    public String encodeToUTF8(String text) {
        String encoding = "UTF-8";
        try {
            byte[] bytes = (text).getBytes();
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            Log.i("UE", encoding);
            return text;
        }
    }

    public String encodeToUTF16(String text) {
        String encoding = "UTF-16";
        try {
            byte[] bytes = (text).getBytes();
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            Log.i("UE", encoding);
            return text;
        }
    }
}
