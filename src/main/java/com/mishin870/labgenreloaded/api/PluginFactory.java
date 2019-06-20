package com.mishin870.labgenreloaded.api;

import com.mishin870.labgenreloaded.api.network.WSListenerCallback;
import com.mishin870.labgenreloaded.api.utils.UtilsBundle;
import org.eclipse.jetty.websocket.api.Session;

import java.io.File;

/**
 * Главный класс плагина и одновременно фабрика экземпляров {@link PluginBase} (рабочей логики плагина)<br>
 * Существует всегда в единственном экземпляре в базе плагинов сервера<br>
 * Его место в базе определяют функции {@link #getCourseCode()} и {@link #getLabId()}<br>
 * <b>Важно: адрес этого класса должен лежать в файле plugin.properties в корне jar плагина</b>
 */
public abstract class PluginFactory {
	private File pluginFile;
	
	/**
	 * Конструктор по умолчанию для возможности создания объекта рефлексией
	 */
	public PluginFactory() {}
	
	/**
	 * Создать экземпляр логики плагина для пользователя
	 * 
	 * @param utilsBundle набор утилит, предоставляемый сервером
	 * @param callback обратная связь с вебсокет сервером
	 * @param session сессия вебсокета
	 * @param userId идентификатор пользователя
	 * @param action идентификатор запрашиваемого действия
	 * @return ссылку на созданный экземпляр
	 */
	public abstract PluginBase create(UtilsBundle utilsBundle, WSListenerCallback callback, Session session, long userId, String action);
	
	/**
	 * @return код курса, которому этот плагин принадлежит
	 */
	public abstract String getCourseCode();
	
	/**
	 * @return идентификатор лабораторной, которой этот плагин принадлежит
	 */
	public abstract long getLabId();
	
	/**
	 * Устанавливает файл, из которого загружен плагин
	 * @param pluginFile файл, из которого загружен плагин
	 */
	public final void setPluginFile(File pluginFile) {
		this.pluginFile = pluginFile;
	}
	
	/**
	 * @return файл, из которого загружен плагин
	 */
	public final File getPluginFile() {
		return this.pluginFile;
	}
}
