package uni.social.connect.advice;

import uni.social.connect.binding.Response;
import uni.social.connect.binding.Responses;
import uni.social.connect.exception.EmailExistsException;
import uni.social.connect.exception.ItemNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @Autowired
    ErrorAttributes errorAttributes;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${debug}")
    public boolean debug;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler({ItemNotFoundException.class, NoHandlerFoundException.class})
    public Response handleItemNotFound(ServletWebRequest request, Exception exception) {
        return buildError(request, HttpStatus.NOT_FOUND, exception);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({EmailExistsException.class})
    public Response handleMail(ServletWebRequest request, Exception exception) {
        return buildError(request, HttpStatus.BAD_REQUEST, exception);
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler({SessionAuthenticationException.class})
    public Response handleSessionAuthentication(ServletWebRequest request, Exception exception) {
        return buildError(request, HttpStatus.UNAUTHORIZED, exception);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public Response handleUnknownException(ServletWebRequest request, Exception exception) {
        return buildError(request, HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private Response buildError(ServletWebRequest request, HttpStatus status, Exception exception) {
       return buildError(request, status, exception, null);
    }

    private Response buildError(ServletWebRequest request, HttpStatus status, Exception exception,
                                Map<String, Object> additionalAttributes) {

        UUID uuid = UUID.randomUUID();
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(request, debug);
        attributes.put("status", status.value());
        attributes.put("error", status.getReasonPhrase());
        attributes.put("UUID", uuid.toString());
        if (debug && additionalAttributes != null && !additionalAttributes.isEmpty()) {
            attributes.putAll(additionalAttributes);
            logger.error("\nUUID: {} \nURI: {} \nInfo: {}", uuid.toString(), request.getRequest().getRequestURI(),
                    additionalAttributes, exception);
        }else {
            logger.error("\nUUID: {} \nURI: {}", uuid.toString(), request.getRequest().getRequestURI(), exception);
        }
        return Responses.error(attributes);
    }

}
