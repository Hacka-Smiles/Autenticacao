package br.com.smiles.Autenticacao.integration.repository;

import br.com.smiles.Autenticacao.integration.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity,String> {

    boolean existsByUsuarioAndSenha(String usuario, String senha);
    boolean existsByUsuario(String usuario);
    boolean existsByIdCliente(String idCliente);
    LoginEntity findByIdCliente(String idCliente);

}
