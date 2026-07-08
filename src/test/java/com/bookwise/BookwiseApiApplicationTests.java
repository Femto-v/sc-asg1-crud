package com.bookwise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BookwiseApiApplicationTests {

	@Test
	void applicationClassShouldLoad() {
		assertDoesNotThrow(() -> new BookwiseApiApplication());
	}

}
