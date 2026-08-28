package com.example.demo.controller;

import com.example.demo.model.Cliente;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Endpoints para la gestion de clientes")
public class ClienteController {

    private final List<Cliente> clientes = new ArrayList<>();
    private Long currentId = 1L;

    public ClienteController() {
        clientes.add(new Cliente(currentId++, "Carlos", "Perez", "carlos.perez@email.com", "55512345"));
        clientes.add(new Cliente(currentId++, "Ana", "Gomez", "ana.gomez@email.com", "55523456"));
        clientes.add(new Cliente(currentId++, "Luis", "Martinez", "luis.martinez@email.com", "55534567"));
        clientes.add(new Cliente(currentId++, "Maria", "Lopez", "maria.lopez@email.com", "55545678"));
        clientes.add(new Cliente(currentId++, "Jorge", "Hernandez", "jorge.hernandez@email.com", "55556789"));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los clientes")
    public List<Cliente> getAll() {
        return clientes;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public ResponseEntity<Cliente> getById(@PathVariable Long id) {
        return clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente")
    public ResponseEntity<Cliente> create(@Valid @RequestBody Cliente cliente) {
        cliente.setId(currentId++);
        clientes.add(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente completo")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @Valid @RequestBody Cliente actualizado) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId().equals(id)) {
                actualizado.setId(id);
                clientes.set(i, actualizado);
                return ResponseEntity.ok(actualizado);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar campos especificos de un cliente")
    public ResponseEntity<Cliente> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Cliente> opt = clientes.stream().filter(c -> c.getId().equals(id)).findFirst();
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Cliente c = opt.get();
        if (updates.containsKey("nombre")) c.setNombre((String) updates.get("nombre"));
        if (updates.containsKey("apellido")) c.setApellido((String) updates.get("apellido"));
        if (updates.containsKey("correo")) c.setCorreo((String) updates.get("correo"));
        if (updates.containsKey("telefono")) c.setTelefono((String) updates.get("telefono"));

        return ResponseEntity.ok(c);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return clientes.removeIf(c -> c.getId().equals(id)) ? 
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}