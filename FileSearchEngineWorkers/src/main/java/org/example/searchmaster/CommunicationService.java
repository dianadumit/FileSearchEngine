package org.example.searchmaster;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.example.searchworker.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunicationService {
    @Value("${worker.urls}")
    private String workerUrls;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> sendSearchRequest(String query){
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