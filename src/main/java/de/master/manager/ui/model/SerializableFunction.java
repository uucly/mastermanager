package de.master.manager.ui.model;

import java.io.Serializable;

import com.google.common.base.Function;

public interface SerializableFunction<T,R> extends Function<T, R>, Serializable{

}
