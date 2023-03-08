package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.DadosDetalhamentoPaciente;
import med.voll.api.domain.paciente.DadosListagemPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteService;
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
@RequestMapping("api/v1/pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {
    private final PacienteService _pacienteService;

    @Autowired
    public PacienteController(PacienteService _pacienteService) {
        this._pacienteService = _pacienteService;
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoPaciente> cadastrar(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriComponentsBuilder){
        Paciente paciente = this._pacienteService.cadastrar(dados);
        URI uri = uriComponentsBuilder
                .path("/pacientes/{id}")
                .buildAndExpand(paciente.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
    }
    @GetMapping("/existe")
    public ResponseEntity<Boolean> verificarExistencia(
            @RequestParam(name = "tipo") Optional<String> tipoInformado,
            @RequestParam(name = "email", required = false) Optional<String> emailInformado,
            @RequestParam(name = "cpf", required = false) Optional<String> cpfInformado
    ){
        Boolean resultado = true;
        String tipo = tipoInformado.orElseThrow();

        if(Objects.equals(tipo, "email")){
            resultado = _pacienteService.emailPacienteJaCadastrado(emailInformado.orElseThrow());
        }
        if(Objects.equals(tipo, "cpf")){
            resultado = _pacienteService.cpfPacienteJaCadastrado(cpfInformado.orElseThrow());
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listar(@PageableDefault(sort = {"nome"}, size = 10, page = 0) Pageable paginacao){
        Page<DadosListagemPaciente> page = this._pacienteService.listar(paginacao);

        return ResponseEntity.ok(page);
    }

    @PutMapping
    public ResponseEntity<DadosDetalhamentoPaciente> atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados){
        Paciente paciente = this._pacienteService.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        this._pacienteService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPaciente> detalhar(@PathVariable Long id){
        Paciente paciente = _pacienteService.detalhar(id);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }
}
