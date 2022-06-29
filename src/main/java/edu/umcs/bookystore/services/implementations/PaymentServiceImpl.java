package edu.umcs.bookystore.services.implementations;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.umcs.bookystore.dtos.payu.BuyerDto;
import edu.umcs.bookystore.dtos.payu.OrderCreationRequestDto;
import edu.umcs.bookystore.dtos.payu.OrderCreationResponseDto;
import edu.umcs.bookystore.dtos.payu.ProductDto;
import edu.umcs.bookystore.entities.BookEntity;
import edu.umcs.bookystore.entities.OrderEntity;
import edu.umcs.bookystore.entities.UserEntity;
import edu.umcs.bookystore.repositories.OrderRepository;
import edu.umcs.bookystore.services.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final RestTemplate restTemplate;
	private final OrderRepository orderRepository;

	@Value("${bookystore.payment.orderCreationUrl}")
	public String ORDER_CREATION_URL;
	// "https://secure.payu.com/api/v2_1/orders"

	@Value("${bookystore.payment.authToken}")
	public String AUTH_TOKEN;
	// "3e5cac39-7e38-4139-8fd6-30adc06a61bd";

	@Value("${bookystore.payment.notifyUrlTemplate}")
	public String NOTIFY_URL_TEMPLATE;
	// "https://bookystore.azurewebsites.net/order/payment/notify/%d";

	@Value("${bookystore.payment.continueUrlTemplate}")
	public String CONTINUE_URL_TEMPLATE;
	// "https://bookystore.azurewebsites.net/order/%d/detail";

	public PaymentServiceImpl(OrderRepository orderRepository) {
		this.restTemplate = new RestTemplate();
		this.orderRepository = orderRepository;
	}

	public String initializePayForOrder(Long orderId) {
		OrderEntity order = orderRepository.findById(orderId).orElseThrow(
				() -> new NoSuchElementException("Order not found"));
		if (order.isPaid()) {
			throw new IllegalStateException("Order is already paid");
		}

		// create headers
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(AUTH_TOKEN);
		headers.setContentType(MediaType.APPLICATION_JSON);

		// build the request
		OrderCreationRequestDto body = buildBody(order);
		HttpEntity<OrderCreationRequestDto> request = new HttpEntity<>(body, headers);

		// use `exchange` method for HTTP call
		ResponseEntity<OrderCreationResponseDto> response = this.restTemplate.exchange(ORDER_CREATION_URL,
				HttpMethod.POST, request, OrderCreationResponseDto.class);
		if (!response.getStatusCode().isError()) {
			return response.getBody().getRedirectUri();
		} else {
			throw new IllegalStateException("Payment initialization failed");
		}
	}

	private OrderCreationRequestDto buildBody(OrderEntity order) {
		UserEntity user = order.getUser();
		Set<BookEntity> books = order.getBooks();
		BuyerDto buyer = new BuyerDto(
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				"en");
		List<ProductDto> products = books.stream()
				.map(b -> new ProductDto(
						b.getTitle(),
						String.format("%d", (int) (b.getPrice() * 100)),
						"1"))
				.collect(Collectors.toList());
		Integer totalAmount = books.stream()
				.mapToInt(b -> (int) (b.getPrice() * 100)).sum();
		OrderCreationRequestDto request = new OrderCreationRequestDto(
				String.format(NOTIFY_URL_TEMPLATE, order.getId()),
				"127.0.0.1",
				String.format(CONTINUE_URL_TEMPLATE, order.getId()),
				"145227",
				"Books",
				"PLN",
				totalAmount.toString(),
				buyer,
				products);
		return request;
	}

}
