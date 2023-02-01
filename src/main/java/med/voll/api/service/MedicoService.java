package med.voll.api.service;

import med.voll.api.medico.DadosAtualizacaoMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosListagemMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {
    private MedicoRepository _medicoRepository;

    public MedicoService(MedicoRepository _medicoRepository) {
        this._medicoRepository = _medicoRepository;
    }

    @Transactional
    public void cadastrar(DadosCadastroMedico dados){
        this._medicoRepository.save(new Medico(dados));
    }

    @Transactional
    public void atualizarInformacoes(DadosAtualizacaoMedico dados){
        Medico medico = this._medicoRepository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }

    public Page<DadosListagemMedico> listar(Pageable paginacao){
        return this._medicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    }

    @Transactional
    public void deletar(Long id) {
        // forma destrutiva
        // nao usar
        //this._medicoRepository.deleteById(id);

        Medico medico = _medicoRepository.getReferenceById(id);
        medico.deletar();
    }
}
