package com.example.demo.controller;

import com.example.demo.model.Tarea;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final List<Tarea> tareas = new ArrayList<>();
    private Long currentId = 1L;

    public TareaController() {
        tareas.add(new Tarea(currentId++, "Configurar entorno", "Instalar VS Code, Java 21 y Postman", true));
        tareas.add(new Tarea(currentId++, "Crear proyecto", "Generar proyecto Spring Boot con Spring Web", true));
        tareas.add(new Tarea(currentId++, "Implementar CRUDs", "Crear endpoints para Producto, Estudiante, Libro y Tarea", false));
        tareas.add(new Tarea(currentId++, "Probar en Postman", "Verificar peticiones GET, POST, PUT, PATCH y DELETE", false));
        tareas.add(new Tarea(currentId++, "Subir a GitHub", "Publicar los cambios en el repositorio de la materia", false));
    }

    @GetMapping
    public List<Tarea> getAll() {
        return tareas;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> getById(@PathVariable Long id) {
        return tareas.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tarea> create(@RequestBody Tarea tarea) {
        tarea.setId(currentId++);
        tareas.add(tarea);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> update(@PathVariable Long id, @RequestBody Tarea actualizada) {
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).getId().equals(id)) {
                actualizada.setId(id);
                tareas.set(i, actualizada);
                return ResponseEntity.ok(actualizada);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tarea> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Tarea> opt = tareas.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Tarea t = opt.get();
        if (updates.containsKey("titulo")) t.setTitulo((String) updates.get("titulo"));
        if (updates.containsKey("descripcion")) t.setDescripcion((String) updates.get("descripcion"));
        if (updates.containsKey("completada")) t.setCompletada((Boolean) updates.get("completada"));

        return ResponseEntity.ok(t);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return tareas.removeIf(t -> t.getId().equals(id)) ? 
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}