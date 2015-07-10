package com.germany;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class WahlPflichtModule {

	private final Map<String, Double> wahlModulMap;

	public WahlPflichtModule(Map<String, Double> wahlModulMap) throws IOException {
		this.wahlModulMap = wahlModulMap;
		Stream<String> modulLines = Files.lines(Paths.get("src/main/resources/WahlPflichtModule.txt"));
		modulLines.forEach(l -> { String[] split = l.split(","); this.wahlModulMap.put(split[0], Double.parseDouble(split[1])); });
	}

	public Map<String, Double> getWahlModulMap() {
		return wahlModulMap;
	}
}
