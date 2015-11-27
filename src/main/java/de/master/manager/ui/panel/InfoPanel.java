package de.master.manager.ui.panel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.AbstractEvent;
import de.master.manager.ui.events.PanelChangedEvent;
import de.master.manager.ui.events.RemoveCourseEvent;
import de.master.manager.ui.events.SelectedEvent;
import de.master.manager.ui.model.SerializableFunction;


/**
 * 
 * @author je-ke
 *
 */
public class InfoPanel extends Panel{

	private static final int MODUL_POINTS_TO_REECH = 23;
	private static final long serialVersionUID = 1L;
	private IModel<Integer> maxWahlPointsProf1 = Model.of(0);
	private IModel<Integer> maxWahlPointsProf2 = Model.of(0);
	private IModel<Integer> maxPflichtPoints = Model.of(0);
	private static final int MAX_BASIC_POINTS = 32;
	private static final int MAX_SUPPLEMENT_POINTS = 8;
	
	private final IModel<Integer> wahlPoints1 = Model.of(0);
	private final IModel<Integer> wahlPoints2 = Model.of(0);
	private final IModel<Integer> pflichtPoints = Model.of(0);
	private final IModel<Integer> supplementPoints = Model.of(0);
	private final IModel<Integer> basicPoints = Model.of(0);
	
	private final IModel<Prof> prof1;
	private final IModel<Prof> prof2;
	
	private final Form<Object> form;
	
	private final IModul basicModul;
	private final IModul supplementModul;

