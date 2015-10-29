package de.master.manager.noten;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.master.manager.profStuff.Prof;

public class NoteInfoPanel extends Panel{

	public NoteInfoPanel(String id, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2) {
		super(id);
		add(new Label("finalGrade", calculateFinalGrade(profOfPanel1, profOfPanel2)));
	}
	
	private static final IModel<Double> calculateFinalGrade(IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2){
		Prof p1 = profOfPanel1.getObject();
		Prof p2 = profOfPanel2.getObject();
		
		double grade = 0;
		
		grade += p1.calculateFinalPflichtGrade().isPresent() ? p1.calculateFinalPflichtGrade().getAsDouble() : 0;
		grade += p1.calculateFinalWahlGrade().isPresent() ? p1.calculateFinalWahlGrade().getAsDouble() : 0;
		
		grade += p2.calculateFinalPflichtGrade().isPresent() ? p2.calculateFinalPflichtGrade().getAsDouble() : 0;
		grade += p2.calculateFinalWahlGrade().isPresent() ? p2.calculateFinalWahlGrade().getAsDouble() : 0;
		
		return grade > 0 ? Model.of(grade/4) : Model.of();
	}

}
