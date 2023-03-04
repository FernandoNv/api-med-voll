package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.MedicoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private JacksonTester<DadosAtualizacaoMedico> dadosAtualizacaoMedicoJson;
    @MockBean
    private MedicoService medicoService;
    private final String baseUrl = "/api/v1/medicos";
    private DadosDetalhamentoMedico dadosDetalhamentoMedico;
    private DadosDetalhamentoMedico dadosDetalhamentoMedicoComID;
    private final Long idMedicoMock = 1L;
    private DadosCadastroMedico dadosCadastroMedico;
    private DadosAtualizacaoMedico dadosAtualizacaoMedico;
    @BeforeEach
    public void beforeAll(){
        this.dadosCadastroMedico = criarDadosCadastro();
        this.dadosDetalhamentoMedico = criarDadosDetalhamento();
        this.dadosDetalhamentoMedicoComID = criarDadosDetalhamentoComId(idMedicoMock);
        this.dadosAtualizacaoMedico = criarDadosAtualizacao(idMedicoMock);
    }

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
        Mockito.when(this.medicoService.cadastrar(Mockito.any())).thenReturn(this.dadosDetalhamentoMedico);

        var response = mvc.perform(
                MockMvcRequestBuilders.post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.dadosCadastroMedicoJson.write(this.dadosCadastroMedico).getJson())
        ).andReturn().getResponse();
        var jsonEsperado = dadosDetalhamentoMedicoJson.write(this.dadosDetalhamentoMedico).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
    @Test
    @DisplayName("detalhar - Deve devolver codigo HTTP 200 quando o id passado é válido")
    @WithMockUser
    public void detalharCenario1() throws Exception {
        Mockito.when(this.medicoService.detalhar(idMedicoMock)).thenReturn(dadosDetalhamentoMedicoComID);

        var response = mvc.perform(
                MockMvcRequestBuilders.get(baseUrl+"/"+idMedicoMock).contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamentoMedicoComID).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
    @Test
    @DisplayName("escluir - Deve retornar Http codigo 204 quando passado um id válido")
    @WithMockUser
    public void excluir() throws Exception {
        Mockito.when(medicoService.deletar(idMedicoMock)).thenReturn(dadosDetalhamentoMedicoComID);

        var response = mvc.perform(
                MockMvcRequestBuilders.delete(baseUrl+"/"+idMedicoMock).contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("atualizar - Deve retornar Http codigo 400 quando informacoes passadas estão invalidas")
    @WithMockUser
    public void atualizarCenario1() throws Exception {
        var response = mvc.perform(
                MockMvcRequestBuilders.put(baseUrl).contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("atualizar - Deve retornar Http codigo 200 quando informações passadas estão válidas")
    @WithMockUser
    public void atualizarCenario2() throws Exception {
        Mockito.when(this.medicoService.atualizarInformacoes(dadosAtualizacaoMedico)).thenReturn(dadosDetalhamentoMedicoComID);
        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamentoMedicoComID).getJson();

        var response = mvc.perform(
                MockMvcRequestBuilders.put(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAtualizacaoMedicoJson.write(dadosAtualizacaoMedico).getJson())
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    private DadosEndereco criarEndereco(){
        return new DadosEndereco("Rua Exemplo", "Bairro", "12345123", "Cidade", "RJ", null, null);
    }

    private DadosDetalhamentoMedico criarDadosDetalhamento(){
        var dadosCadastro = criarDadosCadastro();
        var dadosDetalhamento = new DadosDetalhamentoMedico(null, "Fernando", "fernando@email.com", "123456", "21123451234", dadosCadastro.especialidade(), new Endereco(dadosCadastro.endereco()));

        return dadosDetalhamento;
    }

    private DadosDetalhamentoMedico criarDadosDetalhamentoComId(Long id){
        var dadosCadastro = criarDadosCadastro();
        var dadosDetalhamento = new DadosDetalhamentoMedico(id, "Fernando", "fernando@email.com", "123456", "21123451234", dadosCadastro.especialidade(), new Endereco(dadosCadastro.endereco()));

        return dadosDetalhamento;
    }

    private DadosCadastroMedico criarDadosCadastro(){
        var dadosEspecialidade = Especialidade.CARDIOLOGIA;
        var dadosEndereco = this.criarEndereco();
        var dadosCadastro = new DadosCadastroMedico("Fernando", "fernando@email.com", "21123451234", "123456", dadosEspecialidade, dadosEndereco);

        return dadosCadastro;
    }

    private DadosAtualizacaoMedico criarDadosAtualizacao(Long idMedicoMock) {
        return new DadosAtualizacaoMedico(idMedicoMock, "Fernando", "21123451234", criarEndereco());
    }
}