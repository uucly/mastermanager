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
public class CourseLoader implements IModulLoader {

	private static final long serialVersionUID = 1L;
	private final List<ICourse> allCourses;

	public CourseLoader(String pathToAllCourse) {

		Function<String, ICourse> parseToModul = line -> {
			String[] split = line.split(",");
			return new ModulCourse(split[0], Double.parseDouble(split[1]));
		};

		allCourses = Pattern.compile("\n").splitAsStream(loadFileInput(pathToAllCourse)).map(parseToModul)
				.collect(Collectors.toList());
	}

	/* methods */

	public IModul loadModul(String fileName) {
		String fileInput = loadFileInput(fileName);
		return new Modul(Pattern.compile(",").splitAsStream(fileInput).map(s -> allCourses.get(Integer.parseInt(s) - 1))
				.collect(Collectors.toList()));
	}

	/**
	 * 
	 * @param fileName
	 *            with file ending (e.g. fileName.txt)
	 * @return input of file
	 */
	private String loadFileInput(String fileName) {
		try (InputStream pflichtResource = CourseLoader.class.getResourceAsStream("/"+ fileName);) {
			return loadFileInputFromInputStream(pflichtResource);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load file" + fileName, e);
		}
	}

	private String loadFileInputFromInputStream(InputStream pflichtResource) {
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
