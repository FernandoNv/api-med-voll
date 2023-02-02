package med.voll.api.service;

import med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.DadosListagemPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {
    private final PacienteRepository _pacienteRepository;

    @Autowired
    public PacienteService(PacienteRepository _pacienteRepository) {
        this._pacienteRepository = _pacienteRepository;
    }

    @Transactional
    public Paciente cadastrar(DadosCadastroPaciente dados){
        return this._pacienteRepository.save(new Paciente(dados));
    }

    public Page<DadosListagemPaciente> listar(Pageable paginacao){
        return this._pacienteRepository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
    }

    @Transactional
    public Paciente atualizarInformacoes(DadosAtualizacaoPaciente dados) {
        Paciente paciente = this._pacienteRepository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);

        return paciente;
    }

    @Transactional
    public void deletar(Long id){
        Paciente paciente = this._pacienteRepository.getReferenceById(id);
        paciente.deletar();
    }

    public Paciente detalhar(Long id) {
        return this._pacienteRepository.getReferenceById(id);
    }
}
