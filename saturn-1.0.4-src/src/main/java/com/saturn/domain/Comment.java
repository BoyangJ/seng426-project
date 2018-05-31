package com.saturn.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Comment extends AbstractDatedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 4, max = 20)
	@Column(name = "name", length = 20, nullable = false)
	private String name;

	@NotNull
	@Column(name = "email", nullable = false)
	private String email;

	@NotNull
	@Size(min = 5, max = 500)
	@Column(name = "content", length = 500, nullable = false)
	private String content;

	@ManyToOne
	@NotNull
	private BlogPost post;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Comment name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public Comment email(String email) {
		this.email = email;
		return this;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContent() {
		return content;
	}

	public Comment content(String content) {
		this.content = content;
		return this;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BlogPost getPost() {
		return post;
	}

	public Comment post(BlogPost blogPost) {
		this.post = blogPost;
		return this;
	}

	public void setPost(BlogPost blogPost) {
		this.post = blogPost;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Comment comment = (Comment) o;
		if (comment.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, comment.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Comment{"
			+ "id=" + id
			+ ", name='" + name + "'"
			+ ", email='" + email + "'"
			+ ", content='" + content + "'"
			+ ", createdDate='" + createdDate + "'"
			+ '}';
	}
}
