package com.example.demo.controllers;

import com.example.demo.models.Cliente;
import com.example.demo.models.Material;
import com.example.demo.models.Servico;
import com.example.demo.services.ClienteService;
import com.example.demo.services.MaterialService;
import com.example.demo.services.ServicoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
@Log4j2
@CrossOrigin("*")
public class ClienteControler {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private MaterialService materialService;

    int maoDeObra = 0;
    int materiaisValor = 0;
    Long iden = 0L;
    Long idService = 0L;

    @GetMapping("/inicio")
    public String inicio(){
        log.info("Pedindo a Home do Sistema");
        return "home";
    }

    @GetMapping("/")
    public String home_redirect(){
        log.info("Redirecionando para Home");
        return "redirect:/inicio";
    }

    @GetMapping("/cadastro")
    public String pagina_cadastro(){
        return "cadastroPage";
    }

    @PostMapping("/cadastro")
    public String salvar_dados_pagina_cadastro(@Valid Cliente cliente, BindingResult res, RedirectAttributes attributes){

        if(res.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Insira todos os Campos!");
            return "redirect:/cadastro";
        }

        cliente.setDataCadastro(LocalDate.now());
        cliente.setTelefone(String.valueOf(cliente.getTelefone()));
        attributes.addFlashAttribute("mensagem", "Sucesso!");
        clienteService.salvarCliente(cliente);
        return "redirect:/cadastro";
    }

    @GetMapping("/clientes")
    public ModelAndView listarClientes(){
        ModelAndView mv = new ModelAndView("clientesPage");
        List<Cliente> clientes = clienteService.listarClientes();

        String msgSemClientes = "";

        if(clientes.isEmpty()){
            msgSemClientes = "A Mega Instalações não possui clientes cadastrados!";
        }

        mv.addObject("clientes", clientes);
        mv.addObject("msg", msgSemClientes);
        return mv;
    }

    @GetMapping("/{id}")
    public ModelAndView encontrarCliente(@PathVariable("id") Long id){
        if(id == 0L){
            ModelAndView mvif = new ModelAndView("home");
            return mvif;
        }

        ModelAndView mv = new ModelAndView("clientePage");
        Cliente cliente = clienteService.encontrarClientePorId(id);
        List<Servico> servicos = cliente.getServicos();

        iden = id;

        String msgSemServicos = "";

        if(servicos.isEmpty()){
            msgSemServicos = "Sem Serviços!";
        }

        mv.addObject("cliente", cliente);
        mv.addObject("servicos", servicos);
        mv.addObject("msg", msgSemServicos);
        return mv;
    }

    @PostMapping("/{id}")
    public String atribuirServico_E_Material(@PathVariable("id") Long id,
                                             @Valid Servico s, BindingResult res,
                                             RedirectAttributes attributes){

        if(res.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Insira todos os Campos!");
            return "redirect:/{id}";
        }

        s.setDateService(LocalDate.now());
        servicoService.salvarServico(s);

        Cliente cliente = clienteService.encontrarClientePorId(id);

        clienteService.salvarCliente(clonarClienteComServicosAtualizado(s, cliente));
        attributes.addFlashAttribute("mensagem", "Sucesso!");
        return "redirect:/{id}";
    }

    @GetMapping("/service-{identificador}")
    public ModelAndView encontrarServico(@PathVariable("identificador") Long identificador){

        idService = identificador;
        ModelAndView mv = new ModelAndView("servicePage");
        Servico service = servicoService.encontrarServicoPorId(identificador);
        List<Material> materiais = service.getMateriais();


        int maoDeObra = Integer.valueOf(service.getMaoDeObra().replace("R$ ", ""));
        int materiaisValorTotal = Integer.valueOf(service.getTotalMateriais().replace("R$ ", ""));
        service.setCustoServicoFinal("R$ " + String.valueOf(maoDeObra + materiaisValorTotal));

        servicoService.salvarServico(service);

        String msgSemMateriais = "";

        if(materiais.isEmpty()){
            msgSemMateriais = "Sem Materiais!";
        }

        mv.addObject("service", service);
        mv.addObject("materiais", calcularCustoTotal(materiais));
        mv.addObject("idCliente", iden);
        mv.addObject("msg", msgSemMateriais);
        mv.addObject("custoTotalServico", somarCustoTotalServico(calcularCustoTotal(materiais)));
        mv.addObject("custoTotalMateriais", service.getTotalMateriais());
        mv.addObject("maoDeObra", service.getMaoDeObra());
        mv.addObject("custoFinal", service.getCustoServicoFinal());
        return mv;
    }

