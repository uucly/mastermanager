package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.List;

public interface IModulLoader extends Serializable{

	IModul loadModul(String fileName);
}
