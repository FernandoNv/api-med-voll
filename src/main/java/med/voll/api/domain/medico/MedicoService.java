package med.voll.api.domain.medico;
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
    public DadosDetalhamentoMedico cadastrar(DadosCadastroMedico dados){
        Medico medico = this._medicoRepository.save(new Medico(dados));

        return new DadosDetalhamentoMedico(medico);
    }

    @Transactional
    public DadosDetalhamentoMedico atualizarInformacoes(DadosAtualizacaoMedico dados){
        Medico medico = this._medicoRepository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return new DadosDetalhamentoMedico(medico);
    }

    public Page<DadosListagemMedico> listar(Pageable paginacao){
        return this._medicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    }

    @Transactional
    public DadosDetalhamentoMedico deletar(Long id) {
        // forma destrutiva
        // nao usar
        //this._medicoRepository.deleteById(id);

        Medico medico = _medicoRepository.getReferenceById(id);
        medico.deletar();

        return new DadosDetalhamentoMedico(medico);
    }

    public DadosDetalhamentoMedico detalhar(Long id){
        Medico medico =  this._medicoRepository.getReferenceById(id);

        return new DadosDetalhamentoMedico(medico);
    }

    public Boolean crmMedicoJaCadastrado(String crm) {
        return this._medicoRepository.existisByCrm(crm);
    }

    public Boolean emailMedicoJaCadastrado(String email) {
        return this._medicoRepository.existisByEmail(email);
    }
}
