/**
 * 
 */
package co.com.pruebTecnicaNexosP1MS.services;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.pruebTecnicaNexosP1MS.data.PruebaTecnicaDataAccess;
import co.com.pruebTecnicaNexosP1MS.dto.Transaction;
import co.com.pruebTecnicaNexosP1MS.exceptions.PruebaTecnicaException;
import co.com.pruebTecnicaNexosP1MS.model.Tarjeta;
import co.com.pruebTecnicaNexosP1MS.model.Transaccion;
import co.com.pruebTecnicaNexosP1MS.util.IConstantes;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@Service
public class PruebaTecnicaService {

	@Autowired
	private PruebaTecnicaDataAccess pruebaTecnicaDataAccess;

	/**
	 * @param productId
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean validateProductId(String productId) {
		if (this.pruebaTecnicaDataAccess.countProductId(productId) == 1)
			return Boolean.valueOf(true);
		else
			return Boolean.valueOf(false);
	}

	/**
	 * @param productId
	 * @return
	 */
	public String genCardNumber(String productId) {
		Double rand = Math.random();
		rand = rand * 1000000;
		Integer cardNum = Integer.valueOf(rand.intValue());
		String fullCardNumber = String.format("%s%s", productId, String.format("%06d", cardNum));
		return fullCardNumber;
	}

	/**
	 * @param cardNumber
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean validateCardNumber(String cardNumber) {
		if (this.pruebaTecnicaDataAccess.countCardNumber(cardNumber) == 0) 
			return Boolean.valueOf(true);
		else
			return Boolean.valueOf(false);
	}

	/**
	 * @param cardNumber
	 * @return Integer
	 * @throws Exception
	 */
	public Integer countCards(String cardNumber) {
		return this.pruebaTecnicaDataAccess.countCardNumber(cardNumber);
	}

	/**
	 * @param cardNumber
	 */
	public void saveCard(String cardNumber) {
		Tarjeta card = new Tarjeta();
		card.setTipo(IConstantes.ID_DOMINIO_TIPO_TARJETA_CREDITO);
		card.setNumero(cardNumber);

		LocalDateTime now = LocalDateTime.now();
		card.setVencimiento(now.plusYears(3));
		card.setCupo(0D);
		card.setEsActivo(Boolean.valueOf(false));
		card.setEsBloqueado(Boolean.valueOf(false));
		card.setNombreCliente("");
		card.setApellidoCliente("");
		this.pruebaTecnicaDataAccess.saveCard(card);
	}

	/**
	 * @param cardNumber
	 * @return Tarjeta
	 */
	public Tarjeta getCard(String cardNumber) {
		return this.pruebaTecnicaDataAccess.queryCard(cardNumber);
	}

	/**
	 * @param cardNumber
	 */
	public void enrollCard(String cardNumber) {
		this.pruebaTecnicaDataAccess.activateCard(Boolean.valueOf(true), cardNumber);
	}

	/**
	 * @param cardNumber
	 */
	public void blockCard(String cardNumber) {
		this.pruebaTecnicaDataAccess.blockCard(Boolean.valueOf(true), cardNumber);
	}

	/**
	 * @param value
	 * @param cardNumber
	 */
	public void loadBalance(Double value, String cardNumber) {
		this.pruebaTecnicaDataAccess.loadBalance(value, cardNumber);
	}

	/**
	 * @param cardNumber
	 * @return String
	 */
	public String getBalance(String cardNumber) {
		Double balance = this.queryBalance(cardNumber);
		String balanceValue = this.amountFormatter(balance);
		return balanceValue;
	}

	/**
	 * @param cardNumber
	 * @return Double
	 */
	public Double queryBalance(String cardNumber) {
		Double balance = this.pruebaTecnicaDataAccess.getBalance(cardNumber);
		return balance;
	}

	/**
	 * @param value
	 * @param cardNumber
	 */
	public Integer saveTransaction(Double value, String cardNumber) {

		Transaccion transaction = new Transaccion();
		transaction.setEstado(IConstantes.ID_DOMINIO_ESTADO_TRANSACCION_APROBADA);

		LocalDateTime now = LocalDateTime.now();
		transaction.setFecha(now);
		transaction.setMonto(value);
		transaction.setEsAprobada(Boolean.valueOf(true));
		Integer transactionId = this.pruebaTecnicaDataAccess.saveTransaction(transaction, cardNumber);

		this.loadBalance(value * (-1), cardNumber);
		return transactionId;

	}

	/**
	 * @param cardNumber
	 * @return Boolean
	 */
	public Boolean isValidCard(String cardNumber) {

		Tarjeta card = this.getCard(cardNumber);
		Boolean isVigente = card.getVencimiento().isAfter(LocalDateTime.now());

		if (isVigente && card.getEsActivo() && !card.getEsBloqueado())
			return Boolean.valueOf(true);

		return Boolean.valueOf(false);

	}

	/**
	 * @param transactionId
	 * @return Transaction
	 */
	public Transaction queryTransaction(Integer transactionId) throws PruebaTecnicaException {
		
		try {
			
			Object[] tupla = this.pruebaTecnicaDataAccess.queryTransaction(transactionId);
			String numero = (String)tupla[0];
			Double monto = (Double)tupla[1];
			String estado = (String)tupla[2];
			Boolean esaprobada = (Boolean)tupla[3];
			LocalDateTime fecha = (LocalDateTime)tupla[4];

			Transaction transaction = new Transaction();
			transaction.setCardId(numero);
			transaction.setAmount(this.amountFormatter(monto));
			transaction.setState(estado);
			transaction.setIsApproved(esaprobada);
			transaction.setDate(this.dateFormatter(fecha));
			return transaction;
			
		} catch (Exception e) {
			throw new PruebaTecnicaException("No existe la transacción consultada");
		}
	}

	/**
	 * @param transactionId
	 * @param cardNumber
	 * @return Double
	 * @throws PruebaTecnicaException
	 */
	public Double validateCardTransaction(Integer transactionId, String cardNumber) throws PruebaTecnicaException {

		try {

			LocalDateTime end = LocalDateTime.now();
			LocalDateTime begin = LocalDateTime.now().minusDays(1);

			Object[] tupla = this.pruebaTecnicaDataAccess.queryDateTransaction(transactionId, cardNumber);
			LocalDateTime trxDate = (LocalDateTime)tupla[0];
			Double monto = (Double)tupla[1];

			if (trxDate.isAfter(begin) && trxDate.isBefore(end))
				return monto;

			return 0d;

		}catch (Exception e) {
			throw new PruebaTecnicaException("No existe la transacción consultada");
		}
	}

	/**
	 * @param transactionId
	 * @param cardNumber
	 * @param amount
	 */
	public void anulateTransaction(Integer transactionId, String cardNumber, Double amount) {
		this.pruebaTecnicaDataAccess.modifyTransactionStatus(IConstantes.ID_DOMINIO_ESTADO_TRANSACCION_ANULADA, transactionId);
		this.loadBalance(amount, cardNumber);
	}

	/**
	 * @param value
	 * @return String
	 */
	private String amountFormatter(Double value) {
		Locale locale = new Locale("en", "US");
		Currency currency = Currency.getInstance(locale);
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
		String stringValue = String.format("%s: %s", currency.getDisplayName(), numberFormat.format(value));
		return stringValue;
	}

	/**
	 * @param date
	 * @return String
	 */
	private String dateFormatter(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String localDateTimeString = date.format(formatter);
		return localDateTimeString;
	}
}
