package org.crcaguilerapo.franquicia.domain.exceptions;

public enum ErrorCode {
    FRANCHISE_ALREADY_EXISTS("The franchise already exists"),
    FRANCHISE_DOES_NOT_EXISTS("The franchise does not exist"),
    BRANCH_ALREADY_EXISTS("The branch already exists"),
    BRANCH_DOES_NOT_EXISTS("The branch does not exist"),
    PRODUCT_ALREADY_EXISTS("The product already exists"),
    PRODUCT_DOES_NOT_EXISTS("The product does not exist"),
    DATA_IS_INCORRECT("The data is incorrect"),
    UNKNOWN("The error is unknown");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
