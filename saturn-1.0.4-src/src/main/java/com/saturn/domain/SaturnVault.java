package com.saturn.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.bind.DatatypeConverter;

/**
 * A SaturnVault.
 */
@Entity
@Table(name = "saturnpass")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SaturnVault extends AbstractDatedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Key k = new SecretKeySpec(new byte[]{(byte) 0x21, (byte) 0x9e, (byte) 0x48, (byte) 0xd7,
		(byte) 0x50, (byte) 0x49, (byte) 0x1d, (byte) 0x8c, (byte) 0x1e, (byte) 0x37,
		(byte) 0x28, (byte) 0xaf, (byte) 0xcc, (byte) 0xfd, (byte) 0x9e, (byte) 0xc7}, "AES");

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 3)
	@Column(name = "site", nullable = false)
	private String site;

	@NotNull
	@Column(name = "login", nullable = false)
	private String login;

	@NotNull
	@Column(name = "password", nullable = false)
	private String password;

	@ManyToOne
	@NotNull
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSite() {
		return site;
	}

	public SaturnVault site(String site) {
		this.site = site;
		return this;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getLogin() {
		return login;
	}

	public SaturnVault login(String login) {
		this.login = login;
		return this;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		if (password != null) {
			try {
				Cipher c = Cipher.getInstance("AES");
				c.init(Cipher.DECRYPT_MODE, k);
				return new String(c.doFinal(DatatypeConverter.parseBase64Binary(password)));
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
				Logger.getLogger(SaturnVault.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return null;
	}

	public SaturnVault password(String password) {
		setPassword(password);
		return this;
	}

	public void setPassword(String password) {
		try {
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, k);
			this.password = DatatypeConverter.printBase64Binary(c.doFinal(password.getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
			Logger.getLogger(SaturnVault.class.getName()).log(Level.SEVERE, null, ex);
			this.password = null;
		}
	}

	public User getUser() {
		return user;
	}

	public SaturnVault user(User user) {
		this.user = user;
		return this;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SaturnVault saturnPass = (SaturnVault) o;
		if (saturnPass.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, saturnPass.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "SaturnVault{"
			+ "id=" + id
			+ ", site='" + site + "'"
			+ ", login='" + login + "'"
			+ ", password='" + password + "'"
			+ ", createdDate='" + createdDate + "'"
			+ ", lastModifiedDate='" + lastModifiedDate + "'"
			+ '}';
	}
}
