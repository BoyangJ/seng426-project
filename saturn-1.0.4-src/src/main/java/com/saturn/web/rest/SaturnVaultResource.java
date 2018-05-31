package com.saturn.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.saturn.service.SaturnVaultService;
import com.saturn.service.dto.SaturnVaultDTO;
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

/**
 * REST controller for managing SaturnVault.
 */
@RestController
@RequestMapping("/api")
public class SaturnVaultResource {

	private final Logger log = LoggerFactory.getLogger(SaturnVaultResource.class);

	@Inject
	private SaturnVaultService saturnPassService;

	/**
	 * POST /saturn-vaults : Create a new saturnPass.
	 *
	 * @param saturnPass the saturnPass to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new saturnPass, or with status 400 (Bad
	 * Request) if the saturnPass has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/saturn-vaults")
	@Timed
	public ResponseEntity<SaturnVaultDTO> createSaturnVault(@Valid @RequestBody SaturnVaultDTO saturnPass) throws URISyntaxException {
		log.debug("REST request to save SaturnVault : {}", saturnPass);
		if (saturnPass.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("saturnPass", "idexists", "A new saturnPass cannot already have an ID")).body(null);
		}
		SaturnVaultDTO result = saturnPassService.save(saturnPass);
		return ResponseEntity.created(new URI("/api/saturn-vaults/" + result.getId()))
			.headers(HeaderUtil.createEntityCreationAlert("saturnPass", result.getId().toString()))
			.body(result);
	}

	/**
	 * PUT /saturn-vaults : Updates an existing saturnPass.
	 *
	 * @param saturnPass the saturnPass to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated saturnPass, or with status 400 (Bad
	 * Request) if the saturnPass is not valid, or with status 500 (Internal Server Error) if the saturnPass couldnt be
	 * updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/saturn-vaults")
	@Timed
	public ResponseEntity<SaturnVaultDTO> updateSaturnVault(@Valid @RequestBody SaturnVaultDTO saturnPass) throws URISyntaxException {
		log.debug("REST request to update SaturnVault : {}", saturnPass);
		if (saturnPass.getId() == null) {
			return createSaturnVault(saturnPass);
		}
		SaturnVaultDTO result = saturnPassService.save(saturnPass);
		return ResponseEntity.ok()
			.headers(HeaderUtil.createEntityUpdateAlert("saturnPass", saturnPass.getId().toString()))
			.body(result);
	}

	/**
	 * GET /saturn-vaults : get all the saturnPasses.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of saturnPasses in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/saturn-vaults")
	@Timed
	public ResponseEntity<List<SaturnVaultDTO>> getAllSaturnVaults(@ApiParam Pageable pageable)
		throws URISyntaxException {
		log.debug("REST request to get a page of SaturnVaults");
		Page<SaturnVaultDTO> page = saturnPassService.findAllOfCurrentUser(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/saturn-vaults");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /saturn-vaults/:id : get the "id" saturnPass.
	 *
	 * @param id the id of the saturnPass to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the saturnPass, or with status 404 (Not Found)
	 */
	@GetMapping("/saturn-vaults/{id}")
	@Timed
	public ResponseEntity<SaturnVaultDTO> getSaturnVault(@PathVariable Long id) {
		log.debug("REST request to get SaturnVault : {}", id);
		SaturnVaultDTO saturnPass = saturnPassService.findOne(id);
		return Optional.ofNullable(saturnPass)
			.map(result -> new ResponseEntity<>(
					result,
					HttpStatus.OK))
			.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /saturn-vaults/:id : delete the "id" saturnPass.
	 *
	 * @param id the id of the saturnPass to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/saturn-vaults/{id}")
	@Timed
	public ResponseEntity<Void> deleteSaturnVault(@PathVariable Long id) {
		log.debug("REST request to delete SaturnVault : {}", id);
		saturnPassService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("saturnPass", id.toString())).build();
	}

}
