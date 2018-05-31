package com.saturn.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.saturn.domain.BlogPost;
import com.saturn.domain.enumeration.BlogPostType;

import com.saturn.repository.BlogPostRepository;
import com.saturn.service.UserService;
import com.saturn.web.rest.util.HeaderUtil;
import com.saturn.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for managing BlogPost.
 */
@RestController
@RequestMapping("/api")
public class BlogPostResource {

	private final Logger log = LoggerFactory.getLogger(BlogPostResource.class);

	@Inject
	private BlogPostRepository blogPostRepository;

	@Inject
	private UserService userService;

	/**
	 * POST /blog-posts : Create a new blogPost.
	 *
	 * @param blogPost the blogPost to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new blogPost, or with status 400 (Bad
	 * Request) if the blogPost has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/blog-posts")
	@Timed
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER', 'EMPLOYEE'})")
	public ResponseEntity<BlogPost> createBlogPost(@Valid @RequestBody BlogPost blogPost) throws URISyntaxException {
		log.debug("REST request to save BlogPost : {}", blogPost.getTitle());
		if (blogPost.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("blogPost", "idexists", "A new blogPost cannot already have an ID")).body(null);
		}

		BlogPost result = blogPostRepository.save(blogPost);
		return ResponseEntity.created(new URI("/api/blog-posts/" + result.getId()))
			.headers(HeaderUtil.createEntityCreationAlert("blogPost", result.getId().toString()))
			.body(result);
	}

	/**
	 * PUT /blog-posts : Updates an existing blogPost.
	 *
	 * @param blogPost the blogPost to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated blogPost, or with status 400 (Bad
	 * Request) if the blogPost is not valid, or with status 500 (Internal Server Error) if the blogPost couldnt be
	 * updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/blog-posts")
	@Timed
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER', 'EMPLOYEE'})")
	public ResponseEntity<BlogPost> updateBlogPost(@Valid @RequestBody BlogPost blogPost) throws URISyntaxException {
		log.debug("REST request to update BlogPost : {}", blogPost.getTitle());
		if (blogPost.getId() == null) {
			return createBlogPost(blogPost);
		}
		BlogPost result = blogPostRepository.save(blogPost);
		return ResponseEntity.ok()
			.headers(HeaderUtil.createEntityUpdateAlert("blogPost", blogPost.getId().toString()))
			.body(result);
	}

	/**
	 * GET /blog-posts : get all the blogPosts.
	 *
	 * @param type
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of blogPosts in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/blog-posts")
	@Timed
	public ResponseEntity<List<BlogPost>> getAllBlogPosts(@ApiParam BlogPostType type, @ApiParam Pageable pageable)
		throws URISyntaxException {
		log.debug("REST request to get a page of BlogPosts");

		Page<BlogPost> page;

		if (type != null) {
			page = blogPostRepository.findAllByType(type, pageable);
		} else {
			page = blogPostRepository.findAll(pageable);
		}

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blog-posts");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /blog-posts/:id : get the "id" blogPost.
	 *
	 * @param id the id of the blogPost to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the blogPost, or with status 404 (Not Found)
	 */
	@GetMapping("/blog-posts/{id}")
	@Timed
	public ResponseEntity<BlogPost> getBlogPost(@PathVariable Long id) {
		log.debug("REST request to get BlogPost : {}", id);
		BlogPost blogPost = blogPostRepository.findOne(id);
		return Optional.ofNullable(blogPost)
			.map(result -> new ResponseEntity<>(
					result,
					HttpStatus.OK))
			.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /blog-posts/:id : delete the "id" blogPost.
	 *
	 * @param id the id of the blogPost to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/blog-posts/{id}")
	@Timed
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER', 'EMPLOYEE'})")
	public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id) {
		log.debug("REST request to delete BlogPost : {}", id);
		blogPostRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("blogPost", id.toString())).build();
	}
}
