package co.com.pruebTecnicaNexosP1MS.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import co.com.pruebTecnicaNexosP1MS.dto.Card;
import co.com.pruebTecnicaNexosP1MS.dto.Transaction;
import co.com.pruebTecnicaNexosP1MS.exceptions.PruebaTecnicaException;
import co.com.pruebTecnicaNexosP1MS.services.PruebaTecnicaService;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@RestController
@RequestMapping("/pt/p1")
public class PruebaTecnicaP1Controller {

	@Autowired
	private PruebaTecnicaService pruebaTecnicaService;

	private static final Logger LOGGER = LogManager.getLogger(PruebaTecnicaP1Controller.class);

	/**
	 * @param productId
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/{productId}/number", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String genCardNumber(@PathVariable(required = true) String productId) throws ResponseStatusException {

		try {

			if (productId.length() != 6)
				throw new PruebaTecnicaException("Número de producto no válido");

			if (!this.pruebaTecnicaService.validateProductId(productId))
				throw new PruebaTecnicaException("Id de producto no válido");

			String cardNumber = "";
			Boolean isValidCardNumber = false;

			while (!isValidCardNumber) {
				cardNumber = this.pruebaTecnicaService.genCardNumber(productId);
				isValidCardNumber = this.pruebaTecnicaService.validateCardNumber(cardNumber);
			}

			this.pruebaTecnicaService.saveCard(cardNumber);
			return "{\"cardNumber\": \"" + cardNumber + "\"}";

		} catch (PruebaTecnicaException ptex) {
			LOGGER.error("!!! error: ", ptex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ptex.getMessage(), ptex);
		} catch (Exception e) {
			LOGGER.error("!!! error: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * @param bodyParam
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/enroll", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String enrollCard(@RequestBody String bodyParam) throws ResponseStatusException {

		try {

			Card card = (new Gson()).fromJson(bodyParam, Card.class);

			if (card.getCardId().length() != 12)
				throw new PruebaTecnicaException("Número de tarjeta no válido");

			if (this.pruebaTecnicaService.countCards(card.getCardId()) == 0)
				throw new PruebaTecnicaException("Número de tarjeta no existe");

			this.pruebaTecnicaService.enrollCard(card.getCardId());
			return "{\"status\": \"ok\"}";

		} catch (PruebaTecnicaException ptex) {
			LOGGER.error("!!! error: ", ptex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ptex.getMessage(), ptex);
		} catch (Exception e) {
			LOGGER.error("!!! error: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * @param cardId
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/{cardId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String blockCard(@PathVariable(required = true) String cardId) throws ResponseStatusException {

		try {

			if (cardId.length() != 12)
				throw new PruebaTecnicaException("Número de tarjeta no válido");

			if (this.pruebaTecnicaService.countCards(cardId) == 0)
				throw new PruebaTecnicaException("Número de tarjeta no existe");

			this.pruebaTecnicaService.blockCard(cardId);
			return "{\"status\": \"ok\"}";

		} catch (PruebaTecnicaException ptex) {
			LOGGER.error("!!! error: ", ptex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ptex.getMessage(), ptex);
		} catch (Exception e) {
			LOGGER.error("!!! error: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * @param bodyParam
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/balance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String loadBalance(@RequestBody String bodyParam) throws ResponseStatusException {

		try {

			Card card = (new Gson()).fromJson(bodyParam, Card.class);
			Double balance = Double.valueOf(card.getBalance());

			if (balance <= 0)
				throw new PruebaTecnicaException("El valor de la recarga no puede ser un valor igual a cero (0) o negativo");

			if (card.getCardId().length() != 12)
				throw new PruebaTecnicaException("Número de tarjeta no válido");

			if (this.pruebaTecnicaService.countCards(card.getCardId()) == 0)
				throw new PruebaTecnicaException("Número de tarjeta no existe");

			this.pruebaTecnicaService.loadBalance(balance, card.getCardId());
			return "{\"status\": \"ok\"}";

		} catch (PruebaTecnicaException ptex) {
			LOGGER.error("!!! error: ", ptex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ptex.getMessage(), ptex);
		} catch (NumberFormatException nfex) {
			LOGGER.error("!!! error: ", nfex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de numero no valido", nfex);
		} catch (Exception e) {
			LOGGER.error("!!! error: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * @param cardId
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/card/balance/{cardId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCardBalance(@PathVariable(required = true) String cardId) throws ResponseStatusException {

		try {

			if (cardId.length() != 12)
				throw new PruebaTecnicaException("Número de tarjeta no válido");

			if (this.pruebaTecnicaService.countCards(cardId) == 0)
				throw new PruebaTecnicaException("Número de tarjeta no existe");

			String balance = this.pruebaTecnicaService.getBalance(cardId);
			return "{\"balance\": \"" + balance + "\"}";

		} catch (PruebaTecnicaException ptex) {
			LOGGER.error("!!! error: ", ptex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ptex.getMessage(), ptex);
		} catch (Exception e) {
			LOGGER.error("!!! error: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * @param bodyParam
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/transaction/purchase", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String purchase(@RequestBody String bodyParam) throws ResponseStatusException {

		try {

			Transaction transaction = (new Gson()).fromJson(bodyParam, Transaction.class);
			Double price = Double.valueOf(transaction.getPrice());

			if (price <= 0)
				throw new PruebaTecnicaException("El valor de la compra no puede ser un valor igual a cero (0) o negativo");

			if (transaction.getCardId().length() != 12)
				throw new PruebaTecnicaException("Número de tarjeta no válido");

			if (this.pruebaTecnicaService.countCards(transaction.getCardId()) == 0)
				throw new PruebaTecnicaException("Número de tarjeta no existe");

			if (!this.pruebaTecnicaService.isValidCard(transaction.getCardId()))
				throw new PruebaTecnicaException("La tarjeta no se encuentra activa");

			Double balance = this.pruebaTecnicaService.queryBalance(transaction.getCardId());
			if (balance < price)
				throw new PruebaTecnicaException("Cupo insuficiente");

			Integer transactionId = this.pruebaTecnicaService.saveTransaction(price, transaction.getCardId());
			return "{\"transactionId\": " + transactionId + "}";

		} catch (PruebaTecnicaException ptex) {
			LOGGER.error("!!! error: ", ptex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ptex.getMessage(), ptex);
		} catch (Exception e) {
			LOGGER.error("!!! error: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	/**
	 * @param transactionId
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getTransaction(@PathVariable(required = true) Integer transactionId) throws ResponseStatusException {

		try {

			Transaction transaction = this.pruebaTecnicaService.queryTransaction(transactionId);
			return (new Gson()).toJson(transaction);

		} catch (PruebaTecnicaException ptex) {
			LOGGER.error("!!! error: ", ptex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ptex.getMessage(), ptex);
		} catch (Exception e) {
			LOGGER.error("!!! error: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}
}
