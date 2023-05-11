package com.imageclassification.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imageclassification.dtos.TagDTO;
import com.imageclassification.models.Tag;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImaggaIntegration implements ImageTagger {
    private static final String SERVICE_NAME = "Imagga";

    public Set<Tag> getImageTags(String imageUrl) throws IOException {
        String credentialsToEncode = Secret.API_KEY + ":" + Secret.API_SECRET;
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpointUrl = "https://api.imagga.com/v2/tags";

        String url = endpointUrl + "?image_url=" + imageUrl;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

//      Parse the jsonResponse to a Set of tags.
        ObjectMapper objectMapper = new ObjectMapper();
        Root tagResult = objectMapper.readValue(jsonResponse, Root.class);

        List<ImaggaTag> tags = tagResult.result.tags.stream().limit(5).toList();

        Set<Tag> tagSet = new HashSet<>();

        for (var tag : tags) {
            tagSet.add(new Tag(tag.tag.en, tag.confidence));
        }

        return tagSet;
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
}
