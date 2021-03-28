package br.com.smiles.Autenticacao.controller.model.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sessao {

    private String idCliente;
    private String token;
}
