package med.voll.api.domain.consulta.validacoes.cancelamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorCancelamentoAntecedencia implements ValidadorCancelamentoConsulta {
    private final ConsultaRepository _consultaRepository;
    @Autowired
    public ValidadorCancelamentoAntecedencia(ConsultaRepository consultaRepository) {
        _consultaRepository = consultaRepository;
    }
    @Override
    public void validar(DadosCancelamentoConsulta dados) {
        var consulta = _consultaRepository.getReferenceById(dados.idConsulta());
        var horaConsulta = consulta.getData();
        var agora = LocalDateTime.now();
        var diferernca = Duration.between(agora, horaConsulta).toHours();

        if(diferernca < 24)
            throw new ValidacaoException("O cancelamento deve ser feito com no mínimo 24 horas de antecedencia");
    }
}
