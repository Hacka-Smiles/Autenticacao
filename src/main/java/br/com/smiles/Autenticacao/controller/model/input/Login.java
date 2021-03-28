package br.com.smiles.Autenticacao.controller.model.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Login {

    private String idCliente;
    private String Token;
}
