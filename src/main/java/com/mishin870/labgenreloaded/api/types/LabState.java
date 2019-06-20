package com.mishin870.labgenreloaded.api.types;

/**
 * Обёртка битовой маски состояния лабораторной работы<br>
 * Биты в маске (начиная с младшего):
 * <ul>
 *     <li>0 - сдана ли основная проверка</li>
 *     <li>1 - сдана ли дополнительная проверка</li>
 *     <li>2 - сдан (и проверен) ли отчёт</li>
 *     <li>3 - закрыта ли лабораторная работа</li>
 *     <li>4 - невидима ли лабораторная работа</li>
 * </ul>
 */
public class LabState {
	private static final int TEST_BIT = 1;
	private static final int TEST_EXTRA_BIT = 2;
	private static final int REPORT_BIT = 4;
	private static final int LOCKED_BIT = 8;
	private static final int INVISIBLE_BIT = 16;
	
	private static final String ERROR_NOT_AVAILABLE = "Лабораторная работа недоступна";
	private static final String ERROR_ALREADY_PASSED = "Эта проверка уже была сдана";
	private static final String ERROR_EXTRA_WITHOUT_MAIN = "Попытка сдачи дополнительной проверки до основной";
	private static final String ERROR_REPORT_NO_TEST = "Для сдачи отчёта нужно сдать все проверки";
	private static final String ERROR_REPORT_ALREADY_ACCEPTED = "Отчёт уже был подтверждён преподавателем";
	
	private boolean test;
	private boolean testExtra;
	private boolean report;
	private boolean locked;
	private boolean invisible;
	private int state;
	
	/**
	 * @param state числовое представление состояния
	 */
	public LabState(int state) {
		this.state = state;
		
		this.test = (state & TEST_BIT) != 0;
		this.testExtra = (state & TEST_EXTRA_BIT) != 0;
		this.report = (state & REPORT_BIT) != 0;
		this.locked = (state & LOCKED_BIT) != 0;
		this.invisible = (state & INVISIBLE_BIT) != 0;
	}
	
	/**
	 * @return недоступность лабораторной работы
	 */
	public boolean isInavailable() {
		return locked || invisible;
	}
	
	/**
	 * Проверить возможность запуска основного теста
	 *
	 * @return сообщение об ошибке, либо null, если тест возможен
	 */
	public String canExecuteTest() {
		if (isInavailable()) {
			return ERROR_NOT_AVAILABLE;
		} else if (this.test) {
			return ERROR_ALREADY_PASSED;
		}
		
		return null;
	}
	
	/**
	 * Проверить возможность отправки отчёта
	 *
	 * @return сообщение об ошибке, либо null, если сдача возможна
	 */
	public String canSendReport() {
		if (isInavailable()) {
			return ERROR_NOT_AVAILABLE;
		} else if (!this.test || !this.testExtra) {
			return ERROR_REPORT_NO_TEST;
		} else if (this.report) {
			return ERROR_REPORT_ALREADY_ACCEPTED;
		}
		
		return null;
	}
	
	/**
	 * Проверить возможность запуска дополнительного теста
	 *
	 * @return сообщение об ошибке, либо null, если тест возможен
	 */
	public String canExecuteTestExtra() {
		if (isInavailable()) {
			return ERROR_NOT_AVAILABLE;
		} else if (this.testExtra) {
			return ERROR_ALREADY_PASSED;
		} else if (!this.test) {
			return ERROR_EXTRA_WITHOUT_MAIN;
		}
		
		return null;
	}
	
	/**
	 * Устанавливает определённый бит в состоянии
	 *
	 * @param bit бит
	 * @param value значение
	 */
	private void setBit(int bit, boolean value) {
		this.state = value
				? (this.state | bit)
				: (this.state & ~bit);
	}
	
	public void setTest(boolean test) {
		this.test = test;
		this.setBit(TEST_BIT, test);
	}
	public void setTestExtra(boolean testExtra) {
		this.testExtra = testExtra;
		this.setBit(TEST_EXTRA_BIT, testExtra);
	}
	public void setReport(boolean report) {
		this.report = report;
		this.setBit(REPORT_BIT, report);
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
		this.setBit(LOCKED_BIT, locked);
	}
	
	public boolean isTest() {
		return test;
	}
	public boolean isTestExtra() {
		return testExtra;
	}
	public boolean isReport() {
		return report;
	}
	public boolean isLocked() {
		return locked;
	}
	public boolean isInvisible() {
		return invisible;
	}
	
	/**
	 * @return числовое представление состояния
	 */
	public int getValue() {
		return state;
	}
	
}
