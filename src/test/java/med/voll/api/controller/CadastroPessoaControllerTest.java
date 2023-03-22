package med.voll.api.controller;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.pessoa.DadosCadastroPessoa;
import med.voll.api.domain.pessoa.DadosDetalhamentoPessoa;
import med.voll.api.domain.pessoa.Genero;
import med.voll.api.domain.pessoa.PessoaService;
import med.voll.api.domain.usuario.DadosCadastroUsuario;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CadastroPessoaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<DadosCadastroPessoa> dadosCadastroPessoaJson;
    @Autowired
    private JacksonTester<DadosDetalhamentoPessoa> dadosDetalhamentoPessoaJson;
    @MockBean
    private PessoaService pessoaService;
    private final String baseUrl = "/user/signup";
    @Test
    @DisplayName("cadastrarPessoa - Deve devolver codigo HTTP 400 quando informações estão inválidas")
    void cadastrarPessoa1() throws Exception {
        var response = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("cadastrarPessoa - Deve retornar codigo HTTP 201 quando os dados estão válidos")
    void cadastrarPessoa2() throws Exception{
        var dadosCadastroPessoa = new DadosCadastroPessoa(
                "Carina Laureano",
                LocalDate.of(1997, 12, 21),
                Genero.FEMININO,
                new DadosCadastroUsuario("email@email.com", "123456789")
        );
        var dadosDetalhamentoPessoa = new DadosDetalhamentoPessoa(1L, "Carina Laureano","", "email@email.com", Genero.FEMININO, LocalDate.of(1997, 12, 21));
        var jsonEsperado = dadosDetalhamentoPessoaJson.write(dadosDetalhamentoPessoa).getJson();

        Mockito.when(this.pessoaService.cadastrar(Mockito.any(DadosCadastroPessoa.class))).thenReturn(dadosDetalhamentoPessoa);

        var response = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroPessoaJson.write(dadosCadastroPessoa).getJson())
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
    @Test
    @DisplayName("cadastrarPessoa - Deve devolver codigo HTTP 400 ao passar um email já cadastrado")
    void cadastrarPessoa3() throws Exception{
        var dadosCadastroPessoa = new DadosCadastroPessoa(
                "Carina Laureano",
                LocalDate.of(1997, 12, 21),
                Genero.FEMININO,
                new DadosCadastroUsuario("email@email.com", "123456789")
        );

        Mockito.when(this.pessoaService.cadastrar(Mockito.any(DadosCadastroPessoa.class))).thenThrow(new ValidacaoException("Email já cadastrado"));

        var response = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroPessoaJson.write(dadosCadastroPessoa).getJson())
        ).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo("Email já cadastrado");
    }
}