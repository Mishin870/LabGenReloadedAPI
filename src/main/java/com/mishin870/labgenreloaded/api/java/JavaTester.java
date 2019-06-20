package com.mishin870.labgenreloaded.api.java;

import com.mishin870.labgenreloaded.api.java.shadow.OutputHandler;
import com.mishin870.labgenreloaded.api.java.shadow.OutputType;
import com.mishin870.labgenreloaded.api.java.shadow.PrintStreamShadow;
import com.mishin870.labgenreloaded.api.types.LabState;
import com.mishin870.labgenreloaded.api.utils.JavaUtils;

import java.io.PrintStream;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.List;

/**
 * Логика тестирования Java-приложения<br>
 * Используется в {@link JavaUtils} как класс, который осуществляет логику тестирования
 */
public abstract class JavaTester implements OutputHandler {
	
	/**
	 * Конструктор по умолчанию для возможности создания объекта рефлексией
	 */
	public JavaTester() {}
	
	/**
	 * Главная функция тестировщика, которую нужно обязательно реализовать<br>
	 * Запускает пользовательское приложение и получает его вывод (при помощи {@link PrintStreamShadow}),
	 * смотрит файловую систему, слушает сокет и т.д. (по логике лабораторной работы)
	 *
	 * @param classes список всех классов пользовательского приложения
	 * @param mainClass главный класс пользовательского приложения
	 * @param stdout теневой поток вывода, собирающий в себя весь вывод
	 *               пользовательского приложения<br>
	 *               Для получения итогового вывода см. {@link PrintStreamShadow#getOutput()}
	 * @param userId идентификатор пользователя (для генерации задачи по нему)
	 * @param labState текущее состояния лабораторной работы
	 * @return результат тестирования
	 * @throws Exception в случае любых ошибок
	 */
	public abstract TestResult test(List<Class<?>> classes, Class<?> mainClass, PrintStreamShadow stdout, long userId, LabState labState) throws Exception;
	
	@Override
	public void outputCallback(String text, boolean newLine, OutputType outType, PrintStream realStream) {}
	
	/**
	 * @return список разрешений пользовательского приложения<br>
	 *     По умолчанию возвращает пустой список {@link Permissions}
	 */
	public PermissionCollection getPermissions() {
		return new Permissions();
	}
	
	/**
	 * Подготовить вывод пользовательского приложения для:
	 * <ul>
	 *     <li>Посимвольного сравнения с исходной задачей</li>
	 *     <li>Читабельности текста в выводе ошибки проверки</li>
	 * </ul>
	 * Сначала удаляет все лишние переносы строк и пробелы с конца строки, а
	 * потом проверяет длину строки. В случае, если строка оказалась пуста, возвращает
	 * читабельный текст {@code [Пустая строка]}<br>
	 * <b>Если исходная задача тоже содержит пробелы и/или переносы строк в конце, то
	 * её тоже необходимо обработать данной функцией</b>
	 *
	 * @param text вывод пользовательского приложения
	 * @return подготовленный текст
	 */
	protected String prepareText(String text) {
		String result = text.replaceAll("\\\\n", "\n").replaceFirst("\\s++$", "");
		if (result.isEmpty()) {
			return "[Пустая строка]";
		} else {
			return result;
		}
	}
}
