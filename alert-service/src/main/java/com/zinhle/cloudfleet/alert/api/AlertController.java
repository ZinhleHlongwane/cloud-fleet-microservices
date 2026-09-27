package com.zinhle.cloudfleet.alert.api;

import com.zinhle.cloudfleet.alert.service.AlertService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertService service;
    public AlertController(AlertService service){this.service=service;}

    @GetMapping
    public List<AlertResponse> all(@RequestParam(required=false) Boolean acknowledged){return service.all(acknowledged);}

    @PutMapping("/{id}/acknowledge")
    public AlertResponse acknowledge(@PathVariable Long id){return service.acknowledge(id);}
}
