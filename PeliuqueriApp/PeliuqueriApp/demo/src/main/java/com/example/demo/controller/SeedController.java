package com.example.demo.controller;

import com.example.demo.model.Admin;
import com.example.demo.model.Usuario;
import com.example.demo.service.AdminService;
import com.example.demo.service.AgendaService;
import com.example.demo.service.DatabaseResetService;
import com.example.demo.service.SeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seed")
public class SeedController {

    private final SeedService seedService;
    private final DatabaseResetService databaseResetService;
    private final AdminService adminService;

    public SeedController(SeedService seedService, AgendaService agendaService, DatabaseResetService databaseResetService, AdminService adminService) {
        this.seedService = seedService;
        this.databaseResetService = databaseResetService;
        this.adminService = adminService;
    }



    @PostMapping("/servicios")
    public ResponseEntity<String> seedServicios() {
        databaseResetService.resetDatabase();
        Usuario adminUser = new Usuario();
        adminUser.setNombreCompleto("admin");
        adminUser.setEmail("admin@testmail.com");
        adminUser.setContrasena("pass");
        Admin admin = new Admin(adminUser);
        admin.setEspecialidad("Super Admin");
        adminService.save(admin);
        seedService.seedTipoServicios();
        seedService.seedServicios();
        seedService.seedGrupos();
        seedService.seedAgenda();
        /*AgendaRequest a = new AgendaRequest("2025-12-15T09:00:00","2025-12-15T09:45:00",2L,2L);
        agendaService.createAgendasParaUnTiempo(a, 50);*/
        return ResponseEntity.ok("Servicios importados correctamente");
    }

}
