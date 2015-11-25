package de.master.manager.profStuff;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import de.master.manager.ui.model.SerializableFunction;

@Service
public class CourseLoader implements IModulLoader {

	private static final long serialVersionUID = 1L;
	private final String pathToAllCourse;

	public CourseLoader(String pathToAllCourse) {
		this.pathToAllCourse = pathToAllCourse;
	}


	/* methods */

	public IModul loadModul(String fileName) {
		List<ICourse> allCourses = loadAllCourses(pathToAllCourse);
		
		List<ICourse> courses = new ArrayList<>();
		String fileInput = loadFileInput(fileName);
		StringTokenizer token = new StringTokenizer(fileInput, ",");
		while (token.hasMoreElements()) {
			ICourse course = allCourses.get(Integer.parseInt(token.nextToken()) -1 );
			courses.add(course);
		}
		
		return new Modul(courses);
	}

	private static List<ICourse> loadAllCourses(String pathToAllCourse) {
		SerializableFunction<String, ICourse> parseToModul = new SerializableFunction<String, ICourse>() {
			
			@Override
			public ICourse apply(String line) {
				String[] split = line.split(",");
				return new ModulCourse(split[0], Double.parseDouble(split[1]));
			}
		};
		StringTokenizer tokenizer = new StringTokenizer(loadFileInput(pathToAllCourse), "\n");
		List<ICourse> allCourses = new ArrayList<>();
		while (tokenizer.hasMoreElements()) {
			allCourses.add(parseToModul.apply(tokenizer.nextToken()));
		}
		return allCourses;
	}
	/**
	 * 
	 * @param fileName
	 *            with file ending (e.g. fileName.txt)
	 * @return input of file
	 */
	private static String loadFileInput(String fileName) {
		try (InputStream pflichtResource = CourseLoader.class.getResourceAsStream("/"+ fileName);) {
			return loadFileInputFromInputStream(pflichtResource);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load file" + fileName, e);
		}
	}

	private static String loadFileInputFromInputStream(InputStream pflichtResource) {
		try {
			return IOUtils.toString(pflichtResource, "UTF-8");
		} catch (IOException ioE) {
			throw new RuntimeException("Cannot close inputstream", ioE);
		} finally {
			close(pflichtResource);
		}
	}

	private static void close(InputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
		}
	}

}
