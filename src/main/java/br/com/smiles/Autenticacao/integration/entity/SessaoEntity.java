package br.com.smiles.Autenticacao.integration.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "Sessao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoEntity {
    @Id
    @NotNull
    private String idCliente;
    @NotNull
    private String token;
    @NotNull
    private Timestamp timeout;
}
