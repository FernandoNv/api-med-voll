package med.voll.api.domain.pessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.usuario.Usuario;

import java.time.LocalDate;

@Entity(name = "Pessoa")
@Table(name = "pessoas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Pessoa {
    @Id
    @Column(name = "usuario_id")
    private Long id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "imagem_url")
    private String imagemUrl;
    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private Genero genero;
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    public void atualizarDados(DadosAtualizacaoPessoa dados) {
        if(dados.nome() != null){
            this.nome = dados.nome();
        }

        if(dados.imagemUrl() != null){
            this.imagemUrl = dados.imagemUrl();
        }

        if (dados.genero() != null){
            this.genero = dados.genero();
        }

        if(dados.dataNascimento() != null){
            this.dataNascimento = dados.dataNascimento();
        }

        this.usuario.atualizarDados(dados);
    }
}
