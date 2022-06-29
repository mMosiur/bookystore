package edu.umcs.bookystore.dtos.payu;

public class OrderCreationResponseDto {

	private String redirectUri;
	private String orderId;

	public OrderCreationResponseDto() {
	}

	public OrderCreationResponseDto(String redirectUri, String orderId) {
		this.redirectUri = redirectUri;
		this.orderId = orderId;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
