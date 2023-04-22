package com.unigrad.funiverseappservice.util;

import com.unigrad.funiverseappservice.exception.InvalidValueException;

import java.lang.reflect.Field;
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

    public static boolean hasField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static void setValue(Object object, Class<?> clazz, String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {

        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}