package de.master.manager.noten;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Lists;

import de.master.manager.events.ProfChangedEvent;
import de.master.manager.model.TransformationModel;
import de.master.manager.myproject.CoursePanel;
import de.master.manager.myproject.NotePanel;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.WahlPflichtModuleLoader;

public class NavsPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private Panel currentPanel;
	
	public NavsPanel(String id, WahlPflichtModuleLoader courseLoader, Prof breunig, Prof hinz, Prof heck, Prof hennes) throws IOException {
		super(id);
		List<Prof> allProfs = Lists.newArrayList(breunig, hinz, heck, hennes);
		MarkupContainer container = new WebMarkupContainer("container");
		IModel<Prof> profOfPanel1 = Model.of(breunig);
		IModel<Prof> profOfPanel2 = Model.of(hinz);
		
		IModel<List<Prof>> dropDownList1 = new TransformationModel<Prof, List<Prof>>(profOfPanel1, prof -> allProfs.stream().filter(p -> !p.equals(prof)).collect(Collectors.toList()));
		container.add(createDropDown("dropDown1", profOfPanel1, dropDownList1));
		
		IModel<List<Prof>> dropDownList2 = new TransformationModel<Prof, List<Prof>>(profOfPanel2, prof -> allProfs.stream().filter(p -> !p.equals(prof)).collect(Collectors.toList()));
		container.add(createDropDown("dropDown2", profOfPanel2, dropDownList2));
		add(container);
		
		currentPanel = new CoursePanel("panel", courseLoader, breunig, hinz, heck, hennes);
		currentPanel.setOutputMarkupPlaceholderTag(true);
		AjaxLink<?> cousePanelLink = createLink("courses",  new CoursePanel("panel", courseLoader, breunig, hinz, heck, hennes));
		AjaxLink<?> notenLink = createLink("noten",  new NotePanel("panel"));
				
		add(cousePanelLink);
		add(notenLink);
		add(currentPanel);
	}
	
	private AjaxLink<?> createLink(String id, Panel newPanel){
		return new AjaxLink<Object>(id) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				newPanel.setOutputMarkupId(true);
				currentPanel.replaceWith(newPanel);
				currentPanel = newPanel;
				target.add(currentPanel);
			}
		};
	}
	
	private static DropDownChoice<Prof> createDropDown(String id, final IModel<Prof> selectedProf, IModel<List<Prof>> dropDowns) {
		final DropDownChoice<Prof> dropDown = new DropDownChoice<Prof>(id, selectedProf, dropDowns);
		dropDown.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				dropDown.send(dropDown.getPage(), Broadcast.DEPTH, new ProfChangedEvent(target));
			}
		});

		return dropDown;
	}
}