	private final IModel<List<ICourse>> allCurrentSelectedModuls;
	private final IModel<List<ICourse>> allSelectedPflichtModuls;
	private final IModel<List<ICourse>> allSelectedWahlModuls;
	private final IModel<List<ICourse>> allSelectedSupplementModuls;
	private final IModel<List<ICourse>> allSelectedBasicModuls;
	private final IModel<String> loadPointInfoBasicForLabel;
	private final IModel<String> loadPointInfoPflichtForLabel;
	private final IModel<String> loadPointInfoWahlForLabel1;
	private final IModel<String> loadPointInfoWahlForLabel2;
	private final IModel<String> loadPointInfoSupplementForLabel;
	
	
	public InfoPanel(String id, final IModel<Prof> prof1, final IModel<Prof> prof2, IModul basicModul, IModul supplementModul, final List<Prof> allProfs) {
		super(id);
		setOutputMarkupId(true);
		this.prof1 = prof1;
		this.prof2 = prof2;
		this.basicModul = basicModul;
		this.supplementModul = supplementModul;
		
		final IModel<List<Prof>> loadSelectedProfs = new LoadableDetachableModel<List<Prof>>() {

			@Override
			protected List<Prof> load() {
				return Lists.newArrayList(prof1.getObject(), prof2.getObject());
			}
		};
        
		maxPflichtPoints = new LoadableDetachableModel<Integer>() {

			@Override
			protected Integer load() {
				int pflichtPointsToReach = 0;
				for(Prof p : loadSelectedProfs.getObject()){
					pflichtPointsToReach += p.calculatePflichtPointsToReach();
				}
				return pflichtPointsToReach;
			}
		};
		maxWahlPointsProf1 = new LoadableDetachableModel<Integer>() {

			@Override
			protected Integer load() {
				return (int)(MODUL_POINTS_TO_REECH - prof1.getObject().calculatePflichtPointsToReach());
			}
		};
		maxWahlPointsProf2 = new LoadableDetachableModel<Integer>() {

			@Override
			protected Integer load() {
				return (int)(MODUL_POINTS_TO_REECH - prof2.getObject().calculatePflichtPointsToReach());
			}
		};
		ProgressBar wahlProgressBar1 = createProgressBar("wahlProgress1", wahlPoints1);
		ProgressBar wahlProgressBar2 = createProgressBar("wahlProgress2", wahlPoints2);
		ProgressBar pflichtProgressBar = createProgressBar("pflichtProgress", pflichtPoints);
		ProgressBar aufbauProgressBar = createProgressBar("aufbauProgress", basicPoints);
		ProgressBar supplementProgressBar = createProgressBar("supplementProgress", supplementPoints);
		
		allSelectedPflichtModuls = createLoadableModel(allProfs, new SerializableFunction<Prof, List<ICourse>>(){

			@Override
			public List<ICourse> apply(Prof prof) {
				return prof.getPflichtModulSelected();
			}
			
		});
		allSelectedWahlModuls = createLoadableModel(allProfs, new SerializableFunction<Prof, List<ICourse>>(){

			@Override
			public List<ICourse> apply(Prof prof) {
				return prof.getWahlModulSelected();
			}
			
		});
		allSelectedBasicModuls = createLoadableModel(basicModul);
		allSelectedSupplementModuls = createLoadableModel(supplementModul);
		allCurrentSelectedModuls = createLoadableModel(loadSelectedProfs, basicModul, supplementModul);
		
		ListView<ICourse> modulPflichtListView = createListView("verticalPflichtButtonGroup","selectedPflichtModulButton",allSelectedPflichtModuls, allCurrentSelectedModuls, loadSelectedProfs, basicModul, supplementModul, allProfs);
		ListView<ICourse> modulWahlListView = createListView("verticalWahlButtonGroup","selectedWahlModulButton",allSelectedWahlModuls, allCurrentSelectedModuls, loadSelectedProfs, basicModul, supplementModul, allProfs);
		ListView<ICourse> modulSupplementListView = createListView("verticalSupplementButtonGroup","selectedSupplementModulButton",allSelectedSupplementModuls, allCurrentSelectedModuls, loadSelectedProfs, basicModul, supplementModul, allProfs);
		ListView<ICourse> modulBasicListView = createListView("verticalBasicButtonGroup","selectedBasicModulButton",allSelectedBasicModuls, allCurrentSelectedModuls, loadSelectedProfs, basicModul, supplementModul, allProfs);
		
		loadPointInfoBasicForLabel = createLoadableModel(basicModul,  MAX_BASIC_POINTS);
		loadPointInfoPflichtForLabel = createLoadableModel(new SerializableFunction<Prof, Double>() {

			@Override
			public Double apply(Prof prof) {
				return prof.calculatePflichtPoints();
			}
		}, maxPflichtPoints, prof1, prof2);
		loadPointInfoWahlForLabel1 = createLoadableModel(new SerializableFunction<Prof, Double>() {

			@Override
			public Double apply(Prof prof) {
				return prof.calculateWahlPoints();
			}
		}, maxWahlPointsProf1, prof1);
		loadPointInfoWahlForLabel2 = createLoadableModel(new SerializableFunction<Prof, Double>() {

			@Override
			public Double apply(Prof prof) {
				return prof.calculateWahlPoints();
			}
		}, maxWahlPointsProf2, prof2);
		loadPointInfoSupplementForLabel = createLoadableModel(supplementModul, MAX_SUPPLEMENT_POINTS);
		
		
		Label pointBasicInfo = new Label("pointBasicInfo", loadPointInfoBasicForLabel);
		Label pointPflichtInfo = new Label("pointPflichtInfo", loadPointInfoPflichtForLabel);
		Label pointWahlInfo1 = new Label("pointWahlInfo1", loadPointInfoWahlForLabel1);
		Label pointWahlInfo2 = new Label("pointWahlInfo2", loadPointInfoWahlForLabel2);
		
		Label pointSupplementInfo = new Label("pointSupplementInfo", loadPointInfoSupplementForLabel);
		
		form = new Form<Object>("form");
		form.add(new Label("prof1", prof1));
		form.add(new Label("prof2", prof2));
		form.add(modulPflichtListView, modulWahlListView, modulSupplementListView, modulBasicListView);
        form.add(wahlProgressBar1, wahlProgressBar2, pflichtProgressBar, aufbauProgressBar, supplementProgressBar);
        form.add(pointBasicInfo, pointPflichtInfo, pointWahlInfo1, pointWahlInfo2, pointSupplementInfo);
		add(form);
			
	}

