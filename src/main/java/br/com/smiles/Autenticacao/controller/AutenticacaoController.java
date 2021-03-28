package br.com.smiles.Autenticacao.controller;

import br.com.smiles.Autenticacao.controller.model.input.Login;
import br.com.smiles.Autenticacao.controller.model.output.Erro;
import br.com.smiles.Autenticacao.controller.model.output.Sessao;
import br.com.smiles.Autenticacao.controller.utils.Token;
import br.com.smiles.Autenticacao.integration.entity.LoginEntity;
import br.com.smiles.Autenticacao.integration.entity.SessaoEntity;
import br.com.smiles.Autenticacao.integration.repository.LoginRepository;
import br.com.smiles.Autenticacao.integration.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private SessaoRepository sessaoRepository;

    @CrossOrigin(origins = "*")
    @PostMapping
    private ResponseEntity<?> iniciarSessao(@RequestBody Login login) {
        if (loginRepository.existsByUsuarioAndSenha(login.getUsuario(), login.getSenha())) {

            SessaoEntity sessaoEntity = sessaoRepository.save(SessaoEntity.builder()
                    .idCliente(login.getIdCliente())
                    .token(Token.generateNewToken())
                    .timeout(Timestamp.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
                    .build());

            return ResponseEntity.ok(Sessao.builder()
                    .idCliente(login.getIdCliente())
                    .token(sessaoEntity.getToken())
                    .build());
        } else {
            return ResponseEntity.status(404).body(Erro.builder().menssagem("Conta não encontrada!").build());
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/sessao")
    private ResponseEntity<?> verificaSessao(@RequestBody Sessao sessao) {

        SessaoEntity sessaoEntity = sessaoRepository.findByIdCliente(sessao.getIdCliente());

        if (sessao.getToken().equals(sessaoEntity.getToken())) {
            if (sessaoEntity.getTimeout().before(Timestamp.from(Instant.now()))) {

                return ResponseEntity.ok(sessao);
            } else {

                return ResponseEntity.status(401).body(Erro.builder().menssagem("Token expirado!").build());

            }

        } else {
            return ResponseEntity.status(403).body(Erro.builder().menssagem("Token inválido!").build());
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/cadastrar")
    private ResponseEntity<?> cadastrarLogin(@RequestBody Login login) {
        if (loginRepository.existsByUsuario(login.getUsuario())) {

            return ResponseEntity.status(400).body(Erro.builder().menssagem("Conta já existe!").build());

        } else {
            LoginEntity loginEntity = loginRepository.save(LoginEntity.builder()
                    .idCliente(login.getIdCliente())
                    .senha(login.getSenha())
                    .usuario(login.getUsuario())
                    .build());
            return ResponseEntity.ok(login);
        }
    }

    @CrossOrigin(origins = "*")
    @PatchMapping("/atualizar")
    private ResponseEntity<?> atualizarLogin(@RequestBody Login login) {

        if (loginRepository.existsByUsuario(login.getUsuario())) {

            return ResponseEntity.status(400).body(Erro.builder().menssagem("Conta já existe!").build());

        } else {
            LoginEntity loginEntity = loginRepository.save(LoginEntity.builder()
                    .idCliente(login.getIdCliente())
                    .senha(login.getSenha())
                    .usuario(login.getUsuario())
                    .build());
            return ResponseEntity.ok(login);

        }
    }
}
