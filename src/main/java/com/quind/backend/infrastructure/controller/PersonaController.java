package com.quind.backend.infrastructure.controller;

import com.quind.backend.domain.entity.Persona;
import com.quind.backend.application.service.PersonaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    @Autowired
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    public ResponseEntity<Persona> crear(@Valid @RequestBody Persona persona) {
        return ResponseEntity.ok(personaService.crearPersona(persona));
    }

    @GetMapping ("ObtenerTodosLosUsusarios")
    public ResponseEntity<List<Persona>> listarTodas() {
        return ResponseEntity.ok(personaService.obtenerTodas());
    }

    @GetMapping("ObtenerUsuariosID/{id}")
    public ResponseEntity<Persona> obtenerPorId(@PathVariable Long id) {
        return personaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("ActualizarUsuarioID/{id}")
    public ResponseEntity<Persona> actualizar(@PathVariable Long id, @RequestBody Persona persona) {
        return personaService.obtenerPorId(id)
                .map(p -> {
                    persona.setId(id); // Asegura que se actualice la persona con el ID correcto
                    return ResponseEntity.ok(personaService.actualizarPersona(persona));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("EliminarUsuariosID/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        personaService.eliminarPersona(id);
        return ResponseEntity.noContent().build();
    }
}
