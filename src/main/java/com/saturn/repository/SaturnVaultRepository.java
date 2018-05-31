package com.saturn.repository;

import com.saturn.domain.SaturnVault;

import org.springframework.data.jpa.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the SaturnVault entity.
 */
@SuppressWarnings("unused")
public interface SaturnVaultRepository extends JpaRepository<SaturnVault, Long> {

	@Query("select saturnPass from SaturnVault saturnPass where saturnPass.user.login = ?#{principal.username}")
	Page<SaturnVault> findByUserIsCurrentUser(Pageable pageable);
}
