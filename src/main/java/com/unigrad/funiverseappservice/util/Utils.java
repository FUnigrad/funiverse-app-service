package com.unigrad.funiverseappservice.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {

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