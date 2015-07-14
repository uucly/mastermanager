package com.germany;

import org.springframework.stereotype.Service;

@Service
public class TestImpl implements TestService {

	@Override
	public String say() {
		return "Jo";
	}

}
