package models;

import java.util.Objects;
import java.util.function.BiFunction;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class TransformationModel2<M1, M2, T> extends LoadableDetachableModel<T>{

	private static final long serialVersionUID = 1L;
	private final IModel<? extends M1> m1;
	private final IModel<? extends M2> m2;
	private final BiFunction<M1, M2, T> transform;

	public TransformationModel2(IModel<? extends M1> m1,
			IModel<? extends M2> m2, BiFunction<M1, M2, T> transform) {
		this.m1 = m1;
		this.m2 = m2;
		this.transform = transform;
	}

	@Override
	protected T load() {
		return load(m1.getObject(), m2.getObject());
	}

	@Override
	public void detach() {
		m1.detach();
		m2.detach();
		super.detach();
	}
	protected T load(M1 m1, M2 m2) {
		Objects.requireNonNull(transform, "Transformation must not null");
		return transform.apply(m1, m2);
	}

}
