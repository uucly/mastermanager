package de.master.manager.ui.button;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.IModul;

public class AufbauButton extends AbstractCourseButton{
	private static final long serialVersionUID = 1L;

	public AufbauButton(String id, ICourse course, IModul basicModul) {
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
