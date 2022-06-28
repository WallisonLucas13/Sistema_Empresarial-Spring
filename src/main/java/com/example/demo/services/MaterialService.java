package com.example.demo.services;

import com.example.demo.models.Material;
import com.example.demo.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Transactional
    public void salvarMaterial(Material material){
        materialRepository.save(material);
    }

    @Transactional
    public Boolean deletarMaterialPorId(Long id){
        Boolean exist = materialRepository.existsById(id);

        if(exist){
            materialRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Material buscarMaterialPorId(Long id){
        Optional<Material> optional = materialRepository.findById(id);

        if(optional.isPresent()){
            return optional.get();
        }
        return null;

    }
}
