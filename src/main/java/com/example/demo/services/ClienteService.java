package com.example.demo.services;

import com.example.demo.models.Cliente;
import com.example.demo.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public void salvarCliente(Cliente cliente){
        clienteRepository.save(cliente);
    }

    @Transactional
    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente encontrarClientePorId(Long id){
        Optional<Cliente> optional = clienteRepository.findById(id);

        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Transactional
    public Boolean deletarClientePorId(Long id){
        Boolean exist = clienteRepository.existsById(id);

        if(exist){
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
