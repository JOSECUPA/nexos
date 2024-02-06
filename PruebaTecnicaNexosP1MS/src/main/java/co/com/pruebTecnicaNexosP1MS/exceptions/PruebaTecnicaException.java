package co.com.pruebTecnicaNexosP1MS.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PruebaTecnicaException extends Exception {

	/** */
	private static final long serialVersionUID = -1380873293516985459L;

	public PruebaTecnicaException(String msj) {
		super(msj);
	}
}
