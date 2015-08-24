package com.modul;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.modul.Course;

@Service
public class WahlPflichtModule {

	public List<Course> parse(String path) throws IOException {
		Function<String, Course> parseToModul = line -> {
			String[] split = line.split(",");
			return new Course(split[0], Double.parseDouble(split[1]));
		};
		return Files.lines(Paths.get(path)).map(parseToModul).collect(Collectors.toList());
	}

}
