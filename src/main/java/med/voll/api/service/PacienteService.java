package med.voll.api.service;

import med.voll.api.paciente.DadosAtualizacaoPaciente;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosListagemPaciente;
import med.voll.api.paciente.Paciente;
import med.voll.api.paciente.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {
    private PacienteRepository _pacienteRepository;

    public PacienteService(PacienteRepository _pacienteRepository) {
        this._pacienteRepository = _pacienteRepository;
    }

    @Transactional
    public void cadastrar(DadosCadastroPaciente dados){
        this._pacienteRepository.save(new Paciente(dados));
    }

    public Page<DadosListagemPaciente> listar(Pageable paginacao){
        return this._pacienteRepository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
    }

    @Transactional
    public void atualizarInformacoes(DadosAtualizacaoPaciente dados) {
        Paciente paciente = this._pacienteRepository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);
    }

    @Transactional
    public void deletar(Long id){
        Paciente paciente = this._pacienteRepository.getReferenceById(id);
        paciente.deletar();
    }
}
