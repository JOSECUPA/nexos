/**
 * 
 */
package co.com.pruebaTecnicaNexosP1MS.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.pruebTecnicaNexosP1MS.data.PruebaTecnicaDataAccess;
import co.com.pruebTecnicaNexosP1MS.dto.Transaction;
import co.com.pruebTecnicaNexosP1MS.exceptions.PruebaTecnicaException;
import co.com.pruebTecnicaNexosP1MS.model.Tarjeta;
import co.com.pruebTecnicaNexosP1MS.services.PruebaTecnicaService;
import co.com.pruebTecnicaNexosP1MS.util.IConstantes;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@ExtendWith(MockitoExtension.class)
public class PruebaTecnicaServiceTest {

	@InjectMocks
	private PruebaTecnicaService service;

	@Mock
	private PruebaTecnicaDataAccess pruebaTecnicaDataAccessMock;

	@Test
	public void validateProductIdWhenProdIdIsCorrect() {

		try {

			String productId = "881402";
			given(pruebaTecnicaDataAccessMock.countProductId(productId)).willReturn(1);
			Boolean esValido = service.validateProductId(productId);
			assertTrue(esValido);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void validateProductIdWhenProdIdIsInCorrect() {

		try {

			String productId = "881402";
			given(pruebaTecnicaDataAccessMock.countProductId(productId)).willReturn(0);
			Boolean esValido = service.validateProductId(productId);
			assertTrue(!esValido);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void validateGenCardNumber() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			assertTrue(cardNumber.length() == 12);
			assertTrue(cardNumber.substring(0, 6).equals(productId));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testValidateCardNumberWhenValidCardNumber() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			given(pruebaTecnicaDataAccessMock.countCardNumber(cardNumber)).willReturn(0);
			Boolean esValido = service.validateCardNumber(cardNumber);
			assertTrue(esValido);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testValidateCardNumberWhenInvalidCardNumber() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			given(pruebaTecnicaDataAccessMock.countCardNumber(cardNumber)).willReturn(1);
			Boolean esValido = service.validateCardNumber(cardNumber);
			assertTrue(!esValido);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCountCardNumbers() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			given(pruebaTecnicaDataAccessMock.countCardNumber(cardNumber)).willReturn(1);
			Integer numCards = service.countCards(cardNumber);
			assertEquals(numCards, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSaveCard() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			doNothing().when(pruebaTecnicaDataAccessMock).saveCard(Mockito.any());
			service.saveCard(cardNumber);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetCard() {

		String productId = "881402";
		String cardNumber = service.genCardNumber(productId);

		Tarjeta cardMock = new Tarjeta();
		cardMock.setTipo(IConstantes.ID_DOMINIO_TIPO_TARJETA_CREDITO);
		cardMock.setNumero(cardNumber);

		LocalDateTime now = LocalDateTime.now();
		cardMock.setVencimiento(now.plusYears(3));
		cardMock.setCupo(0D);
		cardMock.setEsActivo(Boolean.valueOf(false));
		cardMock.setEsBloqueado(Boolean.valueOf(false));
		cardMock.setNombreCliente("");
		cardMock.setApellidoCliente("");

		given(pruebaTecnicaDataAccessMock.queryCard(cardNumber)).willReturn(cardMock);
		Tarjeta card = service.getCard(cardNumber);
		assertEquals(cardMock.getVencimiento().hashCode(), card.getVencimiento().hashCode());

	}

	@Test
	public void testEnrollCard() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			doNothing().when(pruebaTecnicaDataAccessMock).activateCard(Mockito.any(), Mockito.any());
			service.enrollCard(cardNumber);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBlockCard() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			doNothing().when(pruebaTecnicaDataAccessMock).blockCard(Mockito.any(), Mockito.any());
			service.blockCard(cardNumber);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLoadBalance() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			doNothing().when(pruebaTecnicaDataAccessMock).loadBalance(Mockito.any(), Mockito.any());
			service.loadBalance(0d, cardNumber);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testQueryBalance() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			given(pruebaTecnicaDataAccessMock.getBalance(cardNumber)).willReturn(0D);
			Double balance = service.queryBalance(cardNumber);
			assertEquals(balance, 0d);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetBalance() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			given(pruebaTecnicaDataAccessMock.getBalance(cardNumber)).willReturn(0D);
			String balance = service.getBalance(cardNumber);
			assertTrue(!balance.isBlank());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSaveTransaction() {

		String productId = "881402";
		String cardNumber = service.genCardNumber(productId);
		Double value = 10d;

		given(pruebaTecnicaDataAccessMock.saveTransaction(Mockito.any(), Mockito.any())).willReturn(1);
		doNothing().when(pruebaTecnicaDataAccessMock).loadBalance(Mockito.any(), Mockito.any());
		Integer trxId = service.saveTransaction(value, cardNumber);
		assertEquals(trxId, 1);

	}

	@Test
	public void testIsValidCardWhenCardIsValid() {

		String productId = "881402";
		String cardNumber = service.genCardNumber(productId);

		Tarjeta cardMock = new Tarjeta();
		cardMock.setTipo(IConstantes.ID_DOMINIO_TIPO_TARJETA_CREDITO);
		cardMock.setNumero(cardNumber);

		LocalDateTime now = LocalDateTime.now();
		cardMock.setVencimiento(now.plusYears(3));
		cardMock.setCupo(0D);
		cardMock.setEsActivo(Boolean.valueOf(true));
		cardMock.setEsBloqueado(Boolean.valueOf(false));
		cardMock.setNombreCliente("");
		cardMock.setApellidoCliente("");

		given(pruebaTecnicaDataAccessMock.queryCard(cardNumber)).willReturn(cardMock);
		Boolean isValid = service.isValidCard(cardNumber);
		assertTrue(isValid);

	}

	@Test
	public void testIsValidCardWhenCardIsInvalidByVencimiento() {

		String productId = "881402";
		String cardNumber = service.genCardNumber(productId);

		Tarjeta cardMock = new Tarjeta();
		cardMock.setTipo(IConstantes.ID_DOMINIO_TIPO_TARJETA_CREDITO);
		cardMock.setNumero(cardNumber);

		LocalDateTime now = LocalDateTime.now();
		cardMock.setVencimiento(now.plusYears(-1));
		cardMock.setCupo(0D);
		cardMock.setEsActivo(Boolean.valueOf(true));
		cardMock.setEsBloqueado(Boolean.valueOf(false));
		cardMock.setNombreCliente("");
		cardMock.setApellidoCliente("");

		given(pruebaTecnicaDataAccessMock.queryCard(cardNumber)).willReturn(cardMock);
		Boolean isValid = service.isValidCard(cardNumber);
		assertTrue(!isValid);

	}

	@Test
	public void testIsValidCardWhenCardIsInvalidByActivoIsFalse() {

		String productId = "881402";
		String cardNumber = service.genCardNumber(productId);

		Tarjeta cardMock = new Tarjeta();
		cardMock.setTipo(IConstantes.ID_DOMINIO_TIPO_TARJETA_CREDITO);
		cardMock.setNumero(cardNumber);

		LocalDateTime now = LocalDateTime.now();
		cardMock.setVencimiento(now.plusYears(3));
		cardMock.setCupo(0D);
		cardMock.setEsActivo(Boolean.valueOf(false));
		cardMock.setEsBloqueado(Boolean.valueOf(false));
		cardMock.setNombreCliente("");
		cardMock.setApellidoCliente("");

		given(pruebaTecnicaDataAccessMock.queryCard(cardNumber)).willReturn(cardMock);
		Boolean isValid = service.isValidCard(cardNumber);
		assertTrue(!isValid);

	}

	@Test
	public void testIsValidCardWhenCardIsInvalidByBloqueadoIsTrue() {

		String productId = "881402";
		String cardNumber = service.genCardNumber(productId);

		Tarjeta cardMock = new Tarjeta();
		cardMock.setTipo(IConstantes.ID_DOMINIO_TIPO_TARJETA_CREDITO);
		cardMock.setNumero(cardNumber);

		LocalDateTime now = LocalDateTime.now();
		cardMock.setVencimiento(now.plusYears(3));
		cardMock.setCupo(0D);
		cardMock.setEsActivo(Boolean.valueOf(false));
		cardMock.setEsBloqueado(Boolean.valueOf(true));
		cardMock.setNombreCliente("");
		cardMock.setApellidoCliente("");

		given(pruebaTecnicaDataAccessMock.queryCard(cardNumber)).willReturn(cardMock);
		Boolean isValid = service.isValidCard(cardNumber);
		assertTrue(!isValid);

	}

	@Test
	public void testQueryTransaction() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);

			Object[] tupla = {cardNumber, 10d, "APROBADA", Boolean.valueOf(true), LocalDateTime.now()};
			given(pruebaTecnicaDataAccessMock.queryTransaction(1)).willReturn(tupla);

			Transaction transaction = service.queryTransaction(1);
			assertTrue(transaction.getIsApproved());
			assertEquals(transaction.getCardId(), cardNumber);

		} catch (PruebaTecnicaException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testValidateCardTransactionWhenValidTransaction() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			LocalDateTime fecha = LocalDateTime.now().minusHours(10);

			Object[] tupla = {fecha, 10d};
			given(pruebaTecnicaDataAccessMock.queryDateTransaction(1, cardNumber)).willReturn(tupla);
			Double monto = service.validateCardTransaction(1, cardNumber);
			assertEquals(monto, 10d);

		} catch (PruebaTecnicaException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testValidateCardTransactionWhenTransactionDateIsInvalid() {

		try {

			String productId = "881402";
			String cardNumber = service.genCardNumber(productId);
			LocalDateTime fecha = LocalDateTime.now().minusHours(30);

			Object[] tupla = {fecha, 10d};
			given(pruebaTecnicaDataAccessMock.queryDateTransaction(1, cardNumber)).willReturn(tupla);
			Double monto = service.validateCardTransaction(1, cardNumber);
			assertEquals(monto, 0d);

		} catch (PruebaTecnicaException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAulateTransaction() {

		String productId = "881402";
		String cardNumber = service.genCardNumber(productId);

		doNothing().when(pruebaTecnicaDataAccessMock).modifyTransactionStatus(Mockito.anyInt(), Mockito.any());
		doNothing().when(pruebaTecnicaDataAccessMock).loadBalance(Mockito.any(), Mockito.any());
		service.anulateTransaction(Mockito.anyInt(), cardNumber, Mockito.any());

	}
}
