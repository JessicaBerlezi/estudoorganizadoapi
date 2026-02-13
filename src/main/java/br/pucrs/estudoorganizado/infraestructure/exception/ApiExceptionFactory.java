package br.pucrs.estudoorganizado.infraestructure.exception;

import br.pucrs.estudoorganizado.entity.enumerate.BusinessError;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class ApiExceptionFactory {

    private ApiExceptionFactory() {
    }

    public static ResponseStatusException badRequest(BusinessError error) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, error.message());
    }

    public static ResponseStatusException notFound(BusinessError error) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, error.message());
    }

    public static ResponseStatusException conflict(BusinessError error) {
        return new ResponseStatusException(HttpStatus.CONFLICT, error.message());
    }

    public static ResponseStatusException internalError(BusinessError error) {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.message());
    }
}
