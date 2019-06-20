package com.mishin870.labgenreloaded.api.java.shadow;

/**
 * Тип теневого объекта<br>
 * Теневые объекты - классы, которые замещают классы из стандартного
 * пакета Java, для того, чтобы возможно было определять ход
 * работы пользовательского приложения
 */
public enum ShadowType {
	PRINT_STREAM("PrintStream");
	
	private final String name;
	
	ShadowType(String name) {
		this.name = name;
	}
	
	/**
	 * @return название теневого объекта
	 */
	public String getName() {
		return name;
	}
}