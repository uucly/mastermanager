package de.master.manager.ui.panel;

import java.util.OptionalDouble;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

import de.master.manager.profStuff.BasicModul;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementModul;
import de.master.manager.ui.model.TransformationModel2;

public class GradeInfoPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public GradeInfoPanel(String id, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, BasicModul basicModul, SupplementModul supplementModul) {
		super(id);
		setOutputMarkupId(true);
		add(new Label("finalGrade", calculateFinalGrade(profOfPanel1, profOfPanel2, basicModul, supplementModul)));
	}
	
	private static final IModel<String> calculateFinalGrade(IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, BasicModul basicModul, SupplementModul supplementModul){
		return new TransformationModel2<Prof, Prof, String>(profOfPanel1, profOfPanel2,(p1, p2) -> {
			OptionalDouble result = Lists.newArrayList(p1.calculateFinalGrade(), p2.calculateFinalGrade(), basicModul.calculateGrade(), supplementModul.calculateGrade()).stream().filter(opt -> opt.isPresent()).mapToDouble(OptionalDouble::getAsDouble).average();
			
			return result.isPresent() ? String.valueOf(round(result.getAsDouble())) : "keine Note eingetragen";
		});
	}
	
	private static final double round(double number){
		return Math.round(number*100.0)/100.0;
	}

}
