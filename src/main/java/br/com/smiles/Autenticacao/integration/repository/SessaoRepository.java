package br.com.smiles.Autenticacao.integration.repository;

import br.com.smiles.Autenticacao.integration.entity.SessaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRepository extends JpaRepository<SessaoEntity, String> {
    SessaoEntity findByIdCliente(String idCliente);
}
