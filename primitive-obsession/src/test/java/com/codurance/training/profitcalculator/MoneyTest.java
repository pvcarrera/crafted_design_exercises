package com.codurance.training.profitcalculator;

import org.junit.Test;

import static com.codurance.training.profitcalculator.Currency.GBP;
import static com.codurance.training.profitcalculator.Money.money;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MoneyTest {

	@Test public void
	should_inform_its_currency() {
	    assertThat(money(100, GBP).currency(), is(GBP));
	}

	@Test public void
	can_be_divided_by_a_number() {
	    assertThat(money(100, GBP).dividedBy(3), is(money(33, GBP)));
	}

	@Test public void
	can_be_converted_to_negative() {
	    assertThat(money(100, GBP).negative(), is(money(-100, GBP)));
	}
}
