package com.example.demo.controller;

import com.example.demo.model.Estudiante;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final List<Estudiante> estudiantes = new ArrayList<>();
    private Long currentId = 1L;

    public EstudianteController() {
        estudiantes.add(new Estudiante(currentId++, "Carlos", "Gómez", "Sistemas"));
        estudiantes.add(new Estudiante(currentId++, "María", "López", "Sistemas"));
        estudiantes.add(new Estudiante(currentId++, "Juan", "Pérez", "Industrial"));
        estudiantes.add(new Estudiante(currentId++, "Ana", "Martínez", "Administración"));
    }

    @GetMapping
    public List<Estudiante> getAll() {
        return estudiantes;
    }

    @GetMapping("/buscar")
    public List<Estudiante> getByCarrera(@RequestParam(required = false) String carrera) {
        if (carrera == null || carrera.isBlank()) {
            return estudiantes;
        }

        String terminoBuscado = Normalizer.normalize(carrera, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();

        List<Estudiante> resultado = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            if (e.getCarrera() != null) {
                String carreraEstudiante = Normalizer.normalize(e.getCarrera(), Normalizer.Form.NFD)
                        .replaceAll("\\p{M}", "")
                        .toLowerCase();

                if (carreraEstudiante.equals(terminoBuscado)) {
                    resultado.add(e);
                }
            }
        }
        return resultado;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> getById(@PathVariable Long id) {
        return estudiantes.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Estudiante> create(@RequestBody Estudiante estudiante) {
        estudiante.setId(currentId++);
        estudiantes.add(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(estudiante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> update(@PathVariable Long id, @RequestBody Estudiante actualizado) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getId().equals(id)) {
                actualizado.setId(id);
                estudiantes.set(i, actualizado);
                return ResponseEntity.ok(actualizado);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Estudiante> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Estudiante> opt = estudiantes.stream().filter(e -> e.getId().equals(id)).findFirst();
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Estudiante e = opt.get();
        if (updates.containsKey("nombre")) e.setNombre((String) updates.get("nombre"));
        if (updates.containsKey("apellido")) e.setApellido((String) updates.get("apellido"));
        if (updates.containsKey("carrera")) e.setCarrera((String) updates.get("carrera"));
        if (updates.containsKey("email")) e.setEmail((String) updates.get("email"));

        return ResponseEntity.ok(e);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return estudiantes.removeIf(e -> e.getId().equals(id)) ? 
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}