package uni.social.connect.binding;


public class Response {

	public static enum Status {
		OK, ERROR;
	}
	
	private Object data;
	
	private Status status;
	
	public Response(Object data, Status status) {
		this.data = data;
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
