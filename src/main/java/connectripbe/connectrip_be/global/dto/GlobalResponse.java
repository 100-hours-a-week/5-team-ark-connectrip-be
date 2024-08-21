package connectripbe.connectrip_be.global.dto;


public record GlobalResponse<T>(
        String message,
        T data) {
}
