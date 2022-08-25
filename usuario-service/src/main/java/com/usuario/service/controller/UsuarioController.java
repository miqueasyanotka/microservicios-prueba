package com.usuario.service.controller;

import com.usuario.service.entity.Usuario;
import com.usuario.service.modelos.Auto;
import com.usuario.service.modelos.Moto;
import com.usuario.service.service.UsuarioService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.getAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") int id) {
        Usuario usuario = usuarioService.getUsuarioById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }


    @CircuitBreaker(name = "autosCB", fallbackMethod = "fallBackGetAutos")
    @GetMapping("/autos/{usuarioId}")
    public ResponseEntity<List<Auto>> listarAutos(@PathVariable("usuarioId") int id) {
        Usuario usuario = usuarioService.getUsuarioById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        List<Auto> autos = usuarioService.getAutos(id);
        return ResponseEntity.ok(autos);
    }

    @CircuitBreaker(name = "motosCB", fallbackMethod = "fallBackGetMotos")
    @GetMapping("/motos/{usuarioId}")
    public ResponseEntity<List<Moto>> listarMotos(@PathVariable("usuarioId") int id) {
        Usuario usuario = usuarioService.getUsuarioById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        List<Moto> motos = usuarioService.getMotos(id);

        return ResponseEntity.ok(motos);
    }


    @CircuitBreaker(name = "autosCB", fallbackMethod = "fallBackSaveAuto")
    @PostMapping("/auto/{usuarioId}")
    public ResponseEntity<Auto> guardarAuto(@PathVariable("usuarioId") int usuarioId, @RequestBody Auto auto) {
        Auto nuevoAuto = usuarioService.saveAuto(usuarioId, auto);
        return ResponseEntity.ok(nuevoAuto);
    }

    @CircuitBreaker(name = "motosCB", fallbackMethod = "fallBackSaveMoto")
    @PostMapping("/moto/{usuarioId}")
    public ResponseEntity<Moto> guardarMoto(@PathVariable("usuarioId") int usuarioId, @RequestBody Moto moto) {
        Moto nuevaMoto = usuarioService.saveMoto(usuarioId, moto);
        return ResponseEntity.ok(nuevaMoto);
    }

    @CircuitBreaker(name = "todosCB", fallbackMethod = "fallBackGetTodos")
    @GetMapping("/todos/{usuarioId}")
    public ResponseEntity<Map<String, Object>> listarTodosLosVehiculos(@PathVariable("usuarioId") int usuarioId) {
        Map<String, Object> resultado = usuarioService.getUsuarioAndVehiculos(usuarioId);
        return ResponseEntity.ok(resultado);
    }


    private ResponseEntity<List<Auto>> fallBackGetAutos(@PathVariable("usuarioId") int id, @RequestBody Auto auto, RuntimeException exception) {
        return new ResponseEntity("El usuairo:" + id + " tiene los autos en el taller", HttpStatus.OK);
    }

    private ResponseEntity<List<Auto>> fallBackSaveAuto(@PathVariable("usuarioId") int id, @RequestBody Auto auto, RuntimeException exception) {
        return new ResponseEntity("El usuairo:" + id + " no tiene dinero para los autos", HttpStatus.OK);
    }

    private ResponseEntity<List<Moto>> fallBackGetMotos(@PathVariable("usuarioId") int id, @RequestBody Moto moto, RuntimeException exception) {
        return new ResponseEntity("El usuairo:" + id + " tiene las motos en el taller", HttpStatus.OK);
    }

    private ResponseEntity<List<Moto>> fallBackSaveMoto(@PathVariable("usuarioId") int id, @RequestBody Moto moto, RuntimeException exception) {
        return new ResponseEntity("El usuairo:" + id + " no tiene dinero para las motos", HttpStatus.OK);
    }

    private ResponseEntity<List<Auto>> fallBackGetTodos(@PathVariable("usuarioId") int id, RuntimeException exception) {
        return new ResponseEntity("El usuairo:" + id + " tiene los vehículos en el taller", HttpStatus.OK);
    }
}
