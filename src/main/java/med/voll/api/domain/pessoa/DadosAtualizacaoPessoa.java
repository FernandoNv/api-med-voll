package med.voll.api.domain.pessoa;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DadosAtualizacaoPessoa(
        @NotNull(message = "{id.obrigatorio}")
        Long id,
        String nome,
        String imagemUrl,
        String login,
        String senha,
        Genero genero,
        LocalDate dataNascimento
) {
}
