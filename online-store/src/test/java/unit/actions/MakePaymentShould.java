package unit.actions;

import actions.MakePayment;
import model.payment.PaymentGateway;
import model.shopping.Basket;
import model.payment.PaymentDetails;
import model.payment.PaymentStatus;
import model.stock.Stock;
import model.stock.StockCheck;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static model.stock.StockCheckStatus.IN_STOCK;
import static model.stock.StockCheckStatus.OUT_OF_STOCK;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MakePaymentShould {

	public static final String SOCRA_BOOK_NOT_IN_STOCK = "SoCra Book not in stock";
	private MakePayment makePayment;
	private Basket basket;
	private PaymentDetails paymentDetails;

	@Mock private Stock stock;
	@Mock private PaymentGateway paymentGateway;

	@Before
	public void initialise() {
		makePayment = new MakePayment(stock, paymentGateway);
		basket = new Basket();
		paymentDetails = new PaymentDetails();
	}

	@Test public void
	return_payment_status_after_submiting_payment_details() {
		givenStockCheckIsSuccessful();

		assertThat(makePayment.execute(basket, paymentDetails), isA(PaymentStatus.class));
	}

	@Test public void
	inform_when_items_are_out_of_stock() {
		givenStockCheckWillFailWithMessage(SOCRA_BOOK_NOT_IN_STOCK);

		PaymentStatus paymentStatus = makePayment.execute(basket, paymentDetails);

		assertPaymentFailedWithMessage(paymentStatus, SOCRA_BOOK_NOT_IN_STOCK);
	}

	@Test public void
	send_payment_details_to_be_processed() {
		givenStockCheckIsSuccessful();

		makePayment.execute(basket, paymentDetails);

		verify(paymentGateway).makePaymentWith(paymentDetails);
	}

	private void givenStockCheckIsSuccessful() {
		stockCheckWillReturn(inStock());
	}

	private void givenStockCheckWillFailWithMessage(String errorMessage) {
		stockCheckWillReturn(notInStockWithMessage(errorMessage));
	}

	private void stockCheckWillReturn(StockCheck value) {
		given(stock.contains(basket.items())).willReturn(value);
	}

	private void assertPaymentFailedWithMessage(PaymentStatus paymentStatus, String errorMessage) {
	    assertThat(paymentStatus.fail(), is(true));
		assertThat(paymentStatus.messages().get(0), is(errorMessage));
	}

	private StockCheck notInStockWithMessage(String... messages) {
		return new StockCheck(OUT_OF_STOCK, messages);
	}

	private StockCheck inStock() {
		return new StockCheck(IN_STOCK);
	}


}
