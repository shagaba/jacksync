package com.shagaba.jacksonpatch.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.shagaba.jacksonpatch.Syncable;

public class Post implements Syncable{
	
	private String id;
	
	private Long version;

	private String title;
    
	private LocalDateTime publishedTime;
    
	private List<Section> sections;
    
	private List<String> categories;
    
	private List<String> tags;
    
    private Author author;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
    @Override
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
    @Override
	public void setVersion(Long version) {
		this.version = version;
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
	 * @return the publishedTime
	 */
	public LocalDateTime getPublishedTime() {
		return publishedTime;
	}

	/**
	 * @param publishedTime the publishedTime to set
	 */
	public void setPublishedTime(LocalDateTime publishedTime) {
		this.publishedTime = publishedTime;
	}

	/**
	 * @return the sections
	 */
	public List<Section> getSections() {
		return sections;
	}

	/**
	 * @param sections the sections to set
	 */
	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Post {id=").append(id).append(", version=").append(version).append(", title=").append(title)
				.append(", publishedTime=").append(publishedTime).append(", sections=").append(sections)
				.append(", categories=").append(categories).append(", tags=").append(tags).append(", author=")
				.append(author).append("}");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((publishedTime == null) ? 0 : publishedTime.hashCode());
		result = prime * result + ((sections == null) ? 0 : sections.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + (int) (version ^ (version >>> 32));
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
		Post other = (Post) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (publishedTime == null) {
			if (other.publishedTime != null)
				return false;
		} else if (!publishedTime.equals(other.publishedTime))
			return false;
		if (sections == null) {
			if (other.sections != null)
				return false;
		} else if (!sections.equals(other.sections))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

    
}
