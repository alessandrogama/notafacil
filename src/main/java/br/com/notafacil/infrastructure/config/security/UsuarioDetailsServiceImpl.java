package br.com.notafacil.infrastructure.config.security;

import br.com.notafacil.infrastructure.adapter.out.persistence.UsuarioJpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioJpaRepository repository;

    public UsuarioDetailsServiceImpl(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        var authorities = usuario.getRoles().stream()
                .map(role -> "ROLE_" + role.name())
                .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                .toList();

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenhaHash())
                .authorities(authorities)
                .disabled(!usuario.isAtivo())
                .build();
    }
}