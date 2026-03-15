package br.com.fiap3espa.auto_escola_3espa.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosAlteracaoSenha(

        @NotBlank
        String senhaAtual,

        @NotBlank
        @Size(min = 4, max = 100)
        String novaSenha) {
}