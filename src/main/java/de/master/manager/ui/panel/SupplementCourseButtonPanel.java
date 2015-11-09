package de.master.manager.ui.panel;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import de.master.manager.profStuff.ICourse;

public class SupplementCourseButtonPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public SupplementCourseButtonPanel(String id, List<ICourse> list) {
		super(id);
		
		Form form = new Form("form");
		ListView<ICourse> buttonListView = new ListView<ICourse>("buttonListView", list) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse currentCourse=item.getModelObject();
				item.add(new Button("modulButton", Model.of(item.getModelObject().getName())));
				String points=String.valueOf(currentCourse.getPoints());
				item.add(new Button("modulPoints", new Model<String>(points)));
			}
		};
		form.add(buttonListView);
		add(form);
	}

	

}
