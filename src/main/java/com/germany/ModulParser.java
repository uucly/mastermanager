package com.germany;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ModulParser {

	public ModulParser() {
		File file = new File("src/main/resources/Breunig_Wahl.txt");
		if(!file.exists()){
			throw new RuntimeException("kein file");
		}
		
		try {
			Stream<String> lines = Files.lines(Paths.get("src/main/resources/Breunig_Wahl.txt"));
			Stream<String> wahlModule = Files.lines(Paths.get("src/main/resources/WahlPflichtModule.txt"));
			
		//	lines.forEach(l -> );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
