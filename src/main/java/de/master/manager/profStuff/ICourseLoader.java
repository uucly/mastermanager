package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.List;

public interface ICourseLoader extends Serializable{

	List<ModulCourse> loadCourses(String fileName);
}
