package edu.umcs.bookystore.dtos.payu;

public class OrderCreationNotifyOrderDto {

	private String status;

	// If needed more DTO properties could be added.
	// As for now only status is used, so only status is present in the DTO.

	public OrderCreationNotifyOrderDto() {
	}

	public OrderCreationNotifyOrderDto(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
