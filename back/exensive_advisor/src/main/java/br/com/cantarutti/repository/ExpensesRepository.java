package br.com.cantarutti.repository;

import br.com.cantarutti.model.ExpensesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends JpaRepository<ExpensesModel, Long> {
    // access to Expenses Table
}
