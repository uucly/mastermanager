package com.modul;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import com.google.common.base.Strings;
import com.professoren.Prof;

public class ModulAutoCompleteTextField extends AutoCompleteTextField<String> {

	private static final long serialVersionUID = 1L;
	private final IModel<List<Modul>> moduls;
	private String selected;
	
	public ModulAutoCompleteTextField(String id, IModel<String> modulModel, IModel<Prof> prof, ListModel<Modul> moduls) {
		super(id, modulModel);
		this.moduls = moduls;
		selected = "";
		add(new OnChangeAjaxBehavior(){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				moduls.getObject().stream().filter(m -> m.getName().equals(selected)).forEach(m -> m.setInUse(false));
				selected = getModelObject();
				moduls.getObject().stream().filter(m -> m.getName().equals(getModelObject())).forEach(m -> m.setInUse(true));	
			}
			
		});
		
		
		add(new AjaxFormComponentUpdatingBehavior("onChange"){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Optional<Modul> selectedModul = moduls.getObject().stream().filter(m -> m.getName().equals(selected)).findFirst();
				if(selectedModul.isPresent()){
					prof.getObject().addSelectedModul(selectedModul.get());
				}
				send(getPage(), Broadcast.DEPTH, new SelectedEvent(target));
			}
			
		});
	}

	@Override
	protected Iterator<String> getChoices(String input) {
		if (Strings.isNullOrEmpty(input)) {
			return Collections.<String>emptyList().iterator();
		}

		return moduls.getObject().stream().filter(Modul::isNotInUse).filter(m -> m.getName().toUpperCase().startsWith(input.toUpperCase())).map(m -> m.getName()).collect(Collectors.toList()).iterator();
	}
	
}
