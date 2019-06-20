package com.mishin870.labgenreloaded.api.java.shadow;

import java.io.PrintStream;

/**
 * Подписчик события вывода текста теневого System.out<br>
 * Подробное описание см. в {@link OutputHandler#outputCallback(String, boolean, OutputType, PrintStream)}
 */
public interface OutputHandler {
	
	/**
	 * Слушатель события вывода текста в System.out<br>
	 * <b>Должен лишь удостовериться в том, что тип вывода не запрещён условиями
	 * лабораторной работы, а не собирать выводимый текст</b><br>
	 * В случае не соответствия правилам лабораторной работы должен возбудить {@link RuntimeException}
	 * с подробным описанием проблемы
	 *
	 * @param text выводимый текст
	 * @param newLine выводится ли это с помощью printLN, а не print
	 * @param outType тип выводимых данных (вариант перегрузки print/println)
	 * @param realStream объект реального потока вывода (в целях дебага. писать туда ничего не стоит)
	 */
	void outputCallback(String text, boolean newLine, OutputType outType, PrintStream realStream);
	
}