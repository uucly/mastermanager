package com.germany;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.wicket.model.IModel;

public class SelectedModulReadOnlyModel {

	private IModel<Collection<Modul>> profModuls;
	private Collection<IModel<String>> selectedModulNames;

	public SelectedModulReadOnlyModel(){}
	
	public SelectedModulReadOnlyModel(IModel<Collection<Modul>> profModuls, Collection<IModel<String>> selectedModulNames){
		this.profModuls = profModuls;
		this.selectedModulNames = selectedModulNames;
	}

	public List<Modul> getObject() {
		Predicate<Modul> filterModuls = m -> selectedModulNames.contains(m.getName());
		return profModuls.getObject().stream().filter(filterModuls).collect(Collectors.toList());
	}

	public void setProfModuls(IModel<Collection<Modul>> profModuls) {
		this.profModuls = profModuls;
	}

	public void setSelectedModulNames(Collection<IModel<String>> selectedModulNames) {
		this.selectedModulNames = selectedModulNames;
	}

}
