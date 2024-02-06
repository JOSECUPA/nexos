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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import co.com.pruebTecnicaNexosP1MS.controller.PruebaTecnicaP1Controller;
import co.com.pruebTecnicaNexosP1MS.dto.Card;
import co.com.pruebTecnicaNexosP1MS.dto.Transaction;
import co.com.pruebTecnicaNexosP1MS.exceptions.PruebaTecnicaException;
import co.com.pruebTecnicaNexosP1MS.services.PruebaTecnicaService;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@ExtendWith(MockitoExtension.class)
public class PruebaTecnicaP1ControllerTest {

	@InjectMocks
	private PruebaTecnicaP1Controller controller;

	@Mock
	private PruebaTecnicaService pruebaTecnicaServiceMock;

	@Test
	public void testGenCardNumberWhenProdIdIsValid() {

		String productId = "881402";
		String cardNumber = "881402000001";

		given(pruebaTecnicaServiceMock.validateProductId(productId)).willReturn(true);
		given(pruebaTecnicaServiceMock.genCardNumber(productId)).willReturn(cardNumber);
		given(pruebaTecnicaServiceMock.validateCardNumber(cardNumber)).willReturn(Boolean.valueOf(true));
		doNothing().when(pruebaTecnicaServiceMock).saveCard(Mockito.any());

		String valueReturn = controller.genCardNumber(productId);
		assertEquals(valueReturn, "{\"cardNumber\": \"" + cardNumber + "\"}");

	}

	@Test
	public void testGenCardNumberWhenProdIdLengthIsNotValid() {

		try {

			String prodId = "88140";
			controller.genCardNumber(prodId);
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de producto no válido"));
		}
	}

	@Test
	public void testGenCardNumberWhenProdIdIsNotValid() {

		try {

			String prodId = "881402";
			given(pruebaTecnicaServiceMock.validateProductId(prodId)).willReturn(Boolean.valueOf(false));
			controller.genCardNumber(prodId);
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Id de producto no válido"));
		}
	}

	@Test
	public void testEnrollCard() {

		String cardNumber = "881402000001";
		Card card = new Card();
		card.setCardId(cardNumber);

		given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(1);
		doNothing().when(pruebaTecnicaServiceMock).enrollCard(cardNumber);
		String response = controller.enrollCard((new Gson()).toJson(card));
		assertEquals("{\"status\": \"ok\"}", response);

	}

	@Test
	public void testEnrollCardWhenCardNumberLengthIsNotValid() {

		try {

			String cardNumber = "8814020000";
			Card card = new Card();
			card.setCardId(cardNumber);

			controller.enrollCard((new Gson()).toJson(card));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no válido"));
		}
	}

	@Test
	public void testEnrollCardWhenCardNumberIsNotValid() {

		try {

			String cardNumber = "881402000001";
			Card card = new Card();
			card.setCardId(cardNumber);

			given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(0);
			controller.enrollCard((new Gson()).toJson(card));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no existe"));
		}
	}

	@Test
	public void testBlockCard() {

		String cardNumber = "881402000001";
		given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(1);
		doNothing().when(pruebaTecnicaServiceMock).blockCard(cardNumber);
		String response = controller.blockCard(cardNumber);
		assertEquals("{\"status\": \"ok\"}", response);

	}

	@Test
	public void testBlockCardWhenCardNumberLengthIsNotValid() {

		try {

			String cardNumber = "8814020000";
			controller.blockCard(cardNumber);
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no válido"));
		}
	}

	@Test
	public void testBlockCardWhenCardNumberIsNotValid() {

		try {

			String cardNumber = "881402000001";
			given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(0);
			controller.blockCard(cardNumber);
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no existe"));
		}
	}

	@Test
	public void testLoadBalance() {

		String cardNumber = "881402000001";
		Card card = new Card();
		card.setCardId(cardNumber);
		card.setBalance("100");

		given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(1);
		doNothing().when(pruebaTecnicaServiceMock).loadBalance(100d, card.getCardId());
		String response = controller.loadBalance((new Gson()).toJson(card));
		assertEquals("{\"status\": \"ok\"}", response);

	}

