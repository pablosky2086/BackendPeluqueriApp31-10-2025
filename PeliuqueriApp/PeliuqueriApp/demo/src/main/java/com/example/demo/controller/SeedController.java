package com.example.demo.controller;

import com.example.demo.service.SeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seed")
public class SeedController {

    private final SeedService seedService;

    public SeedController(SeedService seedService) {
        this.seedService = seedService;
    }

    @PostMapping("/servicios")
    public ResponseEntity<String> seedServicios() {
        seedService.seedServicios();
        return ResponseEntity.ok("Servicios importados correctamente");
    }
}
