package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class WahlPflichtModuleLoader implements Serializable{

	private static final long serialVersionUID = 1L;
	private final List<ModulCourse> allCourses;

	public WahlPflichtModuleLoader(String pathToAllCourse){
		Function<String, ModulCourse> parseToModul = line -> {
			String[] split = line.split(",");
			return new ModulCourse(split[0], Double.parseDouble(split[1]));
		};
		
		allCourses = Pattern.compile("\n").splitAsStream(pathToAllCourse).map(parseToModul).collect(Collectors.toList());
	}
	
	public List<ModulCourse> loadCourseOfProf(String path) {
		return Pattern.compile(",").splitAsStream(path).map(s -> allCourses.get(Integer.parseInt(s)-1)).collect(Collectors.toList());
	}

}
