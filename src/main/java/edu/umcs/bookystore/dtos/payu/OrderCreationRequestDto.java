package edu.umcs.bookystore.dtos.payu;

import java.util.List;

public class OrderCreationRequestDto {

	private String notifyUrl;
	private String customerIp;
	private String continueUrl;
	private String merchantPosId;
	private String description;
	private String currencyCode;
	private String totalAmount;
	private BuyerDto buyer;
	private List<ProductDto> products;

	public OrderCreationRequestDto() {
	}

	public OrderCreationRequestDto(String notifyUrl, String customerIp, String continueUrl, String merchantPosId,
			String description, String currencyCode, String totalAmount, BuyerDto buyer, List<ProductDto> products) {
		this.notifyUrl = notifyUrl;
		this.customerIp = customerIp;
		this.continueUrl = continueUrl;
		this.merchantPosId = merchantPosId;
		this.description = description;
		this.currencyCode = currencyCode;
		this.totalAmount = totalAmount;
		this.buyer = buyer;
		this.products = products;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getCustomerIp() {
		return customerIp;
	}

	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}

	public String getContinueUrl() {
		return continueUrl;
	}

	public void setContinueUrl(String continueUrl) {
		this.continueUrl = continueUrl;
	}

	public String getMerchantPosId() {
		return merchantPosId;
	}

	public void setMerchantPosId(String merchantPosId) {
		this.merchantPosId = merchantPosId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BuyerDto getBuyer() {
		return buyer;
	}

	public void setBuyer(BuyerDto buyer) {
		this.buyer = buyer;
	}

	public List<ProductDto> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDto> products) {
		this.products = products;
	}

}
