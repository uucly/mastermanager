package dragAndDrop;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import com.modul.Modul;
import com.professoren.Prof;

public class ButtonListView extends ListView<Modul>{

	private static final long serialVersionUID = 1L;
	private IModel<Prof> prof;

	public ButtonListView(String id, IModel<? extends List<? extends Modul>> model, IModel<Prof> prof) {
		super(id, model);
		this.prof = prof;
	}

	@Override
	protected void populateItem(ListItem<Modul> item) {
		Button b = new ModulButton("modulButton", item.getModelObject(), prof);
		item.add(b);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
	}

}
