package cupet.com.demo;

import cupet.com.demo.cupetenum.ErrorCode;

public class MyCupetApplicationException extends RuntimeException {
    private ErrorCode errorCode;

    public MyCupetApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
