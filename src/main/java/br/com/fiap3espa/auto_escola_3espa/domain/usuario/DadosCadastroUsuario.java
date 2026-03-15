package br.com.fiap3espa.auto_escola_3espa.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(

        @NotBlank
        String login,

        @NotBlank
        @Size(min = 4, max = 100)
        String senha,

        @NotNull
        Role perfil) {
}