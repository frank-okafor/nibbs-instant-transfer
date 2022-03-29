package com.nibbsinstantpaymenttest.data;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ServiceResponse<T> {

	private HttpStatus status;
	private String message;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime dateTime;
	private T data;

	private ServiceResponse() {
		this.dateTime = LocalDateTime.now();
	}

	public ServiceResponse(HttpStatus status, String message) {
		this();
		this.status = status;
		this.message = message;
	}

	public ServiceResponse(HttpStatus status, Throwable e) {
		this();
		this.status = status;
		this.message = e.getLocalizedMessage();
	}

	public ServiceResponse(HttpStatus status, String message, T data) {
		this();
		this.status = status;
		this.message = message;
		this.data = data;
	}
}
