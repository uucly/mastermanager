package com.germany;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WahlPflichtModule {

	public List<Modul> parse(String path) throws IOException {
		Function<String, Modul> parseToModul = line -> {
			String[] split = line.split(",");
			return new Modul(split[0], Double.parseDouble(split[1]));
		};
		return Files.lines(Paths.get(path)).map(parseToModul).collect(Collectors.toList());
	}

}
