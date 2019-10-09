package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tanukisoftware.wrapper.WrapperManager;

import com.app.MainApp;


public class Service implements org.tanukisoftware.wrapper.WrapperListener {
	Logger logger = LoggerFactory.getLogger(Service.class);

	/**
	 * The start method is called when the WrapperManager is signaled by the
	 * native wrapper code that it can start its application. This method call
	 * is expected to return, so a new thread should be launched it necessary.
	 * If there are any problems, then an Integer should be returned, set to the
	 * desired exit code. If the application should continue, return null.
	 */
	public Integer start(String[] args) {

		try {
			logger.info("starting server");
			new Thread(new Runnable() {
				public void run() {
					MainApp app = new MainApp();
			        app.startServer();
				}
			}).start();
			logger.info("server started");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
		return null;
	}

	/**
	 * Called when the application is shutting down.
	 */
	public int stop(int exitCode) {
		return 0;
	}

	/**
	 * Called whenever the native wrapper code traps a system control signal
	 * against the Java process. It is up to the callback to take any actions
	 * necessary. Possible values are: WrapperManager.WRAPPER_CTRL_C_EVENT,
	 * WRAPPER_CTRL_CLOSE_EVENT, WRAPPER_CTRL_LOGOFF_EVENT, or
	 * WRAPPER_CTRL_SHUTDOWN_EVENT
	 */
	public void controlEvent(int event) {

	}

	public static void main(String[] args) {
		WrapperManager.start(new Service(), args);

	}

}