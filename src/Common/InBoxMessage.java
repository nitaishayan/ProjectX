package Common;

public class InBoxMessage {
	
	private String date;
	private String topic;
	private String content;
	
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
