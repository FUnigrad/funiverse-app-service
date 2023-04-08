package com.unigrad.funiverseappservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static List<Long> extractUserFromContent(String content) {
        Pattern pattern = Pattern.compile("@(.*?)@");
        Matcher matcher = pattern.matcher(content);

        List<Long> userIds = new ArrayList<>();

        while (matcher.find()) {
            try {
                Long userId = Long.parseLong(matcher.group(1));

                userIds.add(userId);
            } catch (NumberFormatException ignored) {
            }
        }

        return userIds;
    }

    public static String generateTeacherCode(String name, String number) {
        String code = "";

        return code;
    }
}