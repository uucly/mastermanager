package de.master.manager.noten;

import java.util.OptionalDouble;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import com.google.common.collect.Lists;

import de.master.manager.model.TransformationModel2;
import de.master.manager.profStuff.Prof;

public class NoteInfoPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public NoteInfoPanel(String id, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2) {
		super(id);
		setOutputMarkupId(true);
		add(new Label("finalGrade", calculateFinalGrade(profOfPanel1, profOfPanel2)));
	}
	
	private static final IModel<String> calculateFinalGrade(IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2){
		return new TransformationModel2<Prof, Prof, String>(profOfPanel1, profOfPanel2,(p1, p2) -> {
			OptionalDouble result = Lists.newArrayList(p1.calculateFinalGrade(), p2.calculateFinalGrade()).stream().filter(opt -> opt.isPresent()).mapToDouble(OptionalDouble::getAsDouble).average();
			return result.isPresent() ? result.toString() : "No grade available";
		});
	}

}
