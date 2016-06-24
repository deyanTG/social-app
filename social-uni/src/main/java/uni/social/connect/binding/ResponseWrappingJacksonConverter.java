package uni.social.connect.binding;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class ResponseWrappingJacksonConverter extends MappingJackson2HttpMessageConverter {

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws HttpMessageNotWritableException, IOException {
        if (object instanceof ResponseEntity<?>) { // preserve original headers and status
            ResponseEntity<?> re = (ResponseEntity<?>) object;
            if ((re.getBody() instanceof Response)) {
                super.writeInternal(object, type, outputMessage);
            } else {
                super.writeInternal(Responses.ok(re.getBody()), type, outputMessage);
            }
        } else if (object instanceof Response) {
            super.writeInternal(object, type, outputMessage);
        } else {
            super.writeInternal(Responses.ok(object), type, outputMessage);
        }
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return true;
    }
}
