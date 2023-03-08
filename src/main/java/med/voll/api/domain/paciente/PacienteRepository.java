package med.voll.api.domain.paciente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Page<Paciente> findAllByAtivoTrue(Pageable paginacao);
    @Query("SELECT p.ativo FROM Paciente p WHERE p.id = :id")
    boolean findAtivoById(Long id);
    @Query("SELECT COUNT(p) > 0 FROM Paciente p WHERE p.email = :email")
    Boolean existsByEmail(String email);
    @Query("SELECT COUNT(p) > 0 FROM Paciente p WHERE p.cpf = :cpf")
    Boolean existisByCpf(String cpf);
}
