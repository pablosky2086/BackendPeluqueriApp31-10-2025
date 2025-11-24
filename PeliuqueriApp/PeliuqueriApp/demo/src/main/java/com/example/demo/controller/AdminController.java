package com.example.demo.controller;

import com.example.demo.model.Admin;
import com.example.demo.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {this.adminService = adminService;}

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Admin> getAllAdmins() {return adminService.findAll();}

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin no encontrado"));
        return new ResponseEntity<Admin>(admin, HttpStatus.OK);
    }

    @GetMapping("/especialidad/{especialidad}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Admin> getAdminsByEspecialidad(@PathVariable String especialidad){
        return adminService.findByPartialEspecialidad(especialidad);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin admin){
        Admin updatedAdmin = adminService.update(id,admin)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin no encontrado"));
        return new ResponseEntity<Admin>(updatedAdmin, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id){
        adminService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
