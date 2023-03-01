package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.consulta.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {
    private final ConsultaService _consultaService;
    @Autowired
    public ConsultaController(ConsultaService consultaService) {
        _consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoConsulta> agendar(@RequestBody @Valid DadosAgendamentoConsulta dadosAgendamentoConsulta){
        var dto = _consultaService.agendarConsulta(dadosAgendamentoConsulta);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping
    public ResponseEntity<Void> cancelamento(@RequestBody @Valid DadosCancelamentoConsulta dadosCancelamentoConsulta){
        _consultaService.cancelamentoConsulta(dadosCancelamentoConsulta);

        return ResponseEntity.noContent().build();
    }
}
