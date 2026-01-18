package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.payload.request.ChangePasswordRequest;
import com.example.demo.service.ClienteService;
import com.example.demo.service.UsuarioService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public List<Usuario> getAllUsuarios(){return usuarioService.findAll();}

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findUsuarioById(@PathVariable Long id){
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public List<Usuario> findUsuarioByNombreParcial(@PathVariable String nombre){return usuarioService.findUsuariosByNombreParcial(nombre);}

    @PostMapping("/cambiar-contrasena/")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        String username = principal.getName();
        Usuario usuario = usuarioService.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        boolean changed = usuarioService.changePassword(usuario.getId(), request.getOldPassword(), request.getNewPassword());
        if (changed) {
            return ResponseEntity.ok("Contraseña cambiada exitosamente");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña antigua es incorrecta");
        }
    }

    /*@PostMapping("/")
    public ResponseEntity<Usuario> addUsuario(@RequestBody Usuario usuario){
        Usuario addedUsuario = usuarioService.save(usuario);
        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
        Usuario uppdatedUsuario = usuarioService.update(id,usuario)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
        return new ResponseEntity<Usuario>(uppdatedUsuario, HttpStatus.OK);
    }
    */

    /*@DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUsuario(@PathVariable long id){
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
     */
}
