package de.master.manager.ui.panel;

import java.util.ArrayList;
import java.util.Arrays;
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
import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementModul;
import de.master.manager.ui.events.AbstractEvent;
import de.master.manager.ui.events.PanelChangedEvent;
import de.master.manager.ui.events.RemoveCourseEvent;
import de.master.manager.ui.events.SelectedEvent;
import de.master.manager.ui.model.SerializableFunction;
import de.master.manager.ui.model.TransformationModel;
import de.master.manager.ui.model.TransformationModel2;


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
	
	private final BasicModul basicModul;
	private final SupplementModul supplementModul;

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
	
	
	public InfoPanel(String id, final IModel<Prof> prof1, final IModel<Prof> prof2, BasicModul basicModul, SupplementModul supplementModul, final List<Prof> allProfs) {
		super(id);
		setOutputMarkupId(true);
		this.prof1 = prof1;
		this.prof2 = prof2;
		this.basicModul = basicModul;
		this.supplementModul = supplementModul;
		
		IModel<List<Prof>> loadSelectedProfs = new TransformationModel2<>(prof1, prof2, Arrays::asList);
        
		maxPflichtPoints = new TransformationModel<>(loadSelectedProfs, l -> (int)l.stream().mapToDouble(p -> p.calculatePflichtPointsToReach()).sum());
		maxWahlPointsProf1 = new TransformationModel<>(prof1, p -> (int)(MODUL_POINTS_TO_REECH - p.calculatePflichtPointsToReach()));
		maxWahlPointsProf2 = new TransformationModel<>(prof2, p -> (int)(MODUL_POINTS_TO_REECH - p.calculatePflichtPointsToReach()));
			
		ProgressBar wahlProgressBar1 = createProgressBar("wahlProgress1", wahlPoints1);
		ProgressBar wahlProgressBar2 = createProgressBar("wahlProgress2", wahlPoints2);
		ProgressBar pflichtProgressBar = createProgressBar("pflichtProgress", pflichtPoints);
		ProgressBar aufbauProgressBar = createProgressBar("aufbauProgress", basicPoints);
		ProgressBar supplementProgressBar = createProgressBar("supplementProgress", supplementPoints);
		
		allSelectedPflichtModuls = createLoadableModel(allProfs, Prof::getSelectedPflichtCourses);
		allSelectedWahlModuls = createLoadableModel(allProfs, Prof::getSelectedCourses);
		allSelectedBasicModuls = createLoadableModel(basicModul);
		allSelectedSupplementModuls = createLoadableModel(supplementModul);
		allCurrentSelectedModuls = createLoadableModel(loadSelectedProfs, basicModul, supplementModul);
		
		ListView<ICourse> modulPflichtListView = createListView("verticalPflichtButtonGroup","selectedPflichtModulButton",allSelectedPflichtModuls, allCurrentSelectedModuls, loadSelectedProfs, basicModul, supplementModul, allProfs);
		ListView<ICourse> modulWahlListView = createListView("verticalWahlButtonGroup","selectedWahlModulButton",allSelectedWahlModuls, allCurrentSelectedModuls, loadSelectedProfs, basicModul, supplementModul, allProfs);
		ListView<ICourse> modulSupplementListView = createListView("verticalSupplementButtonGroup","selectedSupplementModulButton",allSelectedSupplementModuls, allCurrentSelectedModuls, loadSelectedProfs, basicModul, supplementModul, allProfs);
		ListView<ICourse> modulBasicListView = createListView("verticalBasicButtonGroup","selectedBasicModulButton",allSelectedBasicModuls, allCurrentSelectedModuls, loadSelectedProfs, basicModul, supplementModul, allProfs);
		
		loadPointInfoBasicForLabel = createLoadableModel(basicModul,  MAX_BASIC_POINTS);
		loadPointInfoPflichtForLabel = createLoadableModel(Prof::calculatePflichtPoints, maxPflichtPoints, prof1, prof2);
		loadPointInfoWahlForLabel1 = createLoadableModel(Prof::calculateWahlPoints, maxWahlPointsProf1, prof1);
		loadPointInfoWahlForLabel2 = createLoadableModel(Prof::calculateWahlPoints, maxWahlPointsProf2, prof2);
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

	private static IModel<String> createLoadableModel(IModul modul, int maxPoints) {
		return new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return modul.calculatePoints() + " von " + maxPoints;
			}
		};
	}

	@SafeVarargs
	private static IModel<String> createLoadableModel(SerializableFunction<Prof, Double> calculate, IModel<Integer> maxPoints, IModel<Prof>...profs) {
		return new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return Arrays.asList(profs).stream().mapToDouble(p -> calculate.apply(p.getObject())).sum() + " von " + maxPoints.getObject();
			}
		};
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
				list.addAll(supplementModul);
				list.addAll(basicModul);
				
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

	@SafeVarargs
	private static int calculatePoints(Function<Prof, Double> calculate, IModel<Prof>... profs){
		return (int)Math.round(Arrays.asList(profs).stream().mapToDouble(p -> calculate.apply(p.getObject())).sum());
	}
	
	private static int calculatePoints(double points){
		return (int)Math.round(points);
	}
	
	
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		int summaryWahl1 = calculatePoints(prof1.getObject().calculateWahlPoints());
		int summaryWahl2 = calculatePoints(prof2.getObject().calculateWahlPoints());
		
		int summaryPflicht = calculatePoints(Prof::calculatePflichtPoints, prof1, prof2);
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
						supplementModul.remove(course);
						basicModul.remove(course);
						send(getPage(), Broadcast.DEPTH, new RemoveCourseEvent(target));
					};
				};
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
