package lx.ghm.xelerator.util;

public class TimeoutException extends RuntimeException {
	private static final long serialVersionUID = -8078853655388692688L;
	
	public TimeoutException(){
		super();
	}
	
	public TimeoutException(String errMessage) {
		super(errMessage);
	}
	
}
