package com.modul;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CourseParser implements Serializable{

	private static final long serialVersionUID = 1L;
	private final List<Course> allModules;

	public CourseParser(List<Course> allModules) {
		this.allModules = allModules;

	}

	public List<Course> loadCourseOfProf(String path) {
		try {
			return Files.lines(Paths.get(path))
					.map(l -> allModules.get(Integer.parseInt(l)-1))
					.collect(Collectors.toList());
		} catch (NumberFormatException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
