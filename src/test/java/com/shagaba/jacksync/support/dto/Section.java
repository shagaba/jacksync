package com.shagaba.jacksync.support.dto;

import java.util.List;

public class Section {

	private String title;

	private List<Paragraph> paragraphs;

	private String privateNote;


    /**
	 * 
	 */
	public Section() {
		super();
	}

	/**
	 * 
	 * @param title
	 */
	public Section(String title) {
		super();
		this.title = title;
	}

	/**
	 * @param title
	 * @param paragraphs
	 */
	public Section(String title, List<Paragraph> paragraphs) {
		super();
		this.title = title;
		this.paragraphs = paragraphs;
	}

	/**
	 * 
	 * @param title
	 * @param paragraphs
	 * @param privateNote
	 */
	public Section(String title, List<Paragraph> paragraphs, String privateNote) {
		super();
		this.title = title;
		this.paragraphs = paragraphs;
		this.privateNote = privateNote;
	}

	/**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the paragraphs
     */
    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    /**
     * @param paragraphs the paragraphs to set
     */
    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

	/**
	 * @return the privateNote
	 */
	public String getPrivateNote() {
		return privateNote;
	}

	/**
	 * @param privateNote the privateNote to set
	 */
	public void setPrivateNote(String privateNote) {
		this.privateNote = privateNote;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Section {title=").append(title).append(", paragraphs=").append(paragraphs)
				.append(", privateNote=").append(privateNote).append("}");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paragraphs == null) ? 0 : paragraphs.hashCode());
		result = prime * result + ((privateNote == null) ? 0 : privateNote.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Section other = (Section) obj;
		if (paragraphs == null) {
			if (other.paragraphs != null)
				return false;
		} else if (!paragraphs.equals(other.paragraphs))
			return false;
		if (privateNote == null) {
			if (other.privateNote != null)
				return false;
		} else if (!privateNote.equals(other.privateNote))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
