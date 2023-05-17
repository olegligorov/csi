package com.imageclassification.util;

import java.io.IOException;
import java.util.Map;

public interface ImageTagger {
    Map<String, Double> getImageTags(String imageUrl) throws IOException;
    String getServiceName();
}