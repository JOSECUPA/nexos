/**
 * 
 */
package co.com.pruebaTecnicaNexosP1MS.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import co.com.pruebTecnicaNexosP1MS.data.PruebaTecnicaDataAccess;
import co.com.pruebTecnicaNexosP1MS.model.Tarjeta;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@ExtendWith(MockitoExtension.class)
public class PruebaTecnicaDataAccessTest {

	@InjectMocks
	private PruebaTecnicaDataAccess dataAccess;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testCountProductId() {
		String prodId = "881402";
		String query = "select count(*) from productos p where p.identificador = ?";
		Object[] params = { prodId };
		given(jdbcTemplate.queryForObject(query, Integer.class, params)).willReturn(1);
		Integer numProds = dataAccess.countProductId(prodId);
		assertEquals(numProds, 1);
	}

	@Test
	public void testCountCardNumber() {
		String cardNumber = "881402000001";
		String query = "select count(*) from tarjetas t where t.numero = ?";
		Object[] params = { cardNumber };
		given(jdbcTemplate.queryForObject(query, Integer.class, params)).willReturn(1);
		Integer numProds = dataAccess.countCardNumber(cardNumber);
		assertEquals(numProds, 1);
	}

	@Test
	public void testQueryCard() {

		String cardNumber = "881402000001";
		String query = "select id, tipo, numero, vencimiento, cupo, esactivo, "
				+ "esbloqueado, nombre_cliente, apellido_cliente from tarjetas t " + "where numero = ?";
		Object[] params = { cardNumber };
		Tarjeta tarjeta = new Tarjeta(1, 1, "881402000001", LocalDateTime.now(), 0d, Boolean.valueOf(true),
				Boolean.valueOf(false), "", "");

		RowMapper<Tarjeta> mapper = new RowMapper<Tarjeta>() {
			public Tarjeta mapRow(ResultSet rs, int rowNum) throws SQLException {
				Tarjeta tarjeta = new Tarjeta(1, 1, "881402000001", LocalDateTime.now(), 0d, Boolean.valueOf(true),
						Boolean.valueOf(false), "", "");
				return tarjeta;
			}
		};

		given(jdbcTemplate.queryForObject(query, mapper, params)).willReturn(tarjeta);
		Tarjeta card = dataAccess.queryCard(cardNumber);
		assertEquals(tarjeta.getNumero(), card.getNumero());

	}

	@Test
	public void testActivateCard() {

		String cardNumber = "881402000001";
		Boolean activar = Boolean.valueOf(true);
		String query = "update tarjetas set esactivo = ? where numero = ?";
		Object[] parameters = { activar, cardNumber };
		given(jdbcTemplate.update(query, parameters)).willReturn(1);
		dataAccess.activateCard(activar, cardNumber);

	}

	@Test
	public void testBlockCard() {

		String cardNumber = "881402000001";
		Boolean bloquear = Boolean.valueOf(true);
		String query = "update tarjetas set esbloqueado = ? where numero = ?";
		Object[] parameters = { bloquear, cardNumber };
		given(jdbcTemplate.update(query, parameters)).willReturn(1);
		dataAccess.blockCard(bloquear, cardNumber);

	}

	@Test
	public void testLoadBalance() {

		String cardNumber = "881402000001";
		Double value = 0d;
		String query = "update tarjetas set cupo = cupo + ? where numero = ?";
		Object[] parameters = { value, cardNumber };
		given(jdbcTemplate.update(query, parameters)).willReturn(1);
		dataAccess.loadBalance(value, cardNumber);

	}

	@Test
	public void testGetBalance() {

		String cardNumber = "881402000001";
		String query = "select cupo from tarjetas t where t.numero = ?";
		Object[] parameters = { cardNumber };
		given(jdbcTemplate.queryForObject(query, Double.class, parameters)).willReturn(10d);
		Double value = dataAccess.getBalance(cardNumber);
		assertEquals(value, 10d);

	}

	@Test
	public void testQueryTransaction() {

		Integer idTransaction = 1;
		String query = "select tj.numero, t.monto, d.valor as estado, t.esaprobada, t.fecha " + "from transacciones t  "
				+ "join dominios d on d.id = t.estado  " + "join tarjetas tj on tj.id = t.id_tarjeta  "
				+ "where t.id = ?";
		Object[] params = { idTransaction };
		Object[] tupla = { "", 0d, "", Boolean.valueOf(true), LocalDateTime.now() };

		RowMapper<Object[]> mapper = new RowMapper<Object[]>() {
			public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				Object[] tupla = { "", 0d, "", Boolean.valueOf(true), LocalDateTime.now() };
				return tupla;
			}
		};

		given(jdbcTemplate.queryForObject(query, mapper, params)).willReturn(tupla);
		Object[] returnValue = dataAccess.queryTransaction(idTransaction);
		assertEquals(tupla[0], returnValue[0]);

	}

	@Test
	public void testDateQueryTransaction() {

		Integer idTransaction = 1;
		String cardNumber = "881402000001";
		String query = "select t.fecha, t.monto from transacciones t  " + "join tarjetas tj on tj.id = t.id_tarjeta  "
				+ "where t.id = ? and tj.numero = ?";
		Object[] params = { idTransaction, cardNumber };
		Object[] tupla = { LocalDateTime.now(), 0d };

		RowMapper<Object[]> mapper = new RowMapper<Object[]>() {
			public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				Object[] tupla = { LocalDateTime.now(), 0d };
				return tupla;
			}
		};

		given(jdbcTemplate.queryForObject(query, mapper, params)).willReturn(tupla);
		Object[] returnValue = dataAccess.queryDateTransaction(idTransaction, cardNumber);
		assertEquals(tupla[1], returnValue[1]);

	}

	@Test
	public void testModifyTransactionStatus() {

		Integer transactionId = 1;
		Integer estado = 3;
		String query = "update transacciones set estado = ? where id = ?";
		Object[] parameters = { estado, transactionId };
		given(jdbcTemplate.update(query, parameters)).willReturn(1);
		dataAccess.modifyTransactionStatus(transactionId, transactionId);

	}
}
