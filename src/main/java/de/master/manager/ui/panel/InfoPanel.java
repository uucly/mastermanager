package de.master.manager.ui.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
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
import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementCourses;
import de.master.manager.ui.events.AbstractEvent;
import de.master.manager.ui.events.PanelChangedEvent;
import de.master.manager.ui.events.RemoveCourseEvent;
import de.master.manager.ui.events.SelectedEvent;
import de.master.manager.ui.model.TransformationModel;



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
	private final IModel<List<ICourse>> allCurrentSelectedModuls;
	private final IModel<Map<ICourse, String>> allSelectedPflichtModuls;
	private final IModel<List<ICourse>> allSelectedWahlModuls;
	private final IModel<List<ICourse>> allSelectedSupplementModuls;
	private final IModel<List<ICourse>> allSelectedBasicModuls;
	private final IModel<List<ICourse>> loadSupplementCourses;
	private final IModel<List<ICourse>> loadBasicCourses;
	
	
	public InfoPanel(String id, final IModel<List<Prof>> profs,  final List<Prof> allProfs) {
		super(id);
		setOutputMarkupId(true);
		this.profs = profs;
		
		loadSupplementCourses = new TransformationModel<List<Prof>, List<ICourse>>(profs, list -> {
			Optional<Prof> optionalProf = list.stream().findFirst();
			return optionalProf.isPresent() ? optionalProf.get().getSelectedSupplementCourses().getAllCourses() : Collections.emptyList();
		});
		
		loadBasicCourses = new TransformationModel<List<Prof>, List<ICourse>>(profs, list -> {
			Optional<Prof> optionalProf = list.stream().findFirst();
			return optionalProf.isPresent() ? optionalProf.get().getSelectedBasicCourses().getCourses() : Collections.emptyList();
		});

		form = new Form<Object>("form");
		wahlPoints.setObject(calculatePoints(profs, Prof::calculateWahlPoints));
		pflichtPoints.setObject(calculatePoints(profs, Prof::calculatePflichtPoints));
		
		supplementPoints.setObject((int)Math.round(loadSupplementPoints(profs)));
		
		ProgressBar wahlProgressBar = createProgressBar("wahlProgress", wahlPoints);
		ProgressBar pflichtProgressBar = createProgressBar("pflichtProgress", pflichtPoints);
		ProgressBar aufbauProgressBar = createProgressBar("aufbauProgress", basicPoints);
		ProgressBar supplementProgressBar = createProgressBar("supplementProgress", supplementPoints);
		
		/*allSelectedModuls = new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				allProfs.stream().forEach(p->list.addAll(p.getSelectedWahlModuls()));
				allProfs.stream().forEach(p->list.addAll(p.getSelectedPflichtModuls()));
				list.addAll(loadSupplementCourses.getObject());
				list.addAll(loadBasicCourses.getObject());
				
				return list;
			}
		};*/
		
		allSelectedPflichtModuls = new LoadableDetachableModel<Map<ICourse, String>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected Map<ICourse, String> load() {
				
				Map<ICourse,String> map = new TreeMap<>();
				allProfs.stream().forEach(p->{
					p.getSelectedPflichtModuls().stream().forEach(c -> map.put(c, p.getName()));
				});
				
				//list.sort((c1,c2)->c1.getName().compareTo(c2.getName()));
				return map;
			}
		};
		
		allSelectedWahlModuls = new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				allProfs.stream().forEach(p->list.addAll(p.getSelectedWahlModuls()));

				list.sort((c1,c2)->c1.getName().compareTo(c2.getName()));
				return list;
			}
		};
		
		allSelectedBasicModuls = new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				list.addAll(loadBasicCourses.getObject());

				list.sort((c1,c2)->c1.getName().compareTo(c2.getName()));
				return list;
			}
		};
		
		allSelectedSupplementModuls = new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				list.addAll(loadSupplementCourses.getObject());
				list.sort((c1,c2)->c1.getName().compareTo(c2.getName()));
				return list;
			}
		};
		
		
		allCurrentSelectedModuls = new LoadableDetachableModel<List<ICourse>>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				profs.getObject().stream().forEach(m -> list.addAll(m.getSelectedWahlModuls()));
				profs.getObject().stream().forEach(m -> list.addAll(m.getSelectedPflichtModuls()));
				list.addAll(loadSupplementCourses.getObject());
				list.addAll(loadBasicCourses.getObject());

				list.sort((c1,c2)->c1.getName().compareTo(c2.getName()));
				return list;
			}
			
		};
		
		
        //ListView<ICourse> modulListView = createListView(allSelectedModuls, allCurrentSelectedModuls, profs, allProfs);
		ListView<ICourse> modulPflichtListView = createListView("verticalPflichtButtonGroup","selectedPflichtModulButton",allSelectedPflichtModuls, allCurrentSelectedModuls, profs, allProfs);
		ListView<ICourse> modulWahlListView = createListView("verticalWahlButtonGroup","selectedWahlModulButton",allSelectedWahlModuls, allCurrentSelectedModuls, profs, allProfs);
		ListView<ICourse> modulSupplementListView = createListView("verticalSupplementButtonGroup","selectedSupplementModulButton",allSelectedSupplementModuls, allCurrentSelectedModuls, profs, allProfs);
		ListView<ICourse> modulBasicListView = createListView("verticalBasicButtonGroup","selectedBasicModulButton",allSelectedBasicModuls, allCurrentSelectedModuls, profs, allProfs);
		form.add(modulPflichtListView, modulWahlListView, modulSupplementListView, modulBasicListView);
        form.add(wahlProgressBar);
        form.add(pflichtProgressBar);
        form.add(aufbauProgressBar);
        form.add(supplementProgressBar);
		add(form);
			
	}

	private double loadSupplementPoints(final IModel<List<Prof>> profs) {
		Optional<Double> optionalSupplementPoints = profs.getObject().stream().findFirst().map(p -> p.calculateSupplementPoints());
		double points = optionalSupplementPoints.isPresent() ? optionalSupplementPoints.get() : 0;
		return points;
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
		int summarySupplement = calculatePoints(loadSupplementCourses.getObject().stream().mapToDouble(ICourse::getPoints).sum());
		int summaryBasic = calculatePoints(loadBasicCourses.getObject().stream().mapToDouble(ICourse::getPoints).sum());
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
	
	private static ListView<ICourse> createListView(String ListViewID, String ButtonID, IModel<List<ICourse>> allSelectedCourses, final IModel<List<ICourse>> allCurrentSelectedModuls, IModel<List<Prof>> profs, final List<Prof> allProfs){
		return new ListView<ICourse>(ListViewID, allSelectedCourses) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				AjaxButton button = createButton(item.getModelObject().getName()+"("++")");
				if(allCurrentSelectedModuls.getObject().contains(item.getModel().getObject())){
					button.add(new AttributeModifier("class", Model.of("btn btn-xs btn-success btn-block")));
				}
				button.add(new Label("label", item.getModelObject().getName()));
				item.add(button);
			}
			
			/* local private method*/
			private AjaxButton createButton(String modulName){
				return new AjaxButton(ButtonID) {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						allProfs.stream().forEach(prof -> prof.getSelectedWahlModuls().removeIf(m -> m.getName().equals(modulName)));
						allProfs.stream().forEach(prof -> prof.getSelectedPflichtModuls().removeIf(m -> m.getName().equals(modulName)));
						allProfs.stream().findFirst().ifPresent(p -> p.getSelectedSupplementCourses().getAllCourses().removeIf(c -> c.getName().equals(modulName)));
						allProfs.stream().findFirst().ifPresent(p -> p.getSelectedBasicCourses().getCourses().removeIf(c -> c.getName().equals(modulName)));
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
