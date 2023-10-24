package com.example.proyectointegrador.service;

import com.example.proyectointegrador.entity.Comida;
import com.example.proyectointegrador.exception.ComidaDuplicadaException;
import com.example.proyectointegrador.repository.ComidaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComidaService {

    private final ComidaRepository comidaRepository;

    @Autowired
    public ComidaService(ComidaRepository comidaRepository) {
        this.comidaRepository = comidaRepository;
    }

    public Comida guardarComida(Comida comida, List<String> listaDeUrls) {
        String nombreComida = comida.getNombre();
        if (comidaRepository.existsByNombre(nombreComida)) {
            throw new ComidaDuplicadaException("El nombre de la comida ya está en uso: " + nombreComida);
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String imagenesJson = objectMapper.writeValueAsString(listaDeUrls);
            comida.setImagenes(imagenesJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return comidaRepository.save(comida);
    }

    public void eliminarComida(Long id) {
        comidaRepository.deleteById(id);
    }

    public Optional<Comida> buscarComidaPorId(Long id) {
        return comidaRepository.findById(id);
    }

    public List<Comida> listarTodasLasComidas() {
        return comidaRepository.findAll();
    }

}