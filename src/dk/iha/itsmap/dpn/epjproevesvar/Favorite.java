package dk.iha.itsmap.dpn.epjproevesvar;

public class Favorite {
	private String cpr;
	private String name;
	private String color;
	private Boolean hasUnreadNews;
	private String notificationMode;
	
	public String getCpr() {
		return cpr;
	}
	public void setCpr(String cpr) {
		this.cpr = cpr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Boolean getHasUnreadNews() {
		return hasUnreadNews;
	}
	public void setHasUnreadNews(Boolean hasUnreadNews) {
		this.hasUnreadNews = hasUnreadNews;
	}
	public String getNotificationMode() {
		return notificationMode;
	}
	public void setNotificationMode(String notificationMode) {
		this.notificationMode = notificationMode;
	}
	
	
}
