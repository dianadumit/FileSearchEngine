package org.example.searchmaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.example.searchworker.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunicationService {
    private static final Logger logger = LoggerFactory.getLogger(CommunicationService.class);
    @Value("${worker.urls}")
    private String workerUrls;
    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable("searchResults")
    public List<String> sendSearchRequest(String query){

        logger.info("Computing result for query: {}. ", query);
        List<String> results = new ArrayList<>();
        String[] urls = workerUrls.split(",");
        for (String url : urls) {
            WorkerSearchRequest workerRequest = new WorkerSearchRequest(query);
            List<String> workerResponse = restTemplate.postForObject(
                    url.trim(),
                    workerRequest,
                    List.class
            );

            if (workerResponse != null) {
                results.addAll(workerResponse);
            }
        }
        return results;
    }
}