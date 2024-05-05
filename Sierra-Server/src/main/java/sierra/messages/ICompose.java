package sierra.messages;

import sierra.netty.readers.Response;

public abstract class ICompose {

	protected Response response;
	
	public ICompose() {
		this.response = new Response();
	}
	
	public abstract Response compose();
}
