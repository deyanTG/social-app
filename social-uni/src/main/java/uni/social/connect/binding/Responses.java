package uni.social.connect.binding;

import java.util.Collections;


public abstract class Responses {

	
	public static final Response OK = ok(Collections.emptyMap());
	
	public static Response ok(Object data) {
		return new Response(data, Response.Status.OK);
	}
	
	public static Response ok() {
		return new Response(null, Response.Status.OK);
	}
	
	public static Response error(Object data) {
		return new Response(data, Response.Status.ERROR);
	}
	
}
