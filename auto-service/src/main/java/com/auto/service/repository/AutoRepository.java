package com.auto.service.repository;

import com.auto.service.entity.Auto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Integer> {

    List<Auto> findByUsuarioId(int usuarioId);
}
