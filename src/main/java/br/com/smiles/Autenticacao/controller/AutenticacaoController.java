package br.com.smiles.Autenticacao.controller;

import br.com.smiles.Autenticacao.controller.model.input.Login;
import br.com.smiles.Autenticacao.controller.model.output.Senha;
import br.com.smiles.Autenticacao.controller.utils.Erro;
import br.com.smiles.Autenticacao.controller.model.output.Sessao;
import br.com.smiles.Autenticacao.controller.utils.Token;
import br.com.smiles.Autenticacao.integration.entity.LoginEntity;
import br.com.smiles.Autenticacao.integration.entity.SessaoEntity;
import br.com.smiles.Autenticacao.integration.repository.LoginRepository;
import br.com.smiles.Autenticacao.integration.repository.SessaoRepository;
import lombok.extern.java.Log;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

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
    @GetMapping("/sessao/{idCliente}")
    private ResponseEntity<?> verificaSessao(@PathVariable(value = "idCliente") String idCliente,
                                             @RequestHeader(value = "token") String token) {

            SessaoEntity sessaoEntity = sessaoRepository.findByIdCliente(idCliente);

        if (token.equals(sessaoEntity.getToken())) {
            if (sessaoEntity.getTimeout().after(Timestamp.from(Instant.now()))) {

                return ResponseEntity.ok(Sessao.builder()
                        .idCliente(sessaoEntity.getIdCliente())
                        .token(sessaoEntity.getToken())
                        .build());
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
                    .idCliente(UUID.randomUUID().toString())
                    .senha(login.getSenha())
                    .usuario(login.getUsuario())
                    .build());
            return ResponseEntity.ok(Login.builder().idCliente(loginEntity.getIdCliente()).senha(loginEntity.getSenha()).usuario(loginEntity.getUsuario()).build());
        }
    }

    @CrossOrigin(origins = "*")
    @PatchMapping("/atualizar")
    private ResponseEntity<?> atualizarLogin(@RequestBody Login login) {

        if (loginRepository.existsByUsuario(login.getUsuario())) {

            LoginEntity loginEntity = loginRepository.save(LoginEntity.builder()
                    .idCliente(login.getIdCliente())
                    .senha(login.getSenha())
                    .usuario(login.getUsuario())
                    .build());
            return ResponseEntity.ok(login);

        } else {

            return ResponseEntity.status(404).body(Erro.builder().menssagem("Conta não existe!").build());

        }
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{idCliente}")
    private ResponseEntity<?> deletarLogin(@PathVariable(value = "idCliente") String idCliente){
        if (loginRepository.existsByIdCliente(idCliente)) {
            LoginEntity loginEntity = loginRepository.findByIdCliente(idCliente);
            loginRepository.delete(loginEntity);
            return ResponseEntity.ok("Registro excluido com sucesso");

        } else {
            return ResponseEntity.status(400).body(Erro.builder().menssagem("Conta não existe!").build());
        }
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/sessao/{idCliente}")
    private ResponseEntity<?> deletarSessao(@PathVariable(value = "idCliente") String idCliente){

        if(sessaoRepository.existsById(idCliente)){
            SessaoEntity sessaoEntity = sessaoRepository.findByIdCliente(idCliente);
            sessaoEntity.setTimeout(Timestamp.from(Instant.now()));
            sessaoRepository.save(sessaoEntity);
            return ResponseEntity.ok("Sessão finalizada com sucesso!");
        } else{
            return ResponseEntity.status(404).body("Sessão não encontrada!");
        }

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/clienteId")
    private ResponseEntity<?> consultaClienteID(@RequestBody Login login) {

        if(loginRepository.existsByUsuarioAndSenha(login.getUsuario(), login.getSenha())){
            LoginEntity loginEntity = loginRepository.findByUsuarioAndSenha(login.getUsuario(), login.getSenha());
            return ResponseEntity.ok(Login.builder()
                    .idCliente(loginEntity.getIdCliente())
                    .usuario(loginEntity.getUsuario())
                    .senha(login.getSenha())
                    .build());
        } else {
            return ResponseEntity.status(404).body("Conta não encontrada!");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{senha}")
    private ResponseEntity<?> encryptarSenha(@PathVariable(value = "senha")String senha){
            if(senha.isEmpty()){
                return ResponseEntity.status(400).body("String vazia!");
            } else {
                return ResponseEntity.ok(Senha.builder().senha(Md5Crypt.apr1Crypt(senha)).build());
            }
    }
}
