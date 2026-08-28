package com.example.demo.controller;

import com.example.demo.model.Libro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final List<Libro> libros = new ArrayList<>();
    private Long currentId = 1L;

    public LibroController() {
        libros.add(new Libro(currentId++, "Cien años de soledad", "Gabriel García Márquez", 1967));
        libros.add(new Libro(currentId++, "Don Quijote de la Mancha", "Miguel de Cervantes", 1605));
        libros.add(new Libro(currentId++, "1984", "George Orwell", 1949));
        libros.add(new Libro(currentId++, "El principito", "Antoine de Saint-Exupéry", 1943));
        libros.add(new Libro(currentId++, "Fahrenheit 451", "Ray Bradbury", 1953));
    }

    @GetMapping
    public List<Libro> getAll() {
        return libros;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> getById(@PathVariable Long id) {
        return libros.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Libro> create(@RequestBody Libro libro) {
        libro.setId(currentId++);
        libros.add(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> update(@PathVariable Long id, @RequestBody Libro actualizado) {
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getId().equals(id)) {
                actualizado.setId(id);
                libros.set(i, actualizado);
                return ResponseEntity.ok(actualizado);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Libro> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Libro> opt = libros.stream().filter(l -> l.getId().equals(id)).findFirst();
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Libro l = opt.get();
        if (updates.containsKey("titulo")) l.setTitulo((String) updates.get("titulo"));
        if (updates.containsKey("autor")) l.setAutor((String) updates.get("autor"));
        if (updates.containsKey("anioPublicacion")) l.setAnioPublicacion((Integer) updates.get("anioPublicacion"));

        return ResponseEntity.ok(l);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return libros.removeIf(l -> l.getId().equals(id)) ? 
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
