package de.master.manager.ui.button;

import de.master.manager.profStuff.BasicModul;
import de.master.manager.profStuff.ICourse;

public class AufbauButton extends AbstractCourseButton{
	private static final long serialVersionUID = 1L;

	public AufbauButton(String id, ICourse course, BasicModul basicModul) {
		super(id, course, basicModul);
		
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(isAlreadySelected()){
			setSelected();
		}
	}
}
