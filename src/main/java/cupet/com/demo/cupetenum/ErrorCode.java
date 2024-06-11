package cupet.com.demo.cupetenum;

public enum ErrorCode {
    AUTH_TOKEN_NOT_MATCH("Authentication token does not match"),
	AUTH_TOKEN_INVALID("AUTH_TOKEN_INVALID"),
	TOKEN_NOT_FOUND("TOKEN_NOT_FOUND"),
	AUTH_TOKEN_IS_NULL("AUTH_TOKEN_IS_NULL");
	
    private String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
