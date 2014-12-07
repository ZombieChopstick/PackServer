package com.rh.kl.pack.server.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
	@Getter @Setter private String username;
	@Getter @Setter private String password;
}
