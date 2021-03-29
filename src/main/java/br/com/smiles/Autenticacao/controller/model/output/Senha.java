package br.com.smiles.Autenticacao.controller.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Senha {

    @JsonProperty(value = "senha")
    private String senha;
}
