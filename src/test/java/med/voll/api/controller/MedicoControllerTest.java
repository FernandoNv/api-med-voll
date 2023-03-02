package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.MedicoService;
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

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;
    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;
    @MockBean
    private MedicoService medicoService;

    private final String baseUrl = "/api/v1/medicos";

    @Test
    @DisplayName("cadastrar - Deve devolver codigo HTTP 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        var response = mvc.perform(MockMvcRequestBuilders.post(baseUrl)).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("cadastrar - Deve devolver codigo HTTP 200 quando informacoes estao validas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        var dadosEspecialidade = Especialidade.CARDIOLOGIA;
        var dadosEndereco = new DadosEndereco("Rua Exemplo", "Bairro", "12345123", "Cidade", "RJ", null, null);
        var dadosCadastro = new DadosCadastroMedico("Fernando", "fernando@email.com", "21123451234", "123456", dadosEspecialidade, dadosEndereco);
        var dadosDetalhamento = new DadosDetalhamentoMedico(null, "Fernando", "fernando@email.com", "123456", "21123451234", dadosEspecialidade, new Endereco(dadosEndereco));
        Mockito.when(this.medicoService.cadastrar(Mockito.any())).thenReturn(dadosDetalhamento);

        var response = mvc.perform(
                MockMvcRequestBuilders.post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.dadosCadastroMedicoJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();
        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}