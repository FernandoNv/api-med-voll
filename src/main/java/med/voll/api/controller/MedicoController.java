package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.DadosAtualizacaoMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosListagemMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import med.voll.api.service.MedicoService;
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
@RequestMapping("api/v1/medicos")
public class MedicoController {;
    private MedicoService _medicoService;

    @Autowired
    public MedicoController(MedicoService _medicoService) {
        this._medicoService = _medicoService;
    }

    @PostMapping
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados){
        this._medicoService.cadastrar(dados);
    }

    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, page = 0, sort = {"nome"}) Pageable paginacao){
        return this._medicoService.listar(paginacao);
    }

    @PutMapping
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        this._medicoService.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}" )
    public void deletar(@PathVariable Long id){
        this._medicoService.deletar(id);
    }
}
