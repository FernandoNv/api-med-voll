package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@DataJpaTest // utilizada para teste de classes repository
// manda o spring boot não substituir o SGBD utilizado
// na aplicação por um em memória
// (melhor por ser mais fiel ao ambiente da aplicação porém é mais demorado paa efetuar os testes)
// Utilizar um banco de dados exclusivo para teste
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// diz que o prefixo do bandco é teste(ex: banco). O spring boot vai procurar por um bd chamado banco-test
@ActiveProfiles("test")
class MedicoRepositoryTest {
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private TestEntityManager em;
    private Medico medico;
    private Paciente paciente;

    @BeforeEach
    public void initEach(){
        //test setup code
        this.medico = cadastrarMedico("Medico M", "medico@email.com", "123456", Especialidade.CARDIOLOGIA);
        this.paciente = cadastrarPaciente("Paciente P", "paciente@email.com", "123456789");
    }

    @Test
    @DisplayName("Deveria devolver null quando unico medico cadastrado não está disponível na data")
    void escolherMedicoAleatorioLivreNaDataCenario1() {
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        cadastrarConsulta(this.medico, this.paciente, proximaSegundaAs10);

        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        Assertions.assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deveria devolver medico válidoc quando eles estiver disponível na data")
    void escolherMedicoAleatorioLivreNaDataCenario2(){
        //given ou arrange
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        // when ou act
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        //assert
        Assertions.assertThat(medicoLivre).isEqualTo(this.medico);
    }

    @Test
    @DisplayName("Deve devolver false quando passado id de um medico inativo")
    void findAtivoByIdCenario1() {
        //action
        this.medico.deletar();

        //when
        var medicoEncontrado = medicoRepository.findAtivoById(this.medico.getId());

        Assertions.assertThat(medicoEncontrado).isFalse();
    }

    @Test
    @DisplayName("Deve devolver true quando passado id de um medico ativo")
    void findAtivoByIdCenario2() {
        var medicoEncontrado = medicoRepository.findAtivoById(this.medico.getId());

        Assertions.assertThat(medicoEncontrado).isTrue();
    }

    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data){
        var consulta = new Consulta(null, medico, paciente, data, null);
        this.em.persist(consulta);
    }

    private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade){
        var medico = new Medico(this.dadosMedico(nome, email, crm, especialidade));
        this.em.persist(medico);

        return medico;
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf){
        var paciente = new Paciente(this.dadosPaciente(nome, email, cpf));
        this.em.persist(paciente);

        return paciente;
    }

    private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade){
        return new DadosCadastroMedico(
                nome,
                email,
                "6112341234",
                crm,
                especialidade,
                this.dadosEndereco()
        );
    }

    private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf){
        return new DadosCadastroPaciente(
                nome,
                email,
                "6112341234",
                cpf,
                this.dadosEndereco()
        );
    }

    private DadosEndereco dadosEndereco(){
        return new DadosEndereco(
                "Rua exemplo",
                "bairro",
                "09876123",
                "Rio de Janeiro",
                "RJ",
                null,
                null
        );
    }
}