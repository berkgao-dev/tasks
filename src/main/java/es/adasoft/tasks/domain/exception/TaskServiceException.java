package es.adasoft.tasks.domain.exception;

public class TaskServiceException extends RuntimeException {
    public TaskServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
