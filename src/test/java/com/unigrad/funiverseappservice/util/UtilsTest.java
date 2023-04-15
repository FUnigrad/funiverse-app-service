package com.unigrad.funiverseappservice.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    @Test
    void generateUserCode1() {
        String name = "Bùi Thanh Bảo";
        String expectedResult = "baobt";
        String code = Utils.generateUserCode(name);

        assertEquals(expectedResult, code);
    }

    @Test
    void generateUserCode2() {
        String name = "Trương Thị Uyên Trang";
        String expectedResult = "trangttu";
        String code = Utils.generateUserCode(name);

        assertEquals(expectedResult, code);
    }

    @Test
    void removeAccent1() {
        String name = "Bùi Thanh Đạt";
        String expectedResult = "Bui Thanh Dat";
        String code = Utils.removeAccent(name);

        assertEquals(expectedResult, code);
    }

    @Test
    void removeAccent2() {
        String name = "Trương Thị Uyên Trang";
        String expectedResult = "Truong Thi Uyen Trang";
        String code = Utils.removeAccent(name);

        assertEquals(expectedResult, code);
    }
}