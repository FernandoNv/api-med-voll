package med.voll.api.domain.pessoa;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.usuario.DadosCadastroUsuario;

import java.time.LocalDate;

public record DadosCadastroPessoa(
        @NotNull
        String nome,
        @NotNull
        LocalDate dataNascimento,
        @NotNull
        Genero genero,
        @NotNull
        @Valid
        DadosCadastroUsuario dadosCadastroUsuario) {
}
