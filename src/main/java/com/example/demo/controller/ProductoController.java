package com.example.demo.controller;

import com.example.demo.model.Producto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final List<Producto> productos = new ArrayList<>();
    private Long currentId = 1L;

    public ProductoController() {
        productos.add(new Producto(currentId++, "Laptop", 1200.0, 10, "Tecnologia"));
        productos.add(new Producto(currentId++, "Mouse", 25.5, 50, "Perifericos"));
        productos.add(new Producto(currentId++, "Teclado", 45.0, 30, "Perifericos"));
        productos.add(new Producto(currentId++, "Monitor", 300.0, 15, "Tecnologia"));
        productos.add(new Producto(currentId++, "Audifonos", 80.0, 20, "Perifericos"));
    }

    @GetMapping
    public List<Producto> getAll() {
        return productos;
    }

    @GetMapping("/buscar")
    public List<Producto> getByCategoria(@RequestParam(required = false) String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return productos;
        }

        String terminoBuscado = Normalizer.normalize(categoria, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();

        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoria() != null) {
                String categoriaProducto = Normalizer.normalize(p.getCategoria(), Normalizer.Form.NFD)
                        .replaceAll("\\p{M}", "")
                        .toLowerCase();

                if (categoriaProducto.equals(terminoBuscado)) {
                    resultado.add(p);
                }
            }
        }
        return resultado;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> create(@Valid @RequestBody Producto producto) {
        producto.setId(currentId++);
        productos.add(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @Valid @RequestBody Producto actualizado) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equals(id)) {
                actualizado.setId(id);
                productos.set(i, actualizado);
                return ResponseEntity.ok(actualizado);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Producto> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Producto> opt = productos.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Producto p = opt.get();
        if (updates.containsKey("nombre")) p.setNombre((String) updates.get("nombre"));
        if (updates.containsKey("precio")) p.setPrecio(((Number) updates.get("precio")).doubleValue());
        if (updates.containsKey("stock")) p.setStock((Integer) updates.get("stock"));
        if (updates.containsKey("categoria")) p.setCategoria((String) updates.get("categoria"));

        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productos.removeIf(p -> p.getId().equals(id)) ? 
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}