package org.example.searchmaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MasterController {
    private static final Logger logger = LoggerFactory.getLogger(MasterController.class);
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private CacheManager cacheManager;

    @PostMapping("/search")
    public List<String> executeSearch(@RequestBody MasterSearchRequest request) {
        String query = request.getQuery();
        Cache cache = cacheManager.getCache("searchResults");

        if (cache != null && cache.get(query) != null) {
            logger.info("Result for query '{}' retrieved from cache", query);
        } else {
            logger.info("Result for query '{}' not found in cache", query);
        }
        List<String> results = communicationService.sendSearchRequest(query);
        return results;
    }
}
