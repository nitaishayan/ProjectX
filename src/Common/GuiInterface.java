package Common;

/**
 * This Interface is implemented by the GUI Controllers for specific GUI functions.
 * 
 * @param string
 */
public interface GuiInterface {
	
	/**
	 * This function is called in case the client receives a success feedback.
	 * @param string - the message is shown to the user.
	 */
		public void showSuccess(String string);
		
	/**
	 * This function receives the data from the server.
	 * @param obj - data object from the server.
	 */
		public void display(Object obj);
		
		
	/**
	 * This function is called in case the client receives a failure feedback.
	 * @param message - the message is shown to the user.
	 */		
		void showFailed(String message);
		
		
	/**
	 * This function is called in case some fields need to be "cleaned".
	 */		
		public void freshStart();
		
		
		
}
