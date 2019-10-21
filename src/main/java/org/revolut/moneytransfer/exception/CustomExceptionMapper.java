package org.revolut.moneytransfer.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.revolut.moneytransfer.model.ErrorEntity;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		List<ErrorEntity> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : violations) {
			ErrorEntity error = new ErrorEntity();
			error.setErrorMsg(violation.getMessage());
			errors.add(error);
		}
		GenericEntity<List<ErrorEntity>> messages = new GenericEntity<List<ErrorEntity>>(errors) {
		};
		return Response.status(Status.BAD_REQUEST).entity(messages).type(MediaType.APPLICATION_JSON).build();
	}

}
