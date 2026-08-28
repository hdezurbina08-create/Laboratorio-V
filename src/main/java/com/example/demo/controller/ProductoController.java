package com.example.demo.model;

import com.example.demo.model.ProductoController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        productos.add(new Producto(currentId++, "Laptop", 1200.00, 10));
        productos.add(new Producto(currentId++, "Mouse", 25.50, 50));
        productos.add(new Producto(currentId++, "Teclado", 45.00, 30));
        productos.add(new Producto(currentId++, "Monitor", 300.00, 15));
        productos.add(new Producto(currentId++, "Audífonos", 80.00, 20));
    }

    // GET ALL - Obtener todos los productos
    @GetMapping
    public List<Producto> getAll() {
        return productos;
    }

    // GET BY ID - Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Crear producto
    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        producto.setId(currentId++);
        productos.add(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    // PUT - Actualizar completo
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto actualizado) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equals(id)) {
                actualizado.setId(id);
                productos.set(i, actualizado);
                return ResponseEntity.ok(actualizado);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // PATCH - Actualización parcial
    @PatchMapping("/{id}")
    public ResponseEntity<Producto> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Producto> opt = productos.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Producto p = opt.get();
        if (updates.containsKey("nombre")) p.setNombre((String) updates.get("nombre"));
        if (updates.containsKey("precio")) p.setPrecio(Double.valueOf(updates.get("precio").toString()));
        if (updates.containsKey("stock")) p.setStock(Integer.valueOf(updates.get("stock").toString()));

        return ResponseEntity.ok(p);
    }

    // DELETE - Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productos.removeIf(p -> p.getId().equals(id)) ? 
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}