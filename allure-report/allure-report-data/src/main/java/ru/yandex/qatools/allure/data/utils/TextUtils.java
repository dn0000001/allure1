package ru.yandex.qatools.allure.data.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.13
 */
public final class TextUtils {

    private TextUtils() {
    }

    private static final String ALGORITHM = "MD5";

    private static final String CHARSET = "UTF-8";

    private static final Integer RADIX = 16;

    public static String generateUid(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(s.getBytes(CHARSET));
        return new BigInteger(1, md.digest()).toString(RADIX);
    }

    public static String humanize(String text) {
        String result = text.trim();
        result = result.replaceAll(".*\\.([^.^0-9]+)", "$1");
        result = result.replaceAll("((.*\\D)|(^))\\.([0-9][^.]+)", "$4");
        result = splitCamelCase(result);
        result = result.replaceAll("(_)+", " ");
        result = underscoreCapFirstWords(result);
        result = capitalize(result);

        return result;
    }

    public static String capitalize(String text) {
        if (text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    public static String underscoreCapFirstWords(String text) {
        Matcher matcher = Pattern.compile("(^|\\w|\\s)([A-Z]+)([a-z]+)").matcher(text);

        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, matcher.group().toLowerCase());
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    public static String splitCamelCase(String camelCaseString) {
        return camelCaseString.replaceAll(
                String.format("%s|%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[a-z0-9])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[0-9])",
                        "(?<=[A-Za-z0-9])(?=[\\[])"
                ),
                "_"
        );
    }
}
