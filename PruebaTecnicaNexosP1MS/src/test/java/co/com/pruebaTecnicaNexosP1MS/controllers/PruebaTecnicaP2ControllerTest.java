/**
 * 
 */
package co.com.pruebaTecnicaNexosP1MS.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import co.com.pruebTecnicaNexosP1MS.controller.PruebaTecnicaP2Controller;
import co.com.pruebTecnicaNexosP1MS.dto.Anulation;
import co.com.pruebTecnicaNexosP1MS.exceptions.PruebaTecnicaException;
import co.com.pruebTecnicaNexosP1MS.services.PruebaTecnicaService;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@ExtendWith(MockitoExtension.class)
public class PruebaTecnicaP2ControllerTest {

	@InjectMocks
	private PruebaTecnicaP2Controller controller;

	@Mock
	private PruebaTecnicaService pruebaTecnicaServiceMock;

	@Test
	public void testAnulateTransaction() {

		try {

			Anulation anulation = new Anulation();
			anulation.setCardId("881402000001");
			anulation.setTransactionId("1");

			given(pruebaTecnicaServiceMock.validateCardTransaction(1, "881402000001")).willReturn(10d);
			doNothing().when(pruebaTecnicaServiceMock).anulateTransaction(1, "881402000001", 10d);
			String response = controller.anulateTransaction((new Gson()).toJson(anulation));
			assertEquals("{\"status\": \"ok\"}", response);

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAnulateTransactionWhenTransactionIsOutOfDate() throws PruebaTecnicaException {

		try {

			Anulation anulation = new Anulation();
			anulation.setCardId("881402000001");
			anulation.setTransactionId("1");

			given(pruebaTecnicaServiceMock.validateCardTransaction(1, "881402000001")).willReturn(0d);
			controller.anulateTransaction((new Gson()).toJson(anulation));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Transaccón fuera del periodo de tiempo permitido para realizar la anulación"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
