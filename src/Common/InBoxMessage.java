package Common;

public class InBoxMessage {

	/**
	 * this class is for the binding between the presentation of message in the in-box
	 *  (table view) to the instance.
	 */

	private String date;
	private String topic;
	private String content;

	/**
	 * constructor to build a instance of the class (use in table view of in box)
	 * @param date - the date of the message sent
	 * @param topic - the topic of the message.
	 * @param content - the content of the message.
	 */
	public InBoxMessage(String date, String topic, String content) {
		this.date = date;
		this.topic = topic;
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
