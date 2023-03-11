package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultaService {
    private final ConsultaRepository _consultaRepository;
    private final PacienteRepository _pacienteRepository;
    private final MedicoRepository _medicoRepository;
    private final List<ValidadorAgendamentoConsulta> _validadoresConsulta;
    private final List<ValidadorCancelamentoConsulta> _validadoresCancelamentoConsulta;
    @Autowired
    public ConsultaService(
            ConsultaRepository consultaRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            List<ValidadorAgendamentoConsulta> validadoresConsulta,
            List<ValidadorCancelamentoConsulta> validadoresCancelamentoConsulta
    ) {
        _consultaRepository = consultaRepository;
        _pacienteRepository = pacienteRepository;
        _medicoRepository = medicoRepository;
        _validadoresConsulta = validadoresConsulta;
        _validadoresCancelamentoConsulta = validadoresCancelamentoConsulta;
    }

    @Transactional
    public DadosDetalhamentoConsulta agendarConsulta(DadosAgendamentoConsulta dados){
        if(!_pacienteRepository.existsById(dados.idPaciente()))
            throw new ValidacaoException("Id do paciênte informado não existe");

        if( dados.idMedico() != null && !_medicoRepository.existsById(dados.idMedico()))
            throw new ValidacaoException("Id do médico informado não existe");

        _validadoresConsulta.forEach(v -> v.validar(dados));

        var medico = this.escolherMedico(dados);
        if(medico == null)
            throw new ValidacaoException("Não existe médico disponível para essa data!");

        var paciente = _pacienteRepository.getReferenceById(dados.idPaciente());
        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        consulta = _consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico() != null)
            return _medicoRepository.getReferenceById(dados.idMedico());

        if(dados.especialidade() == null)
            throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido!");

        return _medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }

    @Transactional
    public Consulta cancelamentoConsulta(DadosCancelamentoConsulta dados) {
        if(!_consultaRepository.existsById(dados.idConsulta()))
            throw new ValidacaoException("Id da consulta inválido");

        _validadoresCancelamentoConsulta.forEach(v -> v.validar(dados));

        var consulta = _consultaRepository.findById(dados.idConsulta()).get();
        consulta.cancelar(dados.motivoCancelamento());

        return consulta;
    }
}
