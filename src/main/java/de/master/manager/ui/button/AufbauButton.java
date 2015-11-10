package de.master.manager.ui.button;

import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;

public class AufbauButton extends AbstractCourseButton{
	private static final long serialVersionUID = 1L;

	public AufbauButton(String id, ICourse course, IModel<Prof> prof) {
		super(id, course, prof.getObject());
		
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(isAlreadySelected((prof) -> prof.getBasicCourses().getCourses())){
			setSelected();
		}
	}
	
	@Override
	protected void addModulFunction(Prof prof, ICourse course) {
		prof.getBasicCourses().addCourse(course);
	}
}
