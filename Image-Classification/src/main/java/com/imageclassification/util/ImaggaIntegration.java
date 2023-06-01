package com.imageclassification.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imageclassification.util.ImaggaUtil.ImaggaTag;
import com.imageclassification.util.ImaggaUtil.Root;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImaggaIntegration implements ImageTagger {
    private static final String SERVICE_NAME = "Imagga";
    private static final int SERVICE_LIMIT = 1000;
    private static final String ENDPOINT_URL = "https://api.imagga.com/v2/tags";
    private static final int TAG_LIMIT = 5;

    public String fetchTags(String imageUrl) throws IOException {
        String credentialsToEncode = System.getenv("IMAGGA_API_KEY") + ":" + System.getenv("IMAGGA_API_SECRET");
//        TODO
//        String credentialsToEncode = "acc_4c83b6a65e7d031" + ":" +"ba1b8d4251924b5c91fe8d3322f8b562";

        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String url = ENDPOINT_URL + "?image_url=" + imageUrl;

        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        String jsonResponse;
        try (BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            jsonResponse = connectionInput.readLine();
//        connectionInput.close();
        }
        return jsonResponse;
    }

    public Map<String, Double> getImageTags(String imageUrl) throws IOException {
        String jsonResponse = fetchTags(imageUrl);
        //Parse the jsonResponse to a Set of tags.
        ObjectMapper objectMapper = new ObjectMapper();
        Root tagResult = objectMapper.readValue(jsonResponse, Root.class);

        List<ImaggaTag> tags = tagResult.result.tags.stream().limit(TAG_LIMIT).filter(tag -> tag.confidence > 0.3).toList();

        Map<String, Double> tagMap = new HashMap<>();

        for (var tag : tags) {
            tagMap.put(tag.tag.en, Math.round(tag.confidence * 100.0) / 100.0);
        }

        return tagMap;
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public int getServiceLimit() {
        return SERVICE_LIMIT;
    }
}
