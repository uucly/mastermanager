package de.master.manager.myproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

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
		StringTokenizer token = new StringTokenizer(path, ",");
		List<Course> coursesOfProf = new ArrayList<>();
		while(token.hasMoreElements()){
			String course = (String) token.nextElement();
			System.out.println(course);
			coursesOfProf.add(allCourses.get(Integer.parseInt(course)-1));
		}
		//return coursesOfProf;
		return Pattern.compile(",").splitAsStream(path).map(s -> {System.out.println(s);return allCourses.get(Integer.parseInt(s)-1);}).collect(Collectors.toList());
	}

}
