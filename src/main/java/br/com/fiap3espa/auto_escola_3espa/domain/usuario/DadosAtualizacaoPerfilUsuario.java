package br.com.fiap3espa.auto_escola_3espa.domain.usuario;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPerfilUsuario(

        @NotNull
        Long id,

        @NotNull
        Role perfil) {
}