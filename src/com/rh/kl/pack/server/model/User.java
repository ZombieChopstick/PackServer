package com.rh.kl.pack.server.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
	private static final long serialVersionUID = -3666497934245791176L;
	@JsonIgnore
	@Getter @Setter private String userId;
	@Getter @Setter private String username;
	@JsonIgnore
	@Getter @Setter private String password;
	@Getter @Setter private String email;
	@Getter @Setter private Integer gold;
}
