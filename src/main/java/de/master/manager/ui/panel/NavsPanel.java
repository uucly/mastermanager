package de.master.manager.ui.panel;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Lists;

import de.master.manager.events.ProfChangedEvent;
import de.master.manager.model.TransformationModel;
import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.Prof;

public class NavsPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private Panel currentPanel;
	private MarkupContainer profDropDownContainer;
	
	/* constructor */
	public NavsPanel(String id, ICourseLoader courseLoader, Prof breunig, Prof hinz, Prof heck, Prof hennes){
		super(id);
		List<Prof> allProfs = Lists.newArrayList(breunig, hinz, heck, hennes);
		IModel<Prof> profOfPanel1 = Model.of(breunig);
		IModel<Prof> profOfPanel2 = Model.of(hinz);
		
		currentPanel = new CoursePanel("panel", courseLoader, profOfPanel1, profOfPanel2, allProfs);
		currentPanel.setOutputMarkupPlaceholderTag(true);
		
		add(profDropDownContainer = createProfDropDownContainer(allProfs, profOfPanel1, profOfPanel2));
		add(createLink("courses",  currentPanel));
		add(createLink("noten",  new NotePanel("panel", profOfPanel1, profOfPanel2)));
		add(currentPanel);
	}

	/* methods */
	
	private static MarkupContainer createProfDropDownContainer(List<Prof> allProfs, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2) {
		WebMarkupContainer profDropDownContainer = new WebMarkupContainer("container");
		profDropDownContainer.setOutputMarkupId(true);
		
		IModel<List<Prof>> dropDownList1 = new TransformationModel<Prof, List<Prof>>(profOfPanel2, prof -> allProfs.stream().filter(p -> !p.equals(prof)).collect(Collectors.toList()));
		profDropDownContainer.add(createDropDown("dropDown1", profOfPanel1, dropDownList1));
		
		IModel<List<Prof>> dropDownList2 = new TransformationModel<Prof, List<Prof>>(profOfPanel1, prof -> allProfs.stream().filter(p -> !p.equals(prof)).collect(Collectors.toList()));
		profDropDownContainer.add(createDropDown("dropDown2", profOfPanel2, dropDownList2));
		return profDropDownContainer;
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

	
	/* event */
	
	@Override
	public void onEvent(IEvent<?> event) {
		Object payload = event.getPayload();
		if(payload instanceof ProfChangedEvent){
			((ProfChangedEvent) payload).getTarget().add(profDropDownContainer);
		}
	}
}