package com.modul;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.modul.Course;

@Service
public class WahlPflichtModuleLoader implements Serializable{

	private static final long serialVersionUID = 1L;
	private String pathToAllCourse;

	public WahlPflichtModuleLoader(String pathToAllCourse){
		this.pathToAllCourse = pathToAllCourse;
	}
	
	private static List<Course> loadAllWahlCourseOfPath(String path) {
		Function<String, Course> parseToModul = line -> {
			String[] split = line.split(",");
			return new Course(split[0], Double.parseDouble(split[1]));
		};
		
		
		try {
			return Files.lines(Paths.get(path)).map(parseToModul).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Course> loadCourseOfProf(String path) {
		try {
			List<Course> allCourses = loadAllWahlCourseOfPath(pathToAllCourse);
			return Files.lines(Paths.get(path))
					.map(l -> allCourses.get(Integer.parseInt(l)-1))
					.collect(Collectors.toList());
		} catch (NumberFormatException | IOException e) {
			throw new RuntimeException(e);
		}
	}

}
