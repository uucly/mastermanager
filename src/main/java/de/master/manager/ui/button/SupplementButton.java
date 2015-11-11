package de.master.manager.ui.button;

import org.apache.wicket.model.IModel;
import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementCourses;

public class SupplementButton extends AbstractCourseButton{

	private static final long serialVersionUID = 1L;

	public SupplementButton(String id, ICourse course, IModel<Prof> prof) {
		super(id, course, prof.getObject());
		
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(isAlreadySelected((prof) -> prof.getSelectedSupplementCourses().getAllCourses())){
			setSelected();
		}
	}
	
	@Override
	protected void addModulFunction(Prof prof, ICourse course) {
		prof.getSelectedSupplementCourses().addCourse(course);
	}

}
