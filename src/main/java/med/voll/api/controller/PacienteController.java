package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.paciente.DadosAtualizacaoPaciente;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosListagemPaciente;
import med.voll.api.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/pacientes")
public class PacienteController {
    private PacienteService _pacienteService;

    @Autowired
    public PacienteController(PacienteService _pacienteService) {
        this._pacienteService = _pacienteService;
    }

    @PostMapping
    public void cadastrar(@RequestBody @Valid DadosCadastroPaciente dados){
        this._pacienteService.cadastrar(dados);
    }

    @GetMapping
    public Page<DadosListagemPaciente> listar(@PageableDefault(sort = {"nome"}, size = 10, page = 0) Pageable paginacao){
        return this._pacienteService.listar(paginacao);
    }

    @PutMapping
    public void atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados){
        this._pacienteService.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        this._pacienteService.deletar(id);
    }
}
