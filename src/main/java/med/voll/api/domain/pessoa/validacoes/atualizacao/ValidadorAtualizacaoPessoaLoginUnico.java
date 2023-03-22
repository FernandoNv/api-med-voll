package med.voll.api.domain.pessoa.validacoes.atualizacao;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.pessoa.DadosAtualizacaoPessoa;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorAtualizacaoPessoaLoginUnico implements ValidadorAtualizacaoPessoa {
    private final UsuarioRepository _usuarioRepository;
    @Autowired
    public ValidadorAtualizacaoPessoaLoginUnico(UsuarioRepository usuarioRepository) {
        _usuarioRepository = usuarioRepository;
    }

    @Override
    public void validar(DadosAtualizacaoPessoa dados) {
        String login = dados.login();
        if(dados.login() == null){
            return;
        }

        var usuario = _usuarioRepository.getReferenceUsuarioByLogin(login);
        if(usuario.getId().equals(dados.id())){
            throw new ValidacaoException("Email já cadastrado");
        }
    }
}
