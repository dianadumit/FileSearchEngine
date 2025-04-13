package org.example.searchmaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MasterController {
    @Autowired
    private CommunicationService communicationService;
    @PostMapping("/search")
    public List<String> executeSearch(@RequestBody MasterSearchRequest request){
        String query = request.getQuery();
        List<String> results = communicationService.sendSearchRequest(query);
        //add sorting for results
        return results;
    }
}
