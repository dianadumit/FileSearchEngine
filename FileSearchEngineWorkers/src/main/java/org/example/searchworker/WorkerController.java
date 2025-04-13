package org.example.searchworker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkerController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/search")
    public List<String> processSearch(@RequestBody WorkerSearchRequest request) {
        String query = request.getQuery();
        return searchService.performSearch(query);
    }
}
