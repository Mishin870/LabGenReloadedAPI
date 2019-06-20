package com.mishin870.labgenreloaded.api.java.shadow;

/**
 * Тип вывода перегруженной функции {@link PrintStreamShadow}
 */
public enum OutputType {
	/**
	 * boolean
	 */
	BOOLEAN,
	
	/**
	 * char
	 */
	CHAR,
	
	/**
	 * int
	 */
	INT,
	
	/**
	 * long
	 */
	LONG,
	
	/**
	 * float
	 */
	FLOAT,
	
	/**
	 * double
	 */
	DOUBLE,
	
	/**
	 * char[]
	 */
	CHAR_ARRAY,
	
	/**
	 * String
	 */
	STRING,
	
	/**
	 * Object.toString()
	 */
	OBJECT,
	
	/**
	 * println без аргументов
	 * ('\n' или "\n")
	 */
	NEW_LINE,
	
	/**
	 * Object... args
	 * (arg.toString())
	 */
	FORMAT_OBJECTS_ARGS
}

