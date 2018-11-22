package ba.enox.rest.webservices.roomoptimization.model.enums;

/**
 * @author eno Simple String values to distinguish Premium rooms and Economy
 *         rooms.
 */
public enum RoomTypes {

	PREMIUM_ROOM("Premium Room"), ECONOMY_ROOM("Economy Room");

	private final String text;

	/**
	 * @param text
	 */
	RoomTypes(final String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return text;
	}
}
