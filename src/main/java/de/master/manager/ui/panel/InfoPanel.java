package de.master.manager.ui.panel;

import java.util.List;
import java.util.function.Function;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import com.google.common.collect.Lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.master.manager.profStuff.BasicModul;
import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementModul;
import de.master.manager.ui.events.AbstractEvent;
import de.master.manager.ui.events.PanelChangedEvent;
import de.master.manager.ui.events.RemoveCourseEvent;
import de.master.manager.ui.events.SelectedEvent;
import de.master.manager.ui.model.SerializableFunction;



public class InfoPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private static final int MAX_POINTS = 23;
	private static final int MAX_PFLICHT_POINTS = 24;
	private static final int MAX_BASIC_POINTS = 32;
	private static final int MAX_SUPPLEMENT_POINTS = 8;
	
	private final IModel<Integer> wahlPoints = Model.of(0);
	private final IModel<Integer> pflichtPoints = Model.of(0);
	private final IModel<Integer> supplementPoints = Model.of(0);
	private final IModel<Integer> basicPoints = Model.of(0);
	
	private final IModel<List<Prof>> profs;
	private final Form<Object> form;
	
	private final BasicModul basicModul;
	private final SupplementModul supplementModul;

	private final IModel<List<ICourse>> allCurrentSelectedModuls;
	private final IModel<List<ICourse>> allSelectedPflichtModuls;
	private final IModel<List<ICourse>> allSelectedWahlModuls;
	private final IModel<List<ICourse>> allSelectedSupplementModuls;
	private final IModel<List<ICourse>> allSelectedBasicModuls;
	
	
	public InfoPanel(String id, final IModel<List<Prof>> profs, BasicModul basicModul, SupplementModul supplementModul, final List<Prof> allProfs) {
		super(id);
		setOutputMarkupId(true);
		this.profs = profs;
		this.basicModul = basicModul;
		this.supplementModul = supplementModul;
				
		
		ProgressBar wahlProgressBar = createProgressBar("wahlProgress", wahlPoints);
		ProgressBar pflichtProgressBar = createProgressBar("pflichtProgress", pflichtPoints);
		ProgressBar aufbauProgressBar = createProgressBar("aufbauProgress", basicPoints);
		ProgressBar supplementProgressBar = createProgressBar("supplementProgress", supplementPoints);
		
		allSelectedPflichtModuls = createLoadableModel(allProfs, Prof::getSelectedPflichtCourses);
		allSelectedWahlModuls = createLoadableModel(allProfs, Prof::getSelectedCourses);
		allSelectedBasicModuls = createLoadableModel(basicModul.getCourses());
		allSelectedSupplementModuls = createLoadableModel(supplementModul.getCourses());
		allCurrentSelectedModuls = createLoadableModel(profs, basicModul, supplementModul);
		
        ListView<ICourse> modulPflichtListView = createListView("verticalPflichtButtonGroup","selectedPflichtModulButton",allSelectedPflichtModuls, allCurrentSelectedModuls, profs, basicModul, supplementModul, allProfs);
		ListView<ICourse> modulWahlListView = createListView("verticalWahlButtonGroup","selectedWahlModulButton",allSelectedWahlModuls, allCurrentSelectedModuls, profs, basicModul, supplementModul, allProfs);
		ListView<ICourse> modulSupplementListView = createListView("verticalSupplementButtonGroup","selectedSupplementModulButton",allSelectedSupplementModuls, allCurrentSelectedModuls, profs, basicModul, supplementModul, allProfs);
		ListView<ICourse> modulBasicListView = createListView("verticalBasicButtonGroup","selectedBasicModulButton",allSelectedBasicModuls, allCurrentSelectedModuls, profs, basicModul, supplementModul, allProfs);
		
		form = new Form<Object>("form");
		form.add(modulPflichtListView, modulWahlListView, modulSupplementListView, modulBasicListView);
        form.add(wahlProgressBar, pflichtProgressBar, aufbauProgressBar, supplementProgressBar);
		add(form);
			
	}

	/* methods */
	private LoadableDetachableModel<List<ICourse>> createLoadableModel(final IModel<List<Prof>> profs,
			BasicModul basicModul, SupplementModul supplementModul) {
		return new LoadableDetachableModel<List<ICourse>>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				profs.getObject().stream().forEach(m -> list.addAll(m.getSelectedCourses()));
				profs.getObject().stream().forEach(m -> list.addAll(m.getSelectedPflichtCourses()));
				list.addAll(supplementModul.getCourses());
				list.addAll(basicModul.getCourses());
				
				list.sort((c1,c2)->c1.getName().compareTo(c2.getName()));
				return list;
			}
			
		};
	}

	private LoadableDetachableModel<List<ICourse>> createLoadableModel(List<ICourse> coursese) {
		return new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				coursese.sort((c1,c2)->c1.getName().compareTo(c2.getName()));
				return coursese;
			}
		};
	}

	private IModel<List<ICourse>> createLoadableModel(final List<Prof> allProfs, SerializableFunction<Prof, List<ICourse>> loadSelectedCourses) {
		 return new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				allProfs.stream().forEach(p->list.addAll(loadSelectedCourses.apply(p)));
				list.sort((c1,c2)->c1.getName().compareTo(c2.getName()));
				return list;
			}
		};
	}

	private static int calculatePoints(IModel<List<Prof>> profs, Function<Prof, Double> calculate){
		return (int)Math.round(profs.getObject().stream().mapToDouble(p -> calculate.apply(p)).sum());
	}
	
	private static int calculatePoints(double points){
		return (int)Math.round(points);
	}
	
	
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		int summary = calculatePoints(profs, Prof::calculateWahlPoints);
		int summaryPflicht = calculatePoints(profs, Prof::calculatePflichtPoints);
		int summarySupplement = calculatePoints(supplementModul.calculatePoints());
		int summaryBasic = calculatePoints(basicModul.calculatePoints());
		if(event.getPayload() instanceof SelectedEvent){
			setAllPoints(summary, summaryPflicht, summarySupplement, summaryBasic);
			((SelectedEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof AbstractEvent){
			setAllPoints(summary, summaryPflicht, summarySupplement, summaryBasic);
			((AbstractEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof RemoveCourseEvent){
			setAllPoints(summary, summaryPflicht, summarySupplement, summaryBasic);
			((RemoveCourseEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof PanelChangedEvent){
			setAllPoints(summary, summaryPflicht, summarySupplement, summaryBasic);
			((PanelChangedEvent) event.getPayload()).getTarget().add(form);
		}
		
	}

	private void setAllPoints(int summary, int summaryPflicht, int summarySupplement, int summaryBasic) {
		setPoints(summary, wahlPoints, MAX_POINTS);
		setPoints(summaryPflicht, pflichtPoints, MAX_PFLICHT_POINTS);
		setPoints(summarySupplement, supplementPoints,MAX_SUPPLEMENT_POINTS);
		setPoints(summaryBasic, basicPoints,MAX_BASIC_POINTS);
	}
	
	private static void setPoints(int summary, IModel<Integer> points, int maxPoints){
		if(summary < maxPoints){
			points.setObject((int) Math.round(summary*(100/maxPoints)));
		} else {
			points.setObject(100);
		}
	}

	private static ProgressBar createProgressBar(String id, IModel<Integer> points){
		ProgressBar progressBar = new ProgressBar(id, points, ProgressBar.Type.SUCCESS); 
		progressBar.setOutputMarkupId(true);
		return progressBar;
	}
	
	private static ListView<ICourse> createListView(String ListViewID, String ButtonID, IModel<List<ICourse>> allSelectedCourses, final IModel<List<ICourse>> allCurrentSelectedModuls, IModel<List<Prof>> profs, BasicModul basicModul, SupplementModul supplementModul, final List<Prof> allProfs){
		return new ListView<ICourse>(ListViewID, allSelectedCourses) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				AjaxButton button = createButton(ButtonID, item.getModelObject());
				if(allCurrentSelectedModuls.getObject().contains(item.getModel().getObject())){
					button.add(new AttributeModifier("class", Model.of("btn btn-xs btn-success btn-block")));
				}
				button.add(new Label("label", item.getModelObject().getName()));
				item.add(button);
			}
			
			/* local private method*/
			private AjaxButton createButton(String buttonId, ICourse course){
				return new AjaxButton(buttonId) {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						allProfs.stream().forEach(prof -> prof.getSelectedCourses().remove(course));
						allProfs.stream().forEach(prof -> prof.getSelectedPflichtCourses().remove(course));
						supplementModul.removeCourse(course);
						basicModul.removeCourse(course);
						send(getPage(), Broadcast.DEPTH, new RemoveCourseEvent(target));
					};
				};
			}
			
		};
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		allCurrentSelectedModuls.detach();
		allSelectedPflichtModuls.detach();
		allSelectedWahlModuls.detach();
		allSelectedSupplementModuls.detach();
		allSelectedBasicModuls.detach();
		
		profs.detach();
	}
}
