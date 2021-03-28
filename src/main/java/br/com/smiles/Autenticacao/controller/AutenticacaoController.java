package br.com.smiles.Autenticacao.controller;

import br.com.smiles.Autenticacao.controller.model.input.Login;
import br.com.smiles.Autenticacao.controller.model.output.Sessao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @CrossOrigin(origins = "*")
    @GetMapping("/{idCliente}")
    private ResponseEntity<Sessao> getCliente(@PathVariable String idCliente,
                                              @RequestParam(value = "usuario") String usuario,
                                              @RequestParam(value = "senha") String senha,
                                              @RequestParam(value = "flowid") String flowid){

        return ResponseEntity.ok(Sessao.builder().idCliente(idCliente).build());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/sessao")
    private ResponseEntity<Sessao> getSessao(@RequestParam(value = "sessao") Sessao sessao){

        return ResponseEntity.ok(sessao);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{idCliente}")
    private ResponseEntity<Login> updateCliente(@RequestBody Sessao idCliente){

        return ResponseEntity.ok(Login.builder().idCliente(idCliente.getIdCliente()).Token("").build());
    }
}
