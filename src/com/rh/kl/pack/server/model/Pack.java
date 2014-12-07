package com.rh.kl.pack.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Pack {
	
	@Getter @Setter private Integer id;
	@Getter @Setter private String name;
	@Getter @Setter private Integer price;
	@Getter @Setter private String description;
}
