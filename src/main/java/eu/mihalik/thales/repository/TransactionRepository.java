package eu.mihalik.thales.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import eu.mihalik.thales.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findAll(Specification<Transaction> specification);
}
