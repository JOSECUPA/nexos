/**
 * 
 */
package co.com.pruebTecnicaNexosP1MS.dto;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
public class Transaction {

	private String cardId;
	private Float price;
	private String amount;
	private String state;
	private Boolean isApproved;
	private String date;

	public Transaction() {
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
