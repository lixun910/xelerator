package lx.ghm.xelerator.util;

public class TimeoutThread extends Thread {
	private long timeout;
	private boolean isCanceled = false;
	private TimeoutException timeoutException = new TimeoutException();

	public TimeoutThread(long timeout) {
		super();
		this.timeout = timeout;
		//���ñ��߳�Ϊ�ػ��߳�
		this.setDaemon(true);
	}

	public void cancel() {
		isCanceled = true;
	}

	public void run() {
		try {
			Thread.sleep(timeout);
			if (!isCanceled)
				throw timeoutException;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
