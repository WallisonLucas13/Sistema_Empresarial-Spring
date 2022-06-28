package com.example.demo.services;

import com.example.demo.models.Servico;
import com.example.demo.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Transactional
    public void salvarServico(Servico servico){
        servicoRepository.save(servico);
    }

    @Transactional
    public List<Servico> listarServicos(){
        return servicoRepository.findAll();
    }

    @Transactional
    public Servico encontrarServicoPorId(Long id){
        Optional<Servico> optional = servicoRepository.findById(id);

        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Transactional
    public Boolean deletarServicoPorId(Long id){
        Boolean exist = servicoRepository.existsById(id);

        if(exist){
            servicoRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
