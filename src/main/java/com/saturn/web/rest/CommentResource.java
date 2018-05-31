package com.saturn.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.saturn.domain.Comment;

import com.saturn.repository.CommentRepository;
import com.saturn.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for managing Comment.
 */
@RestController
@RequestMapping("/api")
public class CommentResource {

	private final Logger log = LoggerFactory.getLogger(CommentResource.class);

	@Inject
	private CommentRepository commentRepository;

	/**
	 * POST /comments : Create a new comment.
	 *
	 * @param comment the comment to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new comment, or with status 400 (Bad
	 * Request) if the comment has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/comments")
	@Timed
	public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) throws URISyntaxException {
		log.debug("REST request to save Comment : {}", comment);

		if (comment.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("comment", "idexists", "A new comment cannot already have an ID")).body(null);
		}

		comment.setContent(sanitize(comment.getContent()));

		Comment result = commentRepository.save(comment);

		return ResponseEntity.created(new URI("/api/comments/" + result.getId()))
			.headers(HeaderUtil.createEntityCreationAlert("comment", result.getId().toString()))
			.body(result);
	}

	private String sanitize(String comment) {
		StringBuilder sb = new StringBuilder();

		Pattern pattern = Pattern.compile("(.*?)\\[(b|i|url)\\](.*?)\\[\\/\\2\\]((.|\\n)*)");

		String unsanitized = comment;

		while (!unsanitized.isEmpty()) {
			Matcher matcher = pattern.matcher(unsanitized);

			if (matcher.matches()) {
				sb.append(matcher.group(1).replaceAll("<", "&lg;").replaceAll(">", "&gt;"));

				String tagName = matcher.group(2);

				if (tagName.equals("url")) {
					sb.append("<a href='").append(matcher.group(3)).append("'>")
						.append(matcher.group(3)).append("</a>");
				} else {
					sb.append("<").append(tagName).append(">").append(matcher.group(3))
						.append("</").append(tagName).append(">");
				}

				unsanitized = matcher.group(4);
			} else {
				sb.append(unsanitized.replaceAll("<", "&lg;").replaceAll(">", "&gt;"));
				unsanitized = "";
			}
		}

		return sb.toString();
	}

	/**
	 * GET /comments : get all the comment of a blogPost.
	 *
	 * @param postId
	 * @return the ResponseEntity with status 200 (OK) and the list of comments in body
	 */
	@GetMapping("/comments")
	@Timed
	public ResponseEntity<List<Comment>> getAllBlogPosts(@ApiParam Long postId) {
		log.debug("REST request to get a page of Comments");

		return new ResponseEntity<>(commentRepository.findAllByPostId(postId), HttpStatus.OK);
	}

	/**
	 * DELETE /comments/:id : delete the "id" comment.
	 *
	 * @param id the id of the comment to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/comments/{id}")
	@Timed
	@PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER', 'EMPLOYEE'})")
	public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
		log.debug("REST request to delete Comment : {}", id);
		commentRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("comment", id.toString())).build();
	}

}
