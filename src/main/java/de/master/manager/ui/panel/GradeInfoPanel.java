package de.master.manager.ui.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.Prof;

public class GradeInfoPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public GradeInfoPanel(String id, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, IModul basicModul, IModul supplementModul) {
		super(id);
		setOutputMarkupId(true);
		add(new Label("finalGrade", calculateFinalGrade(profOfPanel1, profOfPanel2, basicModul, supplementModul)));
	}
	
	private static final IModel<String> calculateFinalGrade(final IModel<Prof> profOfPanel1, final IModel<Prof> profOfPanel2, final IModul basicModul, final IModul supplementModul){
		return new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				double grades = 0;
				int numberOfGrades = 0;
				List<Optional<Double>> allGrades = Lists.newArrayList(profOfPanel1.getObject().calculateFinalGrade(), profOfPanel2.getObject().calculateFinalGrade(), basicModul.calculateGrade(), supplementModul.calculateGrade());
				for(Optional<Double> opt : allGrades){
					if(opt.isPresent()){
						grades = opt.get();
						numberOfGrades++;
					}
				}
				double finalGrade = grades/numberOfGrades;
				return numberOfGrades == 0 ? String.valueOf(round(finalGrade)) : "keine Note eingetragen";
				}
		};
	}
	
	private static final double round(double number){
		return Math.round(number*100.0)/100.0;
	}

}