	private static IModel<String> createLoadableModel(final IModul modul, final int maxPoints) {
		return new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return modul.calculatePoints() + " von " + maxPoints;
			}
		};
	}

	@SafeVarargs
	private static IModel<String> createLoadableModel(final SerializableFunction<Prof, Double> calculate, final IModel<Integer> maxPoints, final IModel<Prof>...profs) {
		return new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				double sum = 0;
				for(IModel<Prof> p : Arrays.asList(profs)){
					sum += calculate.apply(p.getObject());
				}
				return sum + " von " + maxPoints.getObject();
			}
		};
	}

	/* methods */
	private IModel<List<ICourse>> createLoadableModel(final IModel<List<Prof>> profs, final IModul basicModul, final IModul supplementModul) {
		return new LoadableDetachableModel<List<ICourse>>() {

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				for(Prof p : profs.getObject()){
					list.addAll(p.getWahlModulSelected());
					list.addAll(p.getPflichtModulSelected());
					list.addAll(supplementModul);
					list.addAll(basicModul);
				}
				Collections.sort(list,createComparator());
				return list;}
		};
	}

	private LoadableDetachableModel<List<ICourse>> createLoadableModel(final List<ICourse> coursese) {
		return new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				Collections.sort(coursese,createComparator());
				return coursese;
			}
		};
	}

	private IModel<List<ICourse>> createLoadableModel(final List<Prof> allProfs, final SerializableFunction<Prof, List<ICourse>> loadSelectedCourses) {
		 return new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				for(Prof p : allProfs){
					list.addAll(loadSelectedCourses.apply(p));
				}
				Collections.sort(list,createComparator());
				
				return list;
			}
		};
	}

	@SafeVarargs
	private static int calculatePoints(final SerializableFunction<Prof, Double> calculate, IModel<Prof>... profs){
		double sum = 0;
		for(IModel<Prof> m : Arrays.asList(profs)){
			sum += calculate.apply(m.getObject());
		}
		return (int)Math.round(sum);
	}
	
	private static int calculatePoints(double points){
		return (int)Math.round(points);
	}
	
	
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		int summaryWahl1 = calculatePoints(prof1.getObject().calculateWahlPoints());
		int summaryWahl2 = calculatePoints(prof2.getObject().calculateWahlPoints());
		
		int summaryPflicht = calculatePoints(new SerializableFunction<Prof, Double>() {

			@Override
			public Double apply(Prof prof) {
				return prof.calculatePflichtPoints();
			}
		}, prof1, prof2);
		int summarySupplement = calculatePoints(supplementModul.calculatePoints());
		int summaryBasic = calculatePoints(basicModul.calculatePoints());
		
		
		
		if(event.getPayload() instanceof SelectedEvent){
			setAllPoints(summaryWahl1, summaryWahl2, summaryPflicht, summarySupplement, summaryBasic);
			((SelectedEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof AbstractEvent){
			setAllPoints(summaryWahl1, summaryWahl2, summaryPflicht, summarySupplement, summaryBasic);
			((AbstractEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof RemoveCourseEvent){
			setAllPoints(summaryWahl1, summaryWahl2, summaryPflicht, summarySupplement, summaryBasic);
			((RemoveCourseEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof PanelChangedEvent){
			setAllPoints(summaryWahl1, summaryWahl2, summaryPflicht, summarySupplement, summaryBasic);
			((PanelChangedEvent) event.getPayload()).getTarget().add(form);
		}
		
	}

	private void setAllPoints(int summaryWahl1, int summaryWahl2, int summaryPflicht, int summarySupplement, int summaryBasic) {
		setPoints(summaryWahl1, wahlPoints1, maxWahlPointsProf1.getObject());
		setPoints(summaryWahl2, wahlPoints2, maxWahlPointsProf2.getObject());
		setPoints(summaryPflicht, pflichtPoints, maxPflichtPoints.getObject());
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
	
	private static ListView<ICourse> createListView(String ListViewID, final String ButtonID, IModel<List<ICourse>> allSelectedCourses, final IModel<List<ICourse>> allCurrentSelectedModuls, IModel<List<Prof>> profs, final IModul basicModul, final IModul supplementModul, final List<Prof> allProfs){
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
			private AjaxButton createButton(String buttonId, final ICourse course){
				return new AjaxButton(buttonId) {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						for(Prof p : allProfs){
							p.getWahlModulSelected().remove(course);
							p.getPflichtModulSelected().remove(course);
						}
						supplementModul.remove(course);
						basicModul.remove(course);
						send(getPage(), Broadcast.DEPTH, new RemoveCourseEvent(target));
					};
				};
			}
			
		};
	}
	
	private static Comparator<ICourse> createComparator(){
		return new Comparator<ICourse>() {

			@Override
			public int compare(ICourse o1, ICourse o2) {
				return o1.getName().compareTo(o2.getName());
			}

		};
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		prof1.detach();
		prof2.detach();
		maxPflichtPoints.detach();
		maxWahlPointsProf1.detach();
		maxWahlPointsProf2.detach();
		
		allCurrentSelectedModuls.detach();
		allSelectedPflichtModuls.detach();
		allSelectedWahlModuls.detach();
		allSelectedSupplementModuls.detach();
		allSelectedBasicModuls.detach();
		
		loadPointInfoBasicForLabel.detach();;
		loadPointInfoPflichtForLabel.detach();
		loadPointInfoWahlForLabel1.detach();
		loadPointInfoWahlForLabel2.detach();
		loadPointInfoSupplementForLabel.detach();
		
		pflichtPoints.detach();
		wahlPoints1.detach();
		wahlPoints2.detach();
		basicPoints.detach();
		supplementPoints.detach();
		
		
	}
}
