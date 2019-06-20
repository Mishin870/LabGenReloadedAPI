package com.mishin870.labgenreloaded.api.utils;

import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.PluginFactory;
import com.mishin870.labgenreloaded.api.java.JavaTester;
import com.mishin870.labgenreloaded.api.types.LabState;

import java.io.File;

/**
 * Утилита тестирования Java программ
 */
public abstract class JavaUtils {
	
	/**
	 * Начать Java тест
	 *
	 * @param factory фабрика логики плагина
	 * @param pluginBase логика плагина
	 * @param testClass запускаемый для тестирования класс
	 * @param userApp ссылка на jar файл пользовательского приложения
	 * @param userId идентификатор пользователя
	 * @param labState текущее состояние лабораторной работы
	 */
	public abstract void startTest(PluginFactory factory, PluginBase pluginBase, Class<? extends JavaTester> testClass, File userApp, long userId, LabState labState);
	
}
