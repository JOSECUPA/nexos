package co.com.pruebTecnicaNexosP1MS.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import co.com.pruebTecnicaNexosP1MS.model.Tarjeta;
import co.com.pruebTecnicaNexosP1MS.model.Transaccion;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@Configuration
public class PruebaTecnicaDataAccess {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * @param productId
	 * @return Integer
	 */
	public Integer countProductId(String productId) {
		String query = "select count(*) from productos p where p.identificador = ?";
		Object[] params = { productId };
		return this.jdbcTemplate.queryForObject(query, Integer.class, params);
	}

	/**
	 * @param cardNumber
	 * @return Integer
	 */
	public Integer countCardNumber(String cardNumber) {
		String query = "select count(*) from tarjetas t where t.numero = ?";
		Object[] params = { cardNumber };
		return this.jdbcTemplate.queryForObject(query, Integer.class, params);
	}

	/**
	 * @param card
	 */
	public void saveCard(Tarjeta card) {

		String query = "insert into tarjetas (" + "tipo, numero, vencimiento, cupo, " + "esactivo, esbloqueado, "
				+ "nombre_cliente, apellido_cliente) " + "values (?, ?, ?, ?, ?, ?, ?, ?)";

		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query);

				ps.setInt(1, card.getTipo());
				ps.setString(2, card.getNumero());
				ps.setObject(3, card.getVencimiento());
				ps.setDouble(4, card.getCupo());
				ps.setBoolean(5, card.getEsActivo());
				ps.setBoolean(6, card.getEsBloqueado());
				ps.setString(7, card.getNombreCliente());
				ps.setString(8, card.getApellidoCliente());

				return ps;
			}
		});
	}

	/**
	 * @param cardNumber
	 * @return Tarjeta
	 */
	public Tarjeta queryCard(String cardNumber) {

		String query = "select id, tipo, numero, vencimiento, cupo, esactivo, "
				+ "esbloqueado, nombre_cliente, apellido_cliente from tarjetas t " + "where numero = ?";

		RowMapper<Tarjeta> mapper = new RowMapper<Tarjeta>() {
			public Tarjeta mapRow(ResultSet rs, int rowNum) throws SQLException {
				Tarjeta tarjeta = new Tarjeta(rs.getInt("id"), rs.getInt("tipo"), rs.getString("numero"),
						rs.getTimestamp("vencimiento").toLocalDateTime(), rs.getDouble("cupo"),
						rs.getBoolean("esactivo"), rs.getBoolean("esbloqueado"), rs.getString("nombre_cliente"),
						rs.getString("apellido_cliente"));
				return tarjeta;
			}
		};

		Object[] parameters = { cardNumber };
		return this.jdbcTemplate.queryForObject(query, mapper, parameters);

	}

	/**
	 * @param esActivo
	 * @param cardNumber
	 */
	public void activateCard(Boolean esActivo, String cardNumber) {
		String query = "update tarjetas set esactivo = ? where numero = ?";
		Object[] parameters = { esActivo, cardNumber };
		this.jdbcTemplate.update(query, parameters);
	}

	/**
	 * @param esActivo
	 * @param cardNumber
	 */
	public void blockCard(Boolean esBloqueado, String cardNumber) {
		String query = "update tarjetas set esbloqueado = ? where numero = ?";
		Object[] parameters = { esBloqueado, cardNumber };
		this.jdbcTemplate.update(query, parameters);
	}

	/**
	 * @param value
	 * @param cardNumber
	 */
	public void loadBalance(Double value, String cardNumber) {
		String query = "update tarjetas set cupo = cupo + ? where numero = ?";
		Object[] parameters = { value, cardNumber };
		this.jdbcTemplate.update(query, parameters);
	}

	/**
	 * @param cardId
	 * @return Double
	 */
	public Double getBalance(String cardNumber) {
		String query = "select cupo from tarjetas t where t.numero = ?";
		Object[] params = { cardNumber };
		return this.jdbcTemplate.queryForObject(query, Double.class, params);
	}

	/**
	 * @param transaction
	 * @param cardNumber
	 * @return @Integer transactionId
	 */
	public Integer saveTransaction(Transaccion transaction, String cardNumber) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = "insert into transacciones (" + "id_tarjeta, monto, estado, esaprobada, "
				+ "fecha) values ((select id from tarjetas where numero = ?), ?, ?, ?, ?)";

		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query, new String[] { "id" });

				ps.setString(1, cardNumber);
				ps.setDouble(2, transaction.getMonto());
				ps.setObject(3, transaction.getEstado());
				ps.setBoolean(4, transaction.getEsAprobada());
				ps.setObject(5, transaction.getFecha());

				return ps;
			}
		}, keyHolder);

		return (Integer) keyHolder.getKey();
	}

	/**
	 * @param idTransaction
	 * @return Object[]
	 */
	public Object[] queryTransaction(Integer idTransaction) {

		String query = "select tj.numero, t.monto, d.valor as estado, t.esaprobada, t.fecha "
				+ "from transacciones t  " + "join dominios d on d.id = t.estado  "
				+ "join tarjetas tj on tj.id = t.id_tarjeta  " + "where t.id = ?";

		RowMapper<Object[]> mapper = new RowMapper<Object[]>() {
			public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				Object[] tupla = { rs.getString("numero"), rs.getDouble("monto"), rs.getString("estado"),
						rs.getBoolean("esaprobada"), rs.getTimestamp("fecha").toLocalDateTime() };
				return tupla;
			}
		};

		Object[] parameters = { idTransaction };
		return this.jdbcTemplate.queryForObject(query, mapper, parameters);

	}

	/**
	 * @param tranasctionId
	 * @param cardNumber
	 * @return Object[]
	 */
	public Object[] queryDateTransaction(Integer tranasctionId, String cardNumber) {

		String query = "select t.fecha, t.monto from transacciones t  "
				+ "join tarjetas tj on tj.id = t.id_tarjeta  " 
				+ "where t.id = ? and tj.numero = ?";

		RowMapper<Object[]> mapper = new RowMapper<Object[]>() {
			public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				LocalDateTime fechaTrx = rs.getTimestamp("fecha").toLocalDateTime();
				Double monto = rs.getDouble("monto");
				Object[] tupla = { fechaTrx, monto };
				return tupla;
			}
		};

		Object[] params = { tranasctionId, cardNumber };
		return this.jdbcTemplate.queryForObject(query, mapper, params);

	}

	/**
	 * @param estado
	 * @param transactionId
	 */
	public void modifyTransactionStatus(Integer estado, Integer transactionId) {
		String query = "update transacciones set estado = ? where id = ?";
		Object[] parameters = { estado, transactionId };
		this.jdbcTemplate.update(query, parameters);
	}
}
