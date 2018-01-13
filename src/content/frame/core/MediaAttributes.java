package content.frame.core;

/**
 * Information on piece of media content.
 *
 */
public class MediaAttributes {
	private String genre;
	private String year;
	private String creator;
	
	public MediaAttributes(String genre, String year, String creator) {
		this.genre = genre;
		this.year = year;
		this.creator = creator;
	}

	/**
	 * Get genre of content
     * @return genre
     */
	public String getGenre() {
		return this.genre;
	}
	
	/**
     * Get year content was released.
     * @return year
     */
	public String getYear() {
		return this.year;
	}
	
	/**
     * Get creator of content
     * @return creator
     */
	public String getCreator() {
		return this.creator;
	}
}
