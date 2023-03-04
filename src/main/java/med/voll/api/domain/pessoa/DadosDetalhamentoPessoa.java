package med.voll.api.domain.pessoa;

import java.time.LocalDate;

public record DadosDetalhamentoPessoa(
        Long id,
        String nome,
        String imagemUrl,
        String login,
        Genero genero,
        LocalDate dataNascimento
) {
    public DadosDetalhamentoPessoa(Pessoa pessoa){
        this(pessoa.getId(), pessoa.getNome(), pessoa.getImagemUrl(), pessoa.getUsuario().getLogin(), pessoa.getGenero(), pessoa.getDataNascimento());
    }
}
