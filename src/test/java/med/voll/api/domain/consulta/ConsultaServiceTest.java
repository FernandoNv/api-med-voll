package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoConsulta;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConsultaServiceTest {
    @Mock
    private ConsultaRepository consultaRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private MedicoRepository medicoRepository;
    @Mock
    private List<ValidadorAgendamentoConsulta> validadoresConsulta;
    @Mock
    private List<ValidadorCancelamentoConsulta> validadorCancelamentoConsulta;
    @InjectMocks
    ConsultaService consultaService;

    @Test
    @DisplayName("agendarConsulta - Deveria falhar ao passar um id de paciente inválido")
    void agendarConsultaCenario1() {
        var dadosAgendamentoConsulta = new DadosAgendamentoConsulta(1l, 2L, Especialidade.CARDIOLOGIA, LocalDateTime.now().plusHours(2));
        Mockito.when(this.pacienteRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        try{
            consultaService.agendarConsulta(dadosAgendamentoConsulta);
            fail("Não deu a exception esperada");
        }catch (ValidacaoException e){
            var mensagem = e.getMessage();

            Assertions.assertEquals("Id do paciênte informado não existe", mensagem);
        }
    }

    @Test
    @DisplayName("agendarConsulta - Deveria falhar ao passar um id de medico inválido")
    void agendarConsultaCenario2() {
        var dadosAgendamentoConsulta = new DadosAgendamentoConsulta(1l, 2L, Especialidade.CARDIOLOGIA, LocalDateTime.now().plusHours(2));
        Mockito.when(this.pacienteRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(this.medicoRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        try{
            consultaService.agendarConsulta(dadosAgendamentoConsulta);
            fail("Não deu a exception esperada");
        }catch (ValidacaoException e){
            var mensagem = e.getMessage();

            Assertions.assertEquals("Id do médico informado não existe", mensagem);
        }
    }

    @Test
    @DisplayName("agendarConsulta - Deveria falhar ao não encontrar um médico disponivel")
    void agendarConsultaCenario3() {
        var dadosAgendamentoConsulta = new DadosAgendamentoConsulta(null, 2L, Especialidade.CARDIOLOGIA, LocalDateTime.now().plusHours(2));
        Mockito.when(this.pacienteRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(this.medicoRepository.escolherMedicoAleatorioLivreNaData(Mockito.any(Especialidade.class), Mockito.any(LocalDateTime.class))).thenReturn(null);

        try{
            consultaService.agendarConsulta(dadosAgendamentoConsulta);
            fail("Não deu a exception esperada");
        }catch (ValidacaoException e){
            var mensagem = e.getMessage();

            Assertions.assertEquals("Não existe médico disponível para essa data!", mensagem);
        }
    }

    @Test
    @DisplayName("agendarConsulta - Deveria falhar ao não passar um médico e a especialidade")
    void agendarConsultaCenario4() {
        var dadosAgendamentoConsulta = new DadosAgendamentoConsulta(null, 2L, null, LocalDateTime.now().plusHours(2));
        Mockito.when(this.pacienteRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(this.medicoRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        try{
            consultaService.agendarConsulta(dadosAgendamentoConsulta);
            fail("Não deu a exception esperada");
        }catch (ValidacaoException e){
            var mensagem = e.getMessage();

            Assertions.assertEquals("Especialidade é obrigatória quando médico não for escolhido!", mensagem);
        }
    }

    @Test
    @DisplayName("agendarConsulta - Deveria cadastrar a consulta quando o id do médico não é informado mas é possível encontrar outro disponível")
    void agendarConsultaCenario5() {
        var especialidade = Especialidade.CARDIOLOGIA;
        var endereco = new Endereco();
        var dataConsulta = LocalDateTime.now().plusHours(2);
        var dadosAgendamentoConsulta = new DadosAgendamentoConsulta(null, 2L, especialidade, dataConsulta);
        var medico = new Medico(1L, "Fernando", "123456", "email@email.com", "21987654321", true, especialidade, endereco);
        var dadosDetalhamentoConsulta = new DadosDetalhamentoConsulta(1L, 1L, 2L, dataConsulta);
        var paciente = new Paciente(2L,"Fernando Vieira", "email2@email.com", "21987650321", "12312312312", true, endereco);
        var consulta = new Consulta(1L, medico, paciente, dataConsulta, null);

        Mockito.when(this.pacienteRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(this.medicoRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(this.medicoRepository.escolherMedicoAleatorioLivreNaData(Mockito.any(Especialidade.class), Mockito.any(LocalDateTime.class))).thenReturn(new Medico());
        Mockito.when(this.pacienteRepository.getReferenceById(Mockito.any(Long.class))).thenReturn(paciente);
        Mockito.when(this.consultaRepository.save(Mockito.any(Consulta.class))).thenReturn(consulta);

        DadosDetalhamentoConsulta esperado = consultaService.agendarConsulta(dadosAgendamentoConsulta);

        Assertions.assertEquals(dadosDetalhamentoConsulta, esperado);
    }

    @Test
    @DisplayName("agendarConsulta - Deveria cadastrar a consulta quando o id do paciente e médico são válidos")
    void agendarConsultaCenario6() {
        var especialidade = Especialidade.CARDIOLOGIA;
        var endereco = new Endereco();
        var dataConsulta = LocalDateTime.now().plusHours(2);
        var dadosAgendamentoConsulta = new DadosAgendamentoConsulta(1L, 2L, null, dataConsulta);
        var medico = new Medico(1L, "Fernando", "123456", "email@email.com", "21987654321", true, especialidade, endereco);
        var dadosDetalhamentoConsulta = new DadosDetalhamentoConsulta(1L, 1L, 2L, dataConsulta);
        var paciente = new Paciente(2L,"Fernando Vieira", "email2@email.com", "21987650321", "12312312312", true, endereco);
        var consulta = new Consulta(1L, medico, paciente, dataConsulta, null);

        Mockito.when(this.pacienteRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(this.medicoRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(this.pacienteRepository.getReferenceById(Mockito.any(Long.class))).thenReturn(paciente);
        Mockito.when(this.medicoRepository.getReferenceById(Mockito.any(Long.class))).thenReturn(medico);
        Mockito.when(this.consultaRepository.save(Mockito.any(Consulta.class))).thenReturn(consulta);

        DadosDetalhamentoConsulta esperado = consultaService.agendarConsulta(dadosAgendamentoConsulta);

        Assertions.assertEquals(dadosDetalhamentoConsulta, esperado);
    }

    @Test
    @DisplayName("cancelamentoConsulta - Deveria falhar quando o id da consulta for inválido")
    void cancelamentoConsultaCenario1() {
        var dadosCaelamentoConsulta = new DadosCancelamentoConsulta(1L, MotivoCancelamento.OUTROS);
         Mockito.when(consultaRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        try{
            consultaService.cancelamentoConsulta(dadosCaelamentoConsulta);
            fail("Não deu a exception esperada");
        }catch (ValidacaoException e){
            var mensagem = e.getMessage();

            Assertions.assertEquals("Id da consulta inválido", mensagem);
        }
    }

    @Test
    @DisplayName("cancelamentoConsulta - Deveria cancelar consulta quando o id for válido")
    void cancelamentoConsultaCenario2() {
        var dadosCaelamentoConsulta = new DadosCancelamentoConsulta(1L, MotivoCancelamento.OUTROS);
        var especialidade = Especialidade.CARDIOLOGIA;
        var endereco = new Endereco();
        var medico = new Medico(1L, "Fernando", "123456", "email@email.com", "21987654321", true, especialidade, endereco);
        var paciente = new Paciente(2L,"Fernando Vieira", "email2@email.com", "21987650321", "12312312312", true, endereco);
        var consulta = new Consulta(1L, medico, paciente,  LocalDateTime.now().plusHours(2), null);
        Mockito.when(consultaRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.when(consultaRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(consulta));

        Consulta retorno = consultaService.cancelamentoConsulta(dadosCaelamentoConsulta);

        Assertions.assertNotNull(retorno);
        Assertions.assertEquals(dadosCaelamentoConsulta.motivoCancelamento(), retorno.getMotivoCancelamento());
    }
}