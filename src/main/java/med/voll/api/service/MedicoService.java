package med.voll.api.service;

import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {
    private final MedicoRepository _medicoRepository;

    @Autowired
    public MedicoService(MedicoRepository _medicoRepository) {
        this._medicoRepository = _medicoRepository;
    }

    @Transactional
    public Medico cadastrar(DadosCadastroMedico dados){
        return this._medicoRepository.save(new Medico(dados));
    }

    @Transactional
    public Medico atualizarInformacoes(DadosAtualizacaoMedico dados){
        Medico medico = this._medicoRepository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return medico;
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

    public Medico detalhar(Long id){
        return this._medicoRepository.getReferenceById(id);
    }
}
