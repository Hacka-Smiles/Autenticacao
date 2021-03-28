package br.com.smiles.Autenticacao.controller.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Login {

    @JsonProperty(value = "idCliente")
    private String idCliente;
    @JsonProperty(value = "usuario")
    private String usuario;
    @JsonProperty(value = "senha")
    private String senha;
}