	@Test
	public void testLoadBalanceWhenBalanceIsZero() {

		try {

			String cardNumber = "881402000001";
			Card card = new Card();
			card.setCardId(cardNumber);
			card.setBalance("0");

			controller.loadBalance((new Gson()).toJson(card));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("El valor de la recarga no puede ser un valor igual a cero (0) o negativo"));
		}
	}

	@Test
	public void testLoadBalanceWhenBalanceIsLiteral() {

		try {

			String cardNumber = "881402000001";
			Card card = new Card();
			card.setCardId(cardNumber);
			card.setBalance("A");

			controller.loadBalance((new Gson()).toJson(card));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Formato de numero no valido"));
		}
	}

	@Test
	public void testLoadBalanceWhenCardNumberLengthIsNotValid() {

		try {

			String cardNumber = "88140200000";
			Card card = new Card();
			card.setCardId(cardNumber);
			card.setBalance("100");

			controller.loadBalance((new Gson()).toJson(card));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no válido"));
		}
	}

	@Test
	public void testLoadBalanceWhenCardNumberIsNotValid() {

		try {

			String cardNumber = "881402000001";
			Card card = new Card();
			card.setCardId(cardNumber);
			card.setBalance("100");

			controller.loadBalance((new Gson()).toJson(card));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no existe"));
		}
	}

	@Test
	public void testGetBalance() {

		String cardNumber = "881402000001";

		given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(1);
		given(pruebaTecnicaServiceMock.getBalance(cardNumber)).willReturn("10");
		String response = controller.getCardBalance(cardNumber);
		assertEquals("{\"balance\": \"10\"}", response);

	}

	@Test
	public void testGetBalanceWhenCardNumberLengthIsNotValid() {

		try {

			String cardNumber = "88140200000";
			controller.getCardBalance(cardNumber);
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no válido"));
		}
	}

	@Test
	public void testGetBalanceWhenCardNumberIsNotValid() {

		try {

			String cardNumber = "881402000001";
			given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(0);
			controller.getCardBalance(cardNumber);
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no existe"));
		}
	}

	@Test
	public void testPurchase() {

		String cardNumber = "881402000001";
		Transaction transaction = new Transaction();
		transaction.setCardId(cardNumber);
		transaction.setAmount("10");
		transaction.setPrice(10F);

		given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(1);
		given(pruebaTecnicaServiceMock.isValidCard(cardNumber)).willReturn(Boolean.valueOf(true));
		given(pruebaTecnicaServiceMock.queryBalance(cardNumber)).willReturn(100d);
		given(pruebaTecnicaServiceMock.saveTransaction(10d, cardNumber)).willReturn(1);
		String response = controller.purchase((new Gson()).toJson(transaction));
		assertEquals("{\"transactionId\": 1}", response);

	}

	@Test
	public void testPurchaseWhenPriceIsNegativeValue() {

		try {

			String cardNumber = "881402000001";
			Transaction transaction = new Transaction();
			transaction.setCardId(cardNumber);
			transaction.setPrice(-10f);

			controller.purchase((new Gson()).toJson(transaction));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("El valor de la compra no puede ser un valor igual a cero (0) o negativo"));
		}
	}

	@Test
	public void testPurchaseWhenCardNumberLengthIsNotValid() {

		try {

			String cardNumber = "88140200000";
			Transaction transaction = new Transaction();
			transaction.setCardId(cardNumber);
			transaction.setPrice(10f);

			controller.purchase((new Gson()).toJson(transaction));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no válido"));
		}
	}

	@Test
	public void testPurchaseWhenCardNumberDoesNotExists() {

		try {

			String cardNumber = "881402000001";
			Transaction transaction = new Transaction();
			transaction.setCardId(cardNumber);
			transaction.setAmount("10");
			transaction.setPrice(10f);

			given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(0);
			controller.purchase((new Gson()).toJson(transaction));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Número de tarjeta no existe"));
		}
	}

	@Test
	public void testPurchaseWhenCardNumberIsNotValid() {

		try {

			String cardNumber = "881402000001";
			Transaction transaction = new Transaction();
			transaction.setCardId(cardNumber);
			transaction.setAmount("10");
			transaction.setPrice(10f);

			given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(1);
			given(pruebaTecnicaServiceMock.isValidCard(cardNumber)).willReturn(Boolean.valueOf(false));
			controller.purchase((new Gson()).toJson(transaction));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("La tarjeta no se encuentra activa"));
		}
	}

	@Test
	public void testPurchaseWhenInsuficientBalance() {

		try {

			String cardNumber = "881402000001";
			Transaction transaction = new Transaction();
			transaction.setCardId(cardNumber);
			transaction.setPrice(10f);

			given(pruebaTecnicaServiceMock.countCards(cardNumber)).willReturn(1);
			given(pruebaTecnicaServiceMock.isValidCard(cardNumber)).willReturn(Boolean.valueOf(true));
			given(pruebaTecnicaServiceMock.queryBalance(cardNumber)).willReturn(5d);
			controller.purchase((new Gson()).toJson(transaction));
			fail("Exception esperada no lanzada");

		} catch (ResponseStatusException e) {
			assertTrue(e.getMessage().contains("Cupo insuficiente"));
		}
	}

	@Test
	public void testQueryTransaction() {

		try {

			String cardNumber = "881402000001";
			Integer transactionId = 1;
			Transaction transaction = new Transaction();
			transaction.setCardId(cardNumber);
			transaction.setAmount("");
			transaction.setState("");
			transaction.setIsApproved(Boolean.valueOf(true));
			transaction.setDate("");

			given(pruebaTecnicaServiceMock.queryTransaction(transactionId)).willReturn(transaction);
			String response = controller.getTransaction(transactionId);
			assertEquals((new Gson()).toJson(transaction), response);

		} catch (ResponseStatusException | PruebaTecnicaException e) {
			e.printStackTrace();
		}
	}
}