    @PostMapping("/service-{identificador}")
    public String atribuirMaterial_Ao_Servico(Material material,
                                              @PathVariable("identificador") Long identificador,
                                              BindingResult res, RedirectAttributes attributes){

        if(material.getNomeMaterial().isEmpty()){

            attributes.addFlashAttribute("mensagem", "Insira todos os Campos!");
            return "redirect:/service-{identificador}";
        }

        if(material.getQuantMaterial().equals("0")){
            material.setQuantMaterial("1");
        }
        if(material.getQuantMaterial().isEmpty()){
            material.setQuantMaterial("1");
        }
        if(material.getValorMaterial().isEmpty()){
            material.setValorMaterial("0");
        }

        int value = Integer.valueOf(material.getQuantMaterial());
        int v = Integer.valueOf(material.getValorMaterial());

        int result = value * v;
        material.setCustoTotal("R$ "+ String.valueOf(result));

        String valor = String.valueOf(v);
        String format = "R$ " + valor;

        material.setQuantMaterial(String.valueOf(value));
        material.setValorMaterial(format);
        materialService.salvarMaterial(material);

        Servico servico = servicoService.encontrarServicoPorId(identificador);

        servicoService.salvarServico(clonarServicoComMaterialAtualizado(servico, material));

        servico.setTotalMateriais(somarCustoTotalServico(calcularCustoTotal(servico.getMateriais())));
        servicoService.salvarServico(servico);

        attributes.addFlashAttribute("mensagem", "Sucesso!");
        return "redirect:/service-{identificador}";
    }

    @PostMapping("/service-valor")
    public String salvarMaoDeObra(Long id, int valor){

        Servico s = servicoService.encontrarServicoPorId(id);

        s.setMaoDeObra("R$ " + String.valueOf(valor));
        servicoService.salvarServico(s);
        return "redirect:/service-"+id;
    }


    @GetMapping("/delete")
    public String delete(Long id){
        clienteService.deletarClientePorId(id);
        return "redirect:/clientes";
    }

    @GetMapping("/deletarServico")
    public String deleteServico(Long identificador){

        servicoService.deletarServicoPorId(identificador);
        return "redirect:/" + iden;
    }

    @GetMapping("/deletarMaterial")
    public String deleteMaterial(Long iden){

        materialService.deletarMaterialPorId(iden);

        Servico servico = servicoService.encontrarServicoPorId(idService);
        List<Material> materiais = servico.getMateriais();

        String res = somarCustoTotalServico(calcularCustoTotal(materiais));

        servico.setTotalMateriais(res);
        servicoService.salvarServico(servico);
        return "redirect:/service-" + idService;
    }

    private Cliente clonarClienteComServicosAtualizado(Servico servico, Cliente clienteAtual){

        List<Servico> servicosAntigos = clienteAtual.getServicos();
        servicosAntigos.add(servico);

        Cliente clienteNovo = new Cliente();

        clienteNovo.setId(clienteAtual.getId());
        clienteNovo.setNome(clienteAtual.getNome());
        clienteNovo.setTelefone(clienteAtual.getTelefone());
        clienteNovo.setBairro(clienteAtual.getBairro());
        clienteNovo.setEndereco(clienteAtual.getEndereco());
        clienteNovo.setDataCadastro(clienteAtual.getDataCadastro());
        clienteNovo.setServicos(servicosAntigos);

        return clienteNovo;
    }

    private Servico clonarServicoComMaterialAtualizado(Servico servico, Material material){

        List<Material> materiaisAntigos = servico.getMateriais();
        materiaisAntigos.add(material);

        servico.setMateriais(materiaisAntigos);

        return servico;
    }

    private List<Material> calcularCustoTotal(List<Material> materiais){

        for(int i=0; i<materiais.size(); i++){
            String replace = materiais.get(i).getValorMaterial().replace("R$ ", "");
            int valor = Integer.valueOf(replace);
            int quant = Integer.valueOf(materiais.get(i).getQuantMaterial());

            String res = "R$ " + String.valueOf(valor * quant);

            materiais.get(i).setCustoTotal(res);
            continue;
        }

        return materiais;
    }

    private String somarCustoTotalServico(List<Material> materiais){
        int soma = 0;

        for(int i=0; i<materiais.size(); i++){

            String replace = materiais.get(i).getCustoTotal().replace("R$ ", "");
            int valor = Integer.valueOf(replace);
            soma += valor;

            continue;
        }

        String formatCustoTotal = "R$ " + String.valueOf(soma);
        return formatCustoTotal;
    }

    private int somarTudoRetornarInt(List<Material> materiais){

        int soma = 0;

        for(int i=0; i<materiais.size(); i++){

            String replace = materiais.get(i).getCustoTotal().replace("R$ ", "");

            int valor = Integer.valueOf(replace);
            soma += valor;

            continue;
        }

        log.info(soma);
        return soma;
    }

}
