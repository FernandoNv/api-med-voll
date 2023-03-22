package med.voll.api.domain.pessoa;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.pessoa.validacoes.atualizacao.ValidadorAtualizacaoPessoa;
import med.voll.api.domain.pessoa.validacoes.cadastro.ValidadorCadastroPessoa;
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
    private final List<ValidadorCadastroPessoa> _validadoresCadastroPessoa;

    @Autowired
    public PessoaService(
            PessoaRepository pessoaRepository,
            UsuarioService usuarioService,
            List<ValidadorAtualizacaoPessoa> validadoresAtualizacaoPessoa,
            List<ValidadorCadastroPessoa> validadoresCadastroPessoa
    ) {
        _pessoaRepository = pessoaRepository;
        _usuarioService = usuarioService;
        _validadoresAtualizacaoPessoa = validadoresAtualizacaoPessoa;
        _validadoresCadastroPessoa = validadoresCadastroPessoa;
    }

    public DadosDetalhamentoPessoa detalhar(String login){
        Usuario usuario = _usuarioService.findByLogin(login);
        Pessoa pessoa = _pessoaRepository.getReferenceById(usuario.getId());

        return new DadosDetalhamentoPessoa(pessoa);
    }

    public DadosDetalhamentoPessoa cadastrar(DadosCadastroPessoa dadosCadastroPessoa){
        for(var validador: _validadoresCadastroPessoa){
            validador.validar(dadosCadastroPessoa);
        }

        var usuario = _usuarioService.novoUsuario(dadosCadastroPessoa.dadosCadastroUsuario());
        var pessoa = new Pessoa(
                null,
                dadosCadastroPessoa.nome(),
                null,
                usuario,
                dadosCadastroPessoa.genero(),
                dadosCadastroPessoa.dataNascimento()
        );

        var pessoaCadastrada = _pessoaRepository.save(pessoa);

        return new DadosDetalhamentoPessoa(pessoaCadastrada);
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

        return new DadosAtualizacaoPessoa(
                dados.id(),
                dados.nome(),
                dados.imagemUrl(),
                dados.login(),
                novaSenha,
                dados.genero(),
                dados.dataNascimento()
        );
    }
}
