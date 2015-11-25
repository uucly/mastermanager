package de.master.manager.ui.panel;

import java.util.ArrayList;
import java.util.List;

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
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Lists;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.IModulLoader;
import de.master.manager.profStuff.Modul;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.PanelChangedEvent;
import de.master.manager.ui.events.ProfChangedEvent;

public class NavsPanel extends Panel{

	private static final long serialVersionUID = 1L;
	
	private Panel currentPanel;
	private MarkupContainer profDropDownContainer;
	
	/* constructor */
	public NavsPanel(String id, IModulLoader courseLoader, Prof breunig, Prof hinz, Prof heck, Prof hennes){
		super(id);
		List<Prof> allProfs = Lists.newArrayList(breunig, hinz, heck, hennes);
		IModel<Prof> profOfPanel1 = Model.of(breunig);
		IModel<Prof> profOfPanel2 = Model.of(hinz);
		
		IModul basicModul = new Modul(new ArrayList<ICourse>());
		IModul supplementModul = new Modul(new ArrayList<ICourse>());
		
		currentPanel = new AufbauPanel("panel", courseLoader, profOfPanel1, profOfPanel2, basicModul, supplementModul, allProfs);
		currentPanel.setOutputMarkupPlaceholderTag(true);
		
		add(profDropDownContainer = createProfDropDownContainer(allProfs, profOfPanel1, profOfPanel2));
		add(createLink("aufbau", currentPanel));
		add(createLink("noten",  new GradePanel("panel", profOfPanel1, profOfPanel2, basicModul, supplementModul)));
		add(createLink("supplement", new SupplementPanel("panel", courseLoader, profOfPanel1, profOfPanel2, basicModul, supplementModul, allProfs)));
		add(createLink("courses", new CoursePanel("panel", courseLoader, profOfPanel1, profOfPanel2, basicModul, supplementModul, allProfs)));
		add(currentPanel);
	}

	/* methods */
	private static MarkupContainer createProfDropDownContainer(List<Prof> allProfs, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2) {
		WebMarkupContainer profDropDownContainer = new WebMarkupContainer("container");
		profDropDownContainer.setOutputMarkupId(true);
		
		IModel<List<Prof>> dropDownList1 = filterProfs(profOfPanel2, allProfs);
		profDropDownContainer.add(createDropDown("dropDown1", profOfPanel1, dropDownList1));
		
		IModel<List<Prof>> dropDownList2 = filterProfs(profOfPanel1, allProfs);
		profDropDownContainer.add(createDropDown("dropDown2", profOfPanel2, dropDownList2));
		return profDropDownContainer;
	}
	
	private static IModel<List<Prof>> filterProfs(final IModel<Prof> prof, final List<Prof> profs){
		return new LoadableDetachableModel<List<Prof>>() {

			@Override
			protected List<Prof> load() {
				List<Prof> filteredProfs = new ArrayList<>(profs.size());
				for(Prof p : profs){
					if(!p.equals(prof.getObject())){
						filteredProfs.add(p);
					}
				}
				return filteredProfs;
			}
		};
	}
	
	private AjaxLink<?> createLink(String id, final Panel newPanel){
		return new AjaxLink<Object>(id) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				newPanel.setOutputMarkupId(true);
				currentPanel.replaceWith(newPanel);
				currentPanel = newPanel;
				target.add(currentPanel);
				send(getPage(), Broadcast.DEPTH, new PanelChangedEvent(target));
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
