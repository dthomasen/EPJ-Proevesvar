package dk.iha.itsmap.dpn.epjproevesvar.business;

public class AddUpdateFavoritePatient {
	private String note;
	private Color color;
	
	public AddUpdateFavoritePatient(String note, Color color){
		this.note = note;
		this.color = color;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}
