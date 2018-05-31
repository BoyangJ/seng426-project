package com.saturn.repository;

import com.saturn.domain.BlogPost;
import com.saturn.domain.enumeration.BlogPostType;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the BlogPost entity.
 */
@SuppressWarnings("unused")
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

	@Query("select blogPost from BlogPost blogPost where blogPost.author.login = ?#{principal.username}")
	List<BlogPost> findByAuthorIsCurrentUser();

	Page<BlogPost> findAllByType(BlogPostType type, Pageable pageable);

}
