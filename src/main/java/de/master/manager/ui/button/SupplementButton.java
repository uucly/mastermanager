package de.master.manager.ui.button;

import org.apache.wicket.model.IModel;
import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementCourses;

public class SupplementButton extends AbstractCourseButton{

	private static final long serialVersionUID = 1L;
	private final SupplementCourses supplementCourses;

	public SupplementButton(String id, ICourse course, IModel<Prof> prof, SupplementCourses supplementCourses) {
		super(id, course, prof.getObject());
		this.supplementCourses = supplementCourses;
		
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(isAlreadySelected((prof) -> supplementCourses.getAllCourses())){
			setSelected();
		}
	}
	
	@Override
	protected void addModulFunction(Prof prof, ICourse course) {
		supplementCourses.addCourse(course);
	}

}
