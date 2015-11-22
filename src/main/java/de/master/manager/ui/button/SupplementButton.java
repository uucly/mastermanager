package de.master.manager.ui.button;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.IModul;

public class SupplementButton extends AbstractCourseButton{

	private static final long serialVersionUID = 1L;

	public SupplementButton(String id, ICourse course, IModul modul) {
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
