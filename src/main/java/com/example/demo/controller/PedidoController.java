package com.example.demo.controller;

import com.example.demo.model.Pedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para la gestion de pedidos")
public class PedidoController {

    private final List<Pedido> pedidos = new ArrayList<>();
    private Long currentId = 1L;

    public PedidoController() {
        // Al menos 5 pedidos iniciales cargados en memoria
        pedidos.add(new Pedido(currentId++, "Carlos Perez", "Laptop", 1, 1200.00, "PENDIENTE"));
        pedidos.add(new Pedido(currentId++, "Ana Gomez", "Mouse", 2, 51.00, "ENVIADO"));
        pedidos.add(new Pedido(currentId++, "Luis Martinez", "Teclado", 1, 45.00, "ENTREGADO"));
        pedidos.add(new Pedido(currentId++, "Maria Lopez", "Monitor", 2, 600.00, "PENDIENTE"));
        pedidos.add(new Pedido(currentId++, "Jorge Hernandez", "Audifonos", 1, 80.00, "CANCELADO"));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los pedidos")
    public List<Pedido> getAll() {
        return pedidos;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<Pedido> getById(@PathVariable Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido")
    public ResponseEntity<Pedido> create(@Valid @RequestBody Pedido pedido) {
        pedido.setId(currentId++);
        pedidos.add(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pedido completo")
    public ResponseEntity<Pedido> update(@PathVariable Long id, @Valid @RequestBody Pedido actualizado) {
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getId().equals(id)) {
                actualizado.setId(id);
                pedidos.set(i, actualizado);
                return ResponseEntity.ok(actualizado);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(
        summary = "Actualizar campos especificos de un pedido",
        description = "Permite modificar únicamente ciertos campos, por ejemplo, cambiar solo el estado a ENVIADO.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "Ejemplo de PATCH para cambiar estado",
                    value = "{\n  \"estado\": \"ENVIADO\"\n}"
                )
            )
        )
    )
    public ResponseEntity<Pedido> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Pedido> opt = pedidos.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pedido p = opt.get();
        if (updates.containsKey("cliente")) p.setCliente((String) updates.get("cliente"));
        if (updates.containsKey("producto")) p.setProducto((String) updates.get("producto"));
        if (updates.containsKey("cantidad")) p.setCantidad((Integer) updates.get("cantidad"));
        if (updates.containsKey("total")) p.setTotal(((Number) updates.get("total")).doubleValue());
        if (updates.containsKey("estado")) p.setEstado((String) updates.get("estado"));

        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pedido por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return pedidos.removeIf(p -> p.getId().equals(id)) ? 
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}