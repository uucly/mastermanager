package de.master.manager.ui.button;

import org.apache.wicket.model.IModel;
import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementCourses;
import de.master.manager.profStuff.SupplementModul;

public class SupplementButton extends AbstractCourseButton{

	private static final long serialVersionUID = 1L;

	public SupplementButton(String id, ICourse course, SupplementModul modul) {
		super(id, course, modul);
		
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(isAlreadySelected()){
			setSelected();
		}
	}
}
