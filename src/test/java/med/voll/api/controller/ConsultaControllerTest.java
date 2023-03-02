package med.voll.api.controller;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.ConsultaService;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.consulta.MotivoCancelamento;
import med.voll.api.domain.medico.Especialidade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;
    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;
    @Autowired
    private JacksonTester<DadosCancelamentoConsulta> dadosCancelamentoConsultaJson;
    @MockBean
    private ConsultaService consultaService;
    private final String baseUrl = "/api/v1/consultas";

    @Test
    @DisplayName("agendar - Deve devolver codigo HTTP 400 quando informacoes estao invalidas")
    @WithMockUser
    void agendarCenario1() throws Exception {
        var response = mvc.perform(MockMvcRequestBuilders.post(baseUrl)).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("agendar - Deve devolver codigo HTTP 200 quando informações estao válidas")
    @WithMockUser
    void agendarCenario2() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;
        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2L, 5L, data);
        Mockito.when(this.consultaService.agendarConsulta(Mockito.any())).thenReturn(dadosDetalhamento);

        var response = mvc.perform(
                MockMvcRequestBuilders
                        .post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                dadosAgendamentoConsultaJson.write(
                                        new DadosAgendamentoConsulta(2L, 5L, especialidade, data)
                                ).getJson()
                        )
        ).andReturn().getResponse();
        var jsonEsperado = dadosDetalhamentoConsultaJson.write(dadosDetalhamento).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("cancelamento - Deve devolver codigo HTTP 400 quando informacoes estao invalidas")
    @WithMockUser
    void cancelamentoCenario1() throws Exception {
        var response = mvc.perform(MockMvcRequestBuilders.delete(baseUrl)).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("cancelamento - Deve devolver codigo HTTP 204 quando informacoes estao validas")
    @WithMockUser
    void cancelamentoCenario2() throws Exception{
        var motivo = MotivoCancelamento.MEDICO_CANCELOU;
        var dadosCancelamento = new DadosCancelamentoConsulta(1L, motivo);
        Mockito.when(this.consultaService.cancelamentoConsulta(Mockito.any())).thenReturn(new Consulta());

        var response = mvc.perform(
                MockMvcRequestBuilders.delete(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCancelamentoConsultaJson.write(dadosCancelamento).getJson())
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}