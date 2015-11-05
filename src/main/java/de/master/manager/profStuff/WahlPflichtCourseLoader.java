package de.master.manager.profStuff;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class WahlPflichtCourseLoader implements ICourseLoader{

	private static final long serialVersionUID = 1L;
	private final List<ModulCourse> allCourses;

	public WahlPflichtCourseLoader(String pathToAllCourse){
		
		Function<String, ModulCourse> parseToModul = line -> {
			String[] split = line.split(",");
			return new ModulCourse(split[0], Double.parseDouble(split[1]));
		};
		
		allCourses = Pattern.compile("\n").splitAsStream(loadFileInput(pathToAllCourse)).map(parseToModul).collect(Collectors.toList());
	}
	
	/* methods */
	
	public List<ModulCourse> loadCourses(String fileName) {
		String fileInput = loadFileInput(fileName);
		return Pattern.compile(",").splitAsStream(fileInput).map(s -> allCourses.get(Integer.parseInt(s)-1)).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param fileName with file ending (e.g. fileName.txt)
	 * @return input of file
	 */
	private String loadFileInput(String fileName){
		FileSystemResourceLoader loader = new FileSystemResourceLoader();
		Resource resource = loader.getResource("/src/main/resources/" + fileName);
		try {
			InputStream pflichtResource = resource.getInputStream();
			return IOUtils.toString(pflichtResource, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException("Cannot load file" + fileName, e);
		}
	}

}
