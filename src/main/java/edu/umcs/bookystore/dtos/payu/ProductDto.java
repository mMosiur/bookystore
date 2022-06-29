package edu.umcs.bookystore.dtos.payu;

public class ProductDto {

	private String name;
	private String unitPrice;
	private String quantity;

	public ProductDto() {
	}

	public ProductDto(String name, String unitPrice, String quantity) {
		this.name = name;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
