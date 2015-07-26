package com.modul;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ModulParser implements Serializable{

	private static final long serialVersionUID = 1L;
	private final List<Modul> allModules;

	public ModulParser(List<Modul> allModules) {
		this.allModules = allModules;

	}

	public List<Modul> parse(String path) throws IOException {
		return Files.lines(Paths.get(path))
				.map(l -> allModules.get(Integer.parseInt(l)-1))
				.collect(Collectors.toList());
	}
}