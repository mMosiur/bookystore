package edu.umcs.bookystore.dtos.payu;

public class OrderCreationNotifyDto {

	private OrderCreationNotifyOrderDto order;

	// If needed more DTO properties could be added.
	// As for now only order is used, so only status is present in the DTO.

	public OrderCreationNotifyDto() {
	}

	public OrderCreationNotifyDto(OrderCreationNotifyOrderDto order) {
		this.order = order;
	}

	public OrderCreationNotifyOrderDto getOrder() {
		return order;
	}

	public void setOrder(OrderCreationNotifyOrderDto order) {
		this.order = order;
	}

}
