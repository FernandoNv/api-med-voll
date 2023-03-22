package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final PasswordEncoder _passwordEncoder;
    private final UsuarioRepository _usuarioRepository;
    @Autowired
    public UsuarioService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        _passwordEncoder = passwordEncoder;
        _usuarioRepository = usuarioRepository;
    }

    public String encodePassword(String senha){
        return this._passwordEncoder.encode(senha);
    }
    public Usuario findByLogin(String login){
        return _usuarioRepository.getReferenceUsuarioByLogin(login);
    }
    public Usuario novoUsuario(DadosCadastroUsuario dadosCadastroUsuario){
        var usuario = new Usuario();
        usuario.setLogin(dadosCadastroUsuario.login());
        usuario.setSenha(encodePassword(dadosCadastroUsuario.senha()));

        return usuario;
    }
}
