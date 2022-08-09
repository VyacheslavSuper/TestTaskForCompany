package ru.super4121.testtask.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.super4121.testtask.model.Country;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ParserCountry {
    @Value("${parser.country.url}")
    private String url;
    @Value("${parser.country.parameters.codes}")
    private String paramCodes;
    @Value("${parser.country.parameters.fields}")
    private String paramField;
    @Value("${parser.country.parameters.fields.names}")
    private String fields;

    public List<Country> getCountry(Collection<String> codes) throws Exception {
        collectionIsNotNullAndNotEmpty(codes);

        String urlRequest = generateUrlRequest(codes);

        Gson gson = new Gson();
        try (InputStream in = new URL(urlRequest).openStream()) {
            String json = IOUtils.toString(in, StandardCharsets.UTF_8);
            Type listType = new TypeToken<ArrayList<Country>>() {
            }.getType();
            return gson.fromJson(json, listType);
        }
    }

    private String generateUrlRequest(Collection<String> codes) {
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        builder.append(paramCodes);
        int i = 0;
        int size = codes.size();
        for (String code : codes) {
            builder.append(code);
            if (i < size - 1) {
                builder.append(",");
            }
            i++;
        }

        if (!fields.isEmpty()) {
            builder
                    .append("&")
                    .append(paramField)
                    .append(fields);
        }
        return builder.toString();
    }

    private void collectionIsNotNullAndNotEmpty(Collection<String> collection) throws Exception {
        if (collection == null) {
            throw new NullPointerException("Collection is null");
        }
        if (collection.isEmpty()) {
            throw new Exception("Collection is empty");
        }
    }

}
