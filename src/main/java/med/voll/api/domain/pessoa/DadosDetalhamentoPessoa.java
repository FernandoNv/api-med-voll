package med.voll.api.domain.pessoa;

public record DadosDetalhamentoPessoa(
        Long id,
        String nome,
        String imagemUrl,
        String login
) {
    public DadosDetalhamentoPessoa(Pessoa pessoa){
        this(pessoa.getId(), pessoa.getNome(), pessoa.getImagemUrl(), pessoa.getUsuario().getLogin());
    }
}
