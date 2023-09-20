package eu.mihalik.thales.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.mihalik.thales.model.Transaction;
import eu.mihalik.thales.repository.TransactionRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TransactionController {

	@Autowired
	TransactionRepository transactionRepository;

	@GetMapping("/transactions")
	public List<Transaction> searchTransactions(String type, String actor, String createdAfter, String createdBefore) {
		Date createdAfterDate;
		Date createdBeforeDate;

		// Convert the string representations of timestamps to Timestamp objects if provided
		if (createdAfter != null) {
			createdAfterDate = Date.valueOf(createdAfter);
		} else {
			createdAfterDate = null;
		}
		if (createdBefore != null) {
			createdBeforeDate = Date.valueOf(createdBefore);
		} else {
			createdBeforeDate = null;
		}

		Specification<Transaction> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Add conditions based on the presence of search parameters
			if (type != null) {
				predicates.add(criteriaBuilder.like(root.get("type"), "%" + type + "%"));
			}
			if (actor != null) {
				predicates.add(criteriaBuilder.like(root.get("actor"), "%" + actor + "%"));
			}
			if (createdAfter != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), createdAfterDate));
			}
			if (createdBefore != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), createdBeforeDate));
			}

			// Combine predicates using AND
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};

		return transactionRepository.findAll(specification);
	}

	@GetMapping("/transactions/{id}")
	public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") long id) {
		Optional<Transaction> transactionOptional = transactionRepository.findById(id);

		if (transactionOptional.isPresent()) {
			return new ResponseEntity<>(transactionOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/transactions")
	public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
		try {
			Transaction _transaction = transactionRepository
					.save(new Transaction(transaction.getType(), transaction.getActor(), transaction.getTransactionData()));
			return new ResponseEntity<>(_transaction, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/transactions/{id}")
	public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") long id, @RequestBody Transaction transaction) {
		Optional<Transaction> transactionOptional = transactionRepository.findById(id);

		if (transactionOptional.isPresent()) {
			Transaction _transaction = transactionOptional.get();
			if (transaction.getType() != null) {
				_transaction.setType(transaction.getType());
			}
			if (transaction.getActor() != null) {
				_transaction.setActor(transaction.getActor());
			}
			if (transaction.getTransactionData() != null) {
				_transaction.setTransactionData(transaction.getTransactionData());
			}
			return new ResponseEntity<>(transactionRepository.save(_transaction), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/transactions/{id}")
	public ResponseEntity<HttpStatus> deleteTransaction(@PathVariable("id") long id) {
		try {
			transactionRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/transactions")
	public ResponseEntity<HttpStatus> deleteAllTransactions() {
		try {
			transactionRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
