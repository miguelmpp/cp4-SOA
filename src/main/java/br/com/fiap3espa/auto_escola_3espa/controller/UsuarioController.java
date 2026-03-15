package br.com.fiap3espa.auto_escola_3espa.controller;

import br.com.fiap3espa.auto_escola_3espa.domain.usuario.DadosAlteracaoSenha;
import br.com.fiap3espa.auto_escola_3espa.domain.usuario.DadosAtualizacaoPerfilUsuario;
import br.com.fiap3espa.auto_escola_3espa.domain.usuario.DadosCadastroUsuario;
import br.com.fiap3espa.auto_escola_3espa.domain.usuario.DadosListagemUsuario;
import br.com.fiap3espa.auto_escola_3espa.domain.usuario.Usuario;
import br.com.fiap3espa.auto_escola_3espa.domain.usuario.UsuarioRepository;
import br.com.fiap3espa.auto_escola_3espa.infra.exception.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosListagemUsuario> cadastrarUsuario(
            @RequestBody @Valid DadosCadastroUsuario dados,
            UriComponentsBuilder uriBuilder) {

        if (repository.existsByLogin(dados.login())) {
            throw new ValidacaoException("Já existe um usuário cadastrado com esse login.");
        }

        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        Usuario usuario = new Usuario(dados, senhaCriptografada);
        repository.save(usuario);

        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DadosListagemUsuario>> listarUsuarios(
            @PageableDefault(size = 10, sort = {"login"}) Pageable paginacao) {

        Page<DadosListagemUsuario> page = repository.findAll(paginacao).map(DadosListagemUsuario::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosListagemUsuario> atualizarPerfilUsuario(
            @RequestBody @Valid DadosAtualizacaoPerfilUsuario dados) {

        Usuario usuario = repository.findById(dados.id())
                .orElseThrow(EntityNotFoundException::new);

        usuario.atualizarPerfil(dados);

        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        repository.delete(usuario);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/alterar-senha")
    @Transactional
    public ResponseEntity<Void> alterarPropriaSenha(
            @RequestBody @Valid DadosAlteracaoSenha dados,
            Authentication authentication) {

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        Usuario usuario = repository.findById(usuarioLogado.getId())
                .orElseThrow(EntityNotFoundException::new);

        if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getSenha())) {
            throw new ValidacaoException("Senha atual inválida.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(dados.novaSenha());
        usuario.alterarSenha(novaSenhaCriptografada);

        return ResponseEntity.noContent().build();
    }
}