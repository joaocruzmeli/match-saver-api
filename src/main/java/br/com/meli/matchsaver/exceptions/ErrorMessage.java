package br.com.meli.matchsaver.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ErrorMessage {
    private HttpStatus status;
    private int statusCode;
    private List<String> messages;

    public ErrorMessage(HttpStatus status, int statusCode, List<String> messages) {
        super();
        this.status = status;
        this.statusCode = statusCode;
        this.messages = messages;
    }

    public ErrorMessage(HttpStatus status, int statusCode, String message) {
        super();
        this.status = status;
        this.statusCode = statusCode;
        this.messages = Arrays.asList(message);
    }
}
