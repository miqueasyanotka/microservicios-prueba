package com.usuario.service.service;

import com.usuario.service.entity.Usuario;
import com.usuario.service.feignclients.AutoFeignClient;
import com.usuario.service.feignclients.MotoFeignClient;
import com.usuario.service.modelos.Auto;
import com.usuario.service.modelos.Moto;
import com.usuario.service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AutoFeignClient autoFeignClient;

    @Autowired
    private MotoFeignClient motoFeignClient;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario) {
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return nuevoUsuario;
    }

    //RestTemplate
    public List<Auto> getAutos(int usuarioId){
        List<Auto> autos = restTemplate.getForObject("http://auto-service/auto/usuario/" + usuarioId, List.class);
        return autos;
    }

    public List<Moto> getMotos(int usuarioId){
        List<Moto> motos = restTemplate.getForObject("http://moto-service/moto/usuario/" + usuarioId, List.class);
        return motos;
    }


    //FeignClient
    public Auto saveAuto(int usuarioId, Auto auto) {
        auto.setUsuarioId(usuarioId);
        Auto nuevoAuto = autoFeignClient.save(auto);
        return nuevoAuto;
    }

    public Moto saveMoto(int usuarioId, Moto moto) {
        moto.setUsuarioId(usuarioId);
        Moto nuevoMoto = motoFeignClient.save(moto);
        return nuevoMoto;
    }

    public Map<String, Object> getUsuarioAndVehiculos(int usuarioId) {
        Map<String, Object> resultado = new HashMap<String, Object>();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if(usuario == null) {
            resultado.put("Mensaje", "El usuario no existe");
            return resultado;
        }

        resultado.put("Usuario", usuario);

        List<Auto> autos = autoFeignClient.getAutos(usuarioId);

        if (autos.isEmpty()){
            resultado.put("Autos", "El usuario no tiene autos");
            return resultado;
        } else {
            resultado.put("Autos", autos);
        }

        List<Moto> motos = motoFeignClient.getMotos(usuarioId);
        if (motos.isEmpty()){
            resultado.put("Motos", "El usuario no tiene motos");
            return resultado;
        } else {
            resultado.put("Motos", motos);
        }

        return resultado;
    }

}
