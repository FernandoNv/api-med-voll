package med.voll.api.domain.pessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    public void atualizarDados(DadosAtualizacaoPessoa dados) {
        if(dados.nome() != null){
            this.nome = dados.nome();
        }

        if(dados.imagemUrl() != null){
            this.imagemUrl = dados.imagemUrl();
        }

        this.usuario.atualizarDados(dados);
    }
}
