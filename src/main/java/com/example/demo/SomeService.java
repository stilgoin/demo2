package com.example.demo;

import org.springframework.retry.annotation.Retryable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class SomeService {
	
	@Autowired
	Love love;
	
	public void duh() {
		love.retry();
	}
	
	@Retryable(value = RuntimeException.class,
			maxAttempts = 5, backoff = @Backoff(maxDelay=1000))
	public static void wut() {
		System.out.println("Ok no");
		throw new RuntimeException();
	}
		
	@Component
	private static class Love {
		public Love() {}		
		
		@Retryable(value = RuntimeException.class,
				maxAttempts = 5, backoff = @Backoff(maxDelay=1000))
		public void retry() {
			System.out.println("Pain now");
			throw new RuntimeException();
		}
	}	
}
