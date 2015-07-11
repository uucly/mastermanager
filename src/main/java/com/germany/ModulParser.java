package com.germany;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ModulParser {

	private final List<Modul> allModules;

	public ModulParser(List<Modul> allModules) throws IOException {
		this.allModules = allModules;

	}

	public List<Modul> parse(String path) throws IOException {
		System.out.println(allModules);
		return Files.lines(Paths.get(path))
				.map(l -> allModules.get(Integer.parseInt(l)-1))
				.collect(Collectors.toList());
	}
}
