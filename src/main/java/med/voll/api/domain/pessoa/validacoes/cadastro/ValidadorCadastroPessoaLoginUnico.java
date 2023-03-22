package med.voll.api.domain.pessoa.validacoes.cadastro;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.pessoa.DadosCadastroPessoa;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCadastroPessoaLoginUnico implements ValidadorCadastroPessoa{
    private final UsuarioRepository _usuarioRepository;

    @Autowired
    public ValidadorCadastroPessoaLoginUnico(UsuarioRepository usuarioRepository) {
        _usuarioRepository = usuarioRepository;
    }

    @Override
    public void validar(DadosCadastroPessoa dados) {
        if(_usuarioRepository.existsByLogin(dados.dadosCadastroUsuario().login())){
            throw new ValidacaoException("Email já cadastrado");
        }
    }
}
