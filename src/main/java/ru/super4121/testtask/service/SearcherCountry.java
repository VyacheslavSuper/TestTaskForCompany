package ru.super4121.testtask.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.super4121.testtask.exception.ProjectException;
import ru.super4121.testtask.model.Country;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SearcherCountry {
    @Value("${parser.country.url}")
    private String url;
    @Value("${parser.country.parameters.codes}")
    private String paramCodes;
    @Value("${parser.country.parameters.fields}")
    private String paramField;
    @Value("${parser.country.parameters.fields.names}")
    private String fields;

    public List<Country> getCountries(Collection<String> codes) throws ProjectException {
        collectionIsNotNullAndNotEmpty(codes);

        String urlRequest = generateUrlRequest(codes);

        Gson gson = new Gson();
        try (InputStream in = new URL(urlRequest).openStream()) {
            String json = IOUtils.toString(in, StandardCharsets.UTF_8);
            Type listType = new TypeToken<ArrayList<Country>>() {
            }.getType();
            return gson.fromJson(json, listType);
        } catch (IOException e) {
            throw new ProjectException("Cant get countries");
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

    private void collectionIsNotNullAndNotEmpty(Collection<String> collection) throws ProjectException {
        if (collection == null) {
            throw new ProjectException("Collection is null");
        }
        if (collection.isEmpty()) {
            throw new ProjectException("Collection is empty");
        }
    }

}
