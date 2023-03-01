package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoConsulta {
    private final MedicoRepository _medicoRepository;

    @Autowired
    public ValidadorMedicoAtivo(MedicoRepository medicoRepository) {
        _medicoRepository = medicoRepository;
    }

    @Override
    public void validar(DadosAgendamentoConsulta dados) {
        // medico opcional
        if(dados.idMedico() == null){
            return;
        }

        var medicoEstaAtivo = _medicoRepository.findAtivoById(dados.idMedico());
        if(!medicoEstaAtivo){
            throw new ValidacaoException("Consulta não pode ser agendad com médico inativo");
        }
    }
}
