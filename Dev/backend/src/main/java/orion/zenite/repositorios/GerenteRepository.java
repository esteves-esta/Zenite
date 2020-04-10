package orion.zenite.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import orion.zenite.entidades.Conta;
import orion.zenite.entidades.Gerente;

public interface GerenteRepository extends JpaRepository<Gerente, Integer> {
    @Query(value = "select max(g.id) from Gerente g")
    int lastId();

    Gerente findByConta(Conta conta);

    Gerente findByConta(String cpf);
}