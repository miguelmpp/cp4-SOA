package br.com.fiap3espa.auto_escola_3espa.domain.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(

        @NotBlank
        String logradouro,
        String numero,
        String complemento,

        @NotBlank
        String bairro,

        @NotBlank
        String cidade,

        @NotBlank
        @Pattern(regexp = "[A-Z]{2}")
        String uf,

        @NotBlank
        @Pattern(regexp = "[0-9]{8}")
        String cep) {
}
