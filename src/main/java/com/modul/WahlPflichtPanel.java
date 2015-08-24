package com.modul;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;
import com.professoren.Prof;

import dragAndDrop.AbstractEvent;

public class WahlPflichtPanel extends Panel{

	private static final long serialVersionUID = 1L;

	private static final String ALL_PATH= "src/main/resources/WahlPflichtModule.txt";
	private static final List<Prof> SEARCH_ENGINES = Arrays.asList(Prof.values());
	
	@SpringBean
	private WahlPflichtModule module;
	
	private final AbstractEvent profEvent;
	private final Form<?> form;

	private CourseAutoCompleteTextField t;

	private IModel<Prof> prof;

	private Model<String> first;

	private Model<String> second;

	private Model<String> third;

	private Model<String> fourth;
	
	public WahlPflichtPanel(String id, IModel<Prof> prof, AbstractEvent profEvent) {
		super(id);
		this.profEvent = profEvent;
		this.prof = prof;
		form = createForm(createModulParser(module), prof, profEvent);
		add(form);
	}
	
	private  Form<?> createForm(final CourseParser modulParser, IModel<Prof> prof, AbstractEvent profEvent){
		ListModel<Course> moduleOfProf = new ListModel<Course>();
	
		List<Course> firstFourModuls = loadFirstFourModuls(prof);
		first = Model.of(firstFourModuls.get(0).getName());
		second= Model.of(firstFourModuls.get(1).getName());
		third = Model.of(firstFourModuls.get(2).getName());
		fourth = Model.of(firstFourModuls.get(3).getName());
		
		Form<?> form = new Form<Object>("form");
		form.add(new CourseAutoCompleteTextField("auto1", first,prof, moduleOfProf));
		form.add(new CourseAutoCompleteTextField("auto2", second,prof, moduleOfProf));
		form.add(new CourseAutoCompleteTextField("auto3", third,prof, moduleOfProf));
		form.add(new CourseAutoCompleteTextField("auto4", fourth,prof, moduleOfProf));
		form.add(createDropDown(moduleOfProf, modulParser, prof, profEvent));
		return form;
	}
	
	private static List<Course> loadFirstFourModuls(IModel<Prof> prof) {
		List<Course> list = Lists.newArrayList();
		for(Course m : prof.getObject().getSelectedModuls()){
			list.add(m);
		}
		
		for(int i=list.size(); i<4;i++){
			list.add(new Course("",0));
		}
		return list;
	}

	private static DropDownChoice<Prof> createDropDown(final IModel<List<Course>> moduleOfProf, CourseParser modulParser, IModel<Prof> prof, AbstractEvent profEvent){
		moduleOfProf.setObject(loadModulsOfProf(modulParser, prof));
		DropDownChoice<Prof> dropDown = new DropDownChoice<Prof>("dropDown",prof, SEARCH_ENGINES);
		dropDown.add(new OnChangeAjaxBehavior() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				moduleOfProf.setObject(loadModulsOfProf(modulParser, prof));
				profEvent.setTarget(target);
				dropDown.send(dropDown.getPage(), Broadcast.DEPTH, profEvent);
			}
		});
		
		return dropDown;
	}
	
	
	private static List<Course> loadModulsOfProf(CourseParser modulParser, IModel<Prof> selected){
		try{
			return modulParser.parse(selected.getObject().getPath());
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}
	}
	
	private static CourseParser createModulParser(WahlPflichtModule module){
		try {
			List<Course> allModule = module.parse(ALL_PATH);
			return new CourseParser(allModule);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if(event.getPayload() instanceof AbstractEvent && profEvent.getId() == ((AbstractEvent)event.getPayload()).getId()){
			List<Course> firstFourModuls = loadFirstFourModuls(prof);
			first.setObject(firstFourModuls.get(0).getName());
			second.setObject(firstFourModuls.get(1).getName());
			third.setObject(firstFourModuls.get(2).getName());
			fourth.setObject(firstFourModuls.get(3).getName());
			
			((AbstractEvent)event.getPayload()).getTarget().add(form);
		}
	}
	
}
