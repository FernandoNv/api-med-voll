package med.voll.api.domain.pessoa;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPessoa(
        @NotNull(message = "{id.obrigatorio}")
        Long id,
        String nome,
        String imagemUrl,
        String login,
        String senha
) {
}
