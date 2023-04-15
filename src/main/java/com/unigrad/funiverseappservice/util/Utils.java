package com.unigrad.funiverseappservice.util;

import java.text.Normalizer;
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

    public static String generateUserCode(String name) {
        String[] parts = removeAccent(name).split(" ");
        String firstName = parts[parts.length - 1].toLowerCase();
        StringBuilder lastName = new StringBuilder();

        for (int i = 0; i < parts.length - 1; i++) {
            lastName.append(parts[i].toLowerCase().charAt(0));
        }
        return firstName + lastName;
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d").replaceAll("Đ", "D");
    }
}