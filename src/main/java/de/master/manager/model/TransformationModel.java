package de.master.manager.model;

import java.util.Objects;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class TransformationModel<M, T> extends LoadableDetachableModel<T> {

	private static final long serialVersionUID = 1L;
	private IModel<? extends M> model;
	private SerializableFunction<M, T> transform;

	public TransformationModel(IModel<? extends M> model,
			SerializableFunction<M, T> transform) {
		this.model = model;
		this.transform = transform;
	}

	@Override
	public void detach() {
		model.detach();
		super.detach();
	}

	@Override
	protected T load() {
		return load(model);
	}

	protected T load(IModel<? extends M> model) {
		Objects.requireNonNull(transform, "Transformation must not null");
		return transform.apply(model.getObject());
	}
}
