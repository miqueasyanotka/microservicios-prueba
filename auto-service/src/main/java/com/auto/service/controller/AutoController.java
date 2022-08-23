package com.auto.service.controller;


import com.auto.service.entity.Auto;
import com.auto.service.service.AutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auto")
public class AutoController {

    @Autowired
    private AutoService autoService;

    @GetMapping
    public ResponseEntity<List<Auto>> listarAutos(){
        List<Auto> autos = autoService.getAll();
        if (autos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auto> obtenerAuto(@PathVariable ("id") int id){
        Auto auto = autoService.getAutoById(id);
        if (auto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(auto);
    }

    @PostMapping
    public ResponseEntity<Auto> guardarAuto(@RequestBody Auto auto){
        Auto nuevoAuto = autoService.save(auto);
        return ResponseEntity.ok(nuevoAuto);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Auto>> listarAutosPorUsuarioId(@PathVariable ("usuarioId") int id){
        List<Auto> autos = autoService.byUsuarioId(id);
        if (autos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autos);
    }
}
