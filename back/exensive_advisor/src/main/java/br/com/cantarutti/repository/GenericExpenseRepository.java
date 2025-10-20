package br.com.cantarutti.repository;
import org.springframework.stereotype.Repository;

// @Repository
public interface ExpensesDataRepository {
    void save(List<Map<String, Object>> data);
}
