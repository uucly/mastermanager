package de.master.manager.myproject;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WahlPflichtModuleLoader implements Serializable{

	private static final long serialVersionUID = 1L;
	private final List<Course> allCourses;

	public WahlPflichtModuleLoader(String pathToAllCourse){
		Function<String, Course> parseToModul = line -> {
			String[] split = line.split(",");
			return new Course(split[0], Double.parseDouble(split[1]));
		};
		
		allCourses = Pattern.compile("\n").splitAsStream(pathToAllCourse).map(parseToModul).collect(Collectors.toList());
	}
	
	public List<Course> loadCourseOfProf(String path) {
		return Pattern.compile(",").splitAsStream(path).map(s -> allCourses.get(Integer.parseInt(s)-1)).collect(Collectors.toList());
	}

}
