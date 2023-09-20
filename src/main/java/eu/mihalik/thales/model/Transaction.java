package eu.mihalik.thales.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@CreationTimestamp // Automatically set when the entity is first created
	@Column(name = "timestamp")
	private Timestamp timestamp;

	@Column(name = "type")
	private String type;

	@Column(name = "actor")
	private String actor;

	@ElementCollection
	@CollectionTable(name = "transaction_data", joinColumns = @JoinColumn(name = "transaction_id"))
	@MapKeyColumn(name = "data_key")
	@Column(name = "data_value")
	private Map<String, String> transactionData = new HashMap<>();

	public Transaction() {

	}

	public Transaction(String type, String actor, Map<String, String> transactionData) {
		this.type = type;
		this.actor = actor;
		this.transactionData = transactionData;
	}

	public long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public Map<String, String> getTransactionData() {
		return transactionData;
	}

	public void setTransactionData(Map<String, String> transactionData) {
		this.transactionData = transactionData;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", type=" + type + ", actor=" + actor + ", timestamp=" + timestamp + ", transactionData=" + transactionData + "]";
	}
}
