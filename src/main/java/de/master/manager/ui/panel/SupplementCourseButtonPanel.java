package de.master.manager.ui.panel;

import java.util.List;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.IModul;
import de.master.manager.ui.button.SupplementButton;
import de.master.manager.ui.events.RemoveCourseEvent;

public class SupplementCourseButtonPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private final Form form;

	public SupplementCourseButtonPanel(String id, IModul modul, List<ICourse> list) {
		super(id);
		
		form = new Form("form");
		ListView<ICourse> buttonListView = new ListView<ICourse>("buttonListView", list) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse currentCourse=item.getModelObject();
				item.add(new SupplementButton("modulButton", item.getModelObject(), modul));
				String points=String.valueOf(currentCourse.getPoints());
				item.add(new Button("modulPoints", new Model<String>(points)));
			}
		};
		form.add(buttonListView);
		add(form);
	}

	@Override
	public void onEvent(IEvent<?> event) {
		Object payload = event.getPayload();
		if(payload instanceof RemoveCourseEvent){
			((RemoveCourseEvent) payload).getTarget().add(form);
		}
	}
	
	
}
