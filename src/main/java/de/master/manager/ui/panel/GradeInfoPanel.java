package de.master.manager.ui.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.google.common.base.Optional;
import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.Modul;
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
				Prof prof1 = profOfPanel1.getObject();
				Prof prof2 = profOfPanel2.getObject();
				IModul modul = Modul.createInstance(prof1.getPflichtModulSelected(), prof1.getWahlModulSelected(), prof2.getPflichtModulSelected(), prof2.getWahlModulSelected(), supplementModul, basicModul);
				Optional<Double> grade = modul.calculateGrade();
				
				return grade.isPresent() ? String.valueOf(round(grade.get())) : "keine Note eingetragen";
				}
		};
	}
	
	private static final double round(double number){
		return Math.round(number*100.0)/100.0;
	}

}
