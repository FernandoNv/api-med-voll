package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {
    private final MedicoService _medicoService;

    @Autowired
    public MedicoController(MedicoService _medicoService) {
        this._medicoService = _medicoService;
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoMedico> cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriComponentsBuilder){
        DadosDetalhamentoMedico medico = this._medicoService.cadastrar(dados);
        URI uri = uriComponentsBuilder
                .path("/medicos/{id}")
                .buildAndExpand(medico.id())
                .toUri();

        return ResponseEntity.created(uri).body(medico);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, page = 0, sort = {"nome"}) Pageable paginacao){
        Page<DadosListagemMedico> page = this._medicoService.listar(paginacao);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> verificarExistencia(
            @RequestParam(name = "tipo") Optional<String> tipoInformado,
            @RequestParam(name = "email", required = false) Optional<String> emailInformado,
            @RequestParam(name = "crm", required = false) Optional<String> crmInformado
    ){
        Boolean resultado = true;
        String tipo = tipoInformado.orElseThrow();

        if(Objects.equals(tipo, "email")){
            resultado = _medicoService.emailMedicoJaCadastrado(emailInformado.orElseThrow());
        }
        if(Objects.equals(tipo, "crm")){
            resultado = _medicoService.crmMedicoJaCadastrado(crmInformado.orElseThrow());
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoMedico> detalhar(@PathVariable Long id){
        DadosDetalhamentoMedico medico = _medicoService.detalhar(id);

        return ResponseEntity.ok((medico));
    }

    @PutMapping
    public ResponseEntity<DadosDetalhamentoMedico> atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        DadosDetalhamentoMedico medico = this._medicoService.atualizarInformacoes(dados);

        return ResponseEntity.ok(medico);
    }

    @DeleteMapping("/{id}" )
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        this._medicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
