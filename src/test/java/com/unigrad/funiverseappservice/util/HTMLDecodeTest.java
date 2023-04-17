package com.unigrad.funiverseappservice.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class HTMLDecodeTest {

    @Test
    void extractUser() {
        String content = "<p>test tag <span class=\"mention\" data-index=\"1\" data-denotation-char=\"@\" data-id=\"2\" data-value=\"Patrik Sjölin\">" +
                "\uFEFF" +
                "<span contenteditable=\"false\"><span class=\"ql-mention-denotation-char\">@</span>" +
                "Patrik Sjölin</span>" +
                "\uFEFF</span> tesst tag " +
                "<span class=\"mention\" data-index=\"0\" data-denotation-char=\"@\" data-id=\"1\" data-value=\"Fredrik Sundqvist\">\uFEFF" +
                "<span contenteditable=\"false\"><span class=\"ql-mention-denotation-char\">@</span>Fredrik Sundqvist</span>\uFEFF</span> </p>";
        Long[] expected = {2L, 1L};
        assertArrayEquals(expected, HTMLDecode.extractUser(content).toArray(new Long[0]));
    }
}