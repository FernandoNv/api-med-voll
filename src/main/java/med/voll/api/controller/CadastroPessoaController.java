package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.pessoa.DadosCadastroPessoa;
import med.voll.api.domain.pessoa.DadosDetalhamentoPessoa;
import med.voll.api.domain.pessoa.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/user/signup")
public class CadastroPessoaController {
    private final PessoaService _pessoaService;

    @Autowired
    public CadastroPessoaController(PessoaService pessoaService) {
        _pessoaService = pessoaService;
    }
    @PostMapping
    public ResponseEntity<DadosDetalhamentoPessoa> cadastrarPessoa(@RequestBody @Valid DadosCadastroPessoa dadosCadastroPessoa, UriComponentsBuilder uriComponentsBuilder){
        var dadosDetalhamentoPessoa = _pessoaService.cadastrar(dadosCadastroPessoa);
        var uri = uriComponentsBuilder.path("/pessoas/{id}")
                .buildAndExpand(dadosDetalhamentoPessoa.id())
                .toUri();

        return ResponseEntity.created(uri).body(dadosDetalhamentoPessoa);
    }
}
