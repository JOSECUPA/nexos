/**
 * 
 */
package co.com.pruebTecnicaNexosP1MS.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import co.com.pruebTecnicaNexosP1MS.dto.Anulation;
import co.com.pruebTecnicaNexosP1MS.exceptions.PruebaTecnicaException;
import co.com.pruebTecnicaNexosP1MS.services.PruebaTecnicaService;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@RestController
@RequestMapping("/pt/p2")
public class PruebaTecnicaP2Controller {

	@Autowired
	private PruebaTecnicaService pruebaTecnicaService;

	private static final Logger LOGGER = LogManager.getLogger(PruebaTecnicaP2Controller.class);

	/**
	 * @param bodyParam
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/transaction/anulation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String anulateTransaction(@RequestBody String bodyParam) throws PruebaTecnicaException {

		try {

			Anulation anulation = (new Gson()).fromJson(bodyParam, Anulation.class);
			Integer trxId = Integer.valueOf(anulation.getTransactionId());
			Double monto = this.pruebaTecnicaService.validateCardTransaction(trxId, anulation.getCardId());

			if (monto == 0)
				throw new PruebaTecnicaException("Transaccón fuera del periodo de tiempo permitido para realizar la anulación");

			this.pruebaTecnicaService.anulateTransaction(trxId, anulation.getCardId(), monto);
			return "{\"status\": \"ok\"}";

		} catch (PruebaTecnicaException ptex) {
			LOGGER.error("!!! error: ", ptex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ptex.getMessage(), ptex);
		} catch (Exception e) {
			LOGGER.error("!!! error: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

}
