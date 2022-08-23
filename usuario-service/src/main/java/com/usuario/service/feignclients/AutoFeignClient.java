package com.usuario.service.feignclients;

import com.usuario.service.modelos.Auto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "auto-service", url = "http://localhost:8002")
@RequestMapping("/auto")
public interface AutoFeignClient {

    @PostMapping
    public Auto save(@RequestBody Auto auto);

    @GetMapping("/usuario/{usuarioId}")
    public List<Auto> getAutos(@PathVariable("usuarioId") int usuarioId);
}
