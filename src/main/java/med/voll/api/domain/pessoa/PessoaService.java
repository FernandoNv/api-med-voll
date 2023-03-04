package med.voll.api.domain.pessoa;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.pessoa.validacoes.ValidadorAtualizacaoPessoa;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.domain.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PessoaService {
    private final PessoaRepository _pessoaRepository;
    private final UsuarioService _usuarioService;
    private final List<ValidadorAtualizacaoPessoa> _validadoresAtualizacaoPessoa;

    @Autowired
    public PessoaService(PessoaRepository pessoaRepository , UsuarioService usuarioService, List<ValidadorAtualizacaoPessoa> validadoresAtualizacaoPessoa) {
        _pessoaRepository = pessoaRepository;
        _usuarioService = usuarioService;
        _validadoresAtualizacaoPessoa = validadoresAtualizacaoPessoa;
    }

    public DadosDetalhamentoPessoa detalhar(String login){
        Usuario usuario = _usuarioService.findByLogin(login);
        Pessoa pessoa = _pessoaRepository.getReferenceById(usuario.getId());

        return new DadosDetalhamentoPessoa(pessoa);
    }

    @Transactional
    public DadosDetalhamentoPessoa atualizar(DadosAtualizacaoPessoa dados){
        if(!_pessoaRepository.existsById(dados.id()))
            throw new ValidacaoException("Pessoa com o id " + dados.id() + " não existe");

        for(var validador : _validadoresAtualizacaoPessoa){
            validador.validar(dados);
        }

        if(dados.senha() != null){
            dados = atualizarSenha(dados);
        }

        Pessoa pessoa = _pessoaRepository.findById(dados.id()).get();
        pessoa.atualizarDados(dados);

        return new DadosDetalhamentoPessoa(pessoa);
    }

    private DadosAtualizacaoPessoa atualizarSenha(DadosAtualizacaoPessoa dados){
        var novaSenha = _usuarioService.encodePassword(dados.senha());
        var novoDados = new DadosAtualizacaoPessoa(
                dados.id(),
                dados.nome(),
                dados.imagemUrl(),
                dados.login(),
                novaSenha,
                dados.genero(),
                dados.dataNascimento()
        );

        return novoDados;
    }
}
