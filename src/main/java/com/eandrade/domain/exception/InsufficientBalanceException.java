package com.eandrade.domain.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InsufficientBalanceException forAccount(String accountNumber, String attemptedAmount) {
        return new InsufficientBalanceException(
                String.format("La cuenta '%s' no tiene saldo disponible para realizar un movimiento de %s",
                        accountNumber, attemptedAmount)
        );
    }
}
