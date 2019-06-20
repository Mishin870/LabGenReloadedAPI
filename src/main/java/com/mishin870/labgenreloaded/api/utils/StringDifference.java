package com.mishin870.labgenreloaded.api.utils;

import java.util.*;

/**
 * Утилита для выделения отличий двух строк
 */
public class StringDifference {
	private static final String SPACE_REPLACEMENT = "\u2591";
	
	private static final Map<OutputEntryType, OutputEntryStyle> DEFAULT_STYLES = new HashMap<>();
	static {
		DEFAULT_STYLES.put(OutputEntryType.NONE, new NoneOutputEntryStyle());
		DEFAULT_STYLES.put(OutputEntryType.ADDED, new SpanOutputEntryStyle("added"));
		DEFAULT_STYLES.put(OutputEntryType.REMOVED, new SpanOutputEntryStyle("removed"));
	}
	
	private final Map<OutputEntryType, OutputEntryStyle> styles;
	
	/**
	 * @param styles карта стилей выделения участков текста
	 */
	public StringDifference(Map<OutputEntryType, OutputEntryStyle> styles) {
		this.styles = styles;
	}
	
	public StringDifference() {
		this(DEFAULT_STYLES);
	}
	
	/**
	 * Сравнивает строки, возвращает стилизованный результат сравнения
	 *
	 * @param original эталонная строка
	 * @param second сравниваемая строка
	 * @return стилизованный результат (по умолчанию в html)
	 */
	public String makeDifference(String original, String second) {
		original = original.replaceAll(" ", SPACE_REPLACEMENT);
		second = second.replaceAll(" ", SPACE_REPLACEMENT);
		
		String[] originalLines = original.split("\n");
		String[] secondLines = second.split("\n");
		int originalMax = originalLines.length;
		int secondMax = secondLines.length;
		int maxIndex = Math.max(originalMax, secondMax);
		
		StringBuilder result = new StringBuilder();
		
		for (int i = 0; i < maxIndex; i++) {
			if (i < originalMax && i < secondMax) {
				StringComparator comparator = new StringComparator(originalLines[i], secondLines[i]);
				List<OutputEntry> entries = comparator.calculateEntries();
				
				for (OutputEntry entry : entries) {
					result.append(styles.get(entry.type).toHtml(entry));
				}
			} else {
				boolean isOriginalLine = i < originalMax;
				
				OutputEntry entry = new OutputEntry(
						isOriginalLine ? originalLines[i] : secondLines[i],
						isOriginalLine ? OutputEntryType.REMOVED : OutputEntryType.ADDED
				);
				result.append(styles.get(entry.type).toHtml(entry));
			}
			
			if (i != maxIndex - 1) {
				result.append('\n');
			}
		}
		
		return result.toString();
	}
	
	/**
	 * @return пояснение к стилизованным строкам
	 */
	public String getLegend() {
		return String.format(
				"'%s' - пробел",
				SPACE_REPLACEMENT
		);
	}
	
	/**
	 * Логика посимвольного выделения отличий в строках
	 */
	private static class StringComparator {
		private final String original, second;
		private final char[] originalChars, secondChars;
		private final int originalLength, secondLength;
		private int originalIndex = 0;
		private int secondIndex = 0;
		
		public StringComparator(String original, String second) {
			this.original = original;
			this.second = second;
			this.originalChars = this.original.toCharArray();
			this.secondChars = this.second.toCharArray();
			this.originalLength = this.originalChars.length;
			this.secondLength = this.secondChars.length;
		}
		
		private OutputEntry none(String text) {
			return new OutputEntry(text, OutputEntryType.NONE);
		}
		private OutputEntry added(String text) {
			return new OutputEntry(text, OutputEntryType.ADDED);
		}
		private OutputEntry removed(String text) {
			return new OutputEntry(text, OutputEntryType.REMOVED);
		}
		
		private boolean isOverflow() {
			return originalIndex >= originalLength || secondIndex >= secondLength;
		}
		
		private List<OutputEntry> nextEntries() {
			if (originalChars[originalIndex] == secondChars[secondIndex]) {
				final int sourceTaskIndex = originalIndex;
				
				while (!isOverflow() && originalChars[originalIndex] == secondChars[secondIndex]) {
					originalIndex++;
					secondIndex++;
				}
				
				return Collections.singletonList(none(original.substring(sourceTaskIndex, originalIndex)));
			} else {
				final int sourceSecondIndex = secondIndex;
				
				while (!isOverflow() && originalChars[originalIndex] != secondChars[secondIndex]) {
					for (int i = originalIndex; i < originalLength; i++) {
						if (originalChars[i] == secondChars[secondIndex]) {
							final int oldTaskIndex = originalIndex;
							originalIndex = i;
							if (sourceSecondIndex != secondIndex) {
								return Arrays.asList(
										removed(original.substring(oldTaskIndex, originalIndex)),
										added(second.substring(sourceSecondIndex, secondIndex))
								);
							} else {
								return Collections.singletonList(removed(original.substring(oldTaskIndex, originalIndex)));
							}
						}
					}
					secondIndex++;
				}
				
				if (isOverflow()) {
					List<OutputEntry> result = Arrays.asList(
							removed(original.substring(originalIndex)),
							added(second.substring(sourceSecondIndex))
					);
					
					if (secondIndex >= secondLength) {
						originalIndex = originalLength;
					}
					
					return result;
				} else {
					return Collections.singletonList(added(second.substring(sourceSecondIndex, secondIndex)));
				}
			}
		}
		
		public List<OutputEntry> calculateEntries() {
			List<OutputEntry> result = new ArrayList<>();
			
			while (!isOverflow()) {
				result.addAll(nextEntries());
			}
			
			if (originalIndex < originalLength) {
				result.add(removed(original.substring(originalIndex)));
			} else if (secondIndex < secondLength) {
				result.add(added(second.substring(secondIndex)));
			}
			
			return result;
		}
	}
	
	/**
	 * Стилизатор строки, вкладывающий текст в html элементы {@code <span>}
	 */
	public static class SpanOutputEntryStyle extends OutputEntryStyle {
		private final String tagClass;
		
		public SpanOutputEntryStyle(String tagClass) {
			this.tagClass = tagClass;
		}
		
		@Override
		public String toHtml(OutputEntry entry) {
			return String.format(
					"<span class='%s'>%s</span>",
					tagClass, entry.text
			);
		}
	}
	
	/**
	 * Стилизатор, не выполняющий стилизацию
	 */
	public static class NoneOutputEntryStyle extends OutputEntryStyle {
		@Override
		public String toHtml(OutputEntry entry) {
			return entry.text;
		}
	}
	
	/**
	 * Базовый стилизатор строк
	 */
	public abstract static class OutputEntryStyle {
		public abstract String toHtml(OutputEntry entry);
	}
	
	/**
	 * Возможные типы участков строк
	 */
	public enum OutputEntryType {
		/**
		 * Обычный участок строки. Имеется в обеих строках
		 */
		NONE,
		/**
		 * Участок, отсутствующий в первой строке
		 */
		REMOVED,
		/**
		 * Участок, отсутствующий во второй строке
		 */
		ADDED;
	}
	
	/**
	 * Дескриптор участка текста
	 */
	public static class OutputEntry {
		public String text;
		public OutputEntryType type;
		
		public OutputEntry(String text, OutputEntryType type) {
			this.text = text;
			this.type = type;
		}
	}
}
