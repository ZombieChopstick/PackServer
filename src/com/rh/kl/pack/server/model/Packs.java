package com.rh.kl.pack.server.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Packs {
	@Getter @Setter private List<Pack> packs;
}
