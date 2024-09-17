package com.summer.common.exception;

import java.io.Serial;

/**
 * @author Renjun Yu
 * @date 2024/06/26 09:11
 */
public class ConflictException extends RuntimeException {

    public ConflictException() {
        super();
    }

    public ConflictException(String s) {
        super(s);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConflictException(Throwable cause) {
        super(cause);
    }

    @Serial
    private static final long serialVersionUID = -3906440265955534802L;
}
