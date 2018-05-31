package com.saturn.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.saturn.domain.enumeration.BlogPostType;

/**
 * A BlogPost.
 */
@Entity
@Table(name = "blog_post")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BlogPost extends AbstractDatedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 5, max = 255)
	@Column(name = "title", length = 255, nullable = false)
	private String title;

	@NotNull
	@Size(min = 40)
	@Column(name = "content", nullable = false)
	private String content;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private BlogPostType type;

	@ManyToOne
	@NotNull
	private User author;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<Comment> comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public BlogPost title(String title) {
		this.title = title;
		return this;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public BlogPost content(String content) {
		this.content = content;
		return this;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BlogPostType getType() {
		return type;
	}

	public BlogPost type(BlogPostType type) {
		this.type = type;
		return this;
	}

	public void setType(BlogPostType type) {
		this.type = type;
	}

	public User getAuthor() {
		return author;
	}

	public BlogPost author(User user) {
		this.author = user;
		return this;
	}

	public void setAuthor(User user) {
		this.author = user;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public BlogPost comments(List<Comment> comments) {
		this.comments = comments;
		return this;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BlogPost blogPost = (BlogPost) o;
		if (blogPost.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, blogPost.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "BlogPost{"
			+ "id=" + id
			+ ", title='" + title + "'"
			+ ", content length=" + content.length() + "chars"
			+ ", createdDate='" + createdDate + "'"
			+ ", type='" + type + "'"
			+ '}';
	}
}
