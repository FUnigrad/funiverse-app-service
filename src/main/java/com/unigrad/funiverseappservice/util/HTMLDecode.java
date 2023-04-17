package com.unigrad.funiverseappservice.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HTMLDecode {

    private static final Logger LOG = LoggerFactory.getLogger(HTMLDecode.class);

    public static List<Long> extractUser(String content) {
        List<Long> result = new ArrayList<>();

        Document document = Jsoup.parse(content);

        Elements mentionElements = document.select("span.mention");

        for (Element mentionElement : mentionElements) {
            String dataId = mentionElement.attr("data-id");

            try {
                result.add(Long.parseLong(dataId));
            } catch (NumberFormatException exception) {
                LOG.error("Cannot parse value %s".formatted(dataId));
            }
        }

        return result;
    }
}