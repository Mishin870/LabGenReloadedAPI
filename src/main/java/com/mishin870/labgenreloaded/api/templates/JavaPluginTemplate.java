package com.mishin870.labgenreloaded.api.templates;

import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.PluginFactory;
import com.mishin870.labgenreloaded.api.java.JavaTester;
import com.mishin870.labgenreloaded.api.java.TestResult;
import com.mishin870.labgenreloaded.api.network.WSListenerCallback;
import com.mishin870.labgenreloaded.api.network.messages.MessageClose;
import com.mishin870.labgenreloaded.api.network.messages.MessageFile;
import com.mishin870.labgenreloaded.api.network.special.MessageJavaTestResult;
import com.mishin870.labgenreloaded.api.types.LabState;
import com.mishin870.labgenreloaded.api.utils.HttpApi;
import com.mishin870.labgenreloaded.api.utils.UtilsBundle;
import org.eclipse.jetty.websocket.api.Session;

import java.io.File;
import java.io.IOException;

/**
 * Шаблон стандартного плагина с функцией тестирования Java приложения и загрузки отчёта
 */
public class JavaPluginTemplate extends PluginBase {
	private static final String JAVA_ACTION = "java";
	private static final String JAVA_EXTRA_ACTION = "java_extra";
	private static final String REPORT_ACTION = "report";
	
	private static final int TEST_JAVA_FILE_ID = 5482;
	private static final int TEST_JAVA_EXTRA_FILE_ID = 2845;
	private static final int REPORT_FILE_ID = 8080;
	
	private static final String CHOOSE_JAR_FILE = "Выберите jar-файл с вашим приложением для отправки на сервер";
	private static final String JAR_MASK = ".jar";
	
	private static final String CHOOSE_DOCX_FILE = "Выберите docx-файл отчёта для отправки на сервер";
	private static final String DOCX_MASK = ".docx";
	
	private final Class<? extends JavaTester> javaTesterClass;
	private final Class<? extends JavaTester> javaExtraTesterClass;
	private final LabState labState;
	
	private boolean isExtraTest = false;
	
	/**
	 * @param pluginFactory ссылка на фабрику плагина
	 * @param utilsBundle утилиты
	 * @param callback обратная связь с хранителем вебсокета
	 * @param session сессия вебсокета
	 * @param userId идентификатор пользователя
	 * @param action запрошенное пользователем действие
	 * @param javaTesterClass {@link JavaTester}, выполняющий тестирование основной задачи
	 * @param javaExtraTesterClass {@link JavaTester}, выполняющий тестирование доп задачи
	 */
	public JavaPluginTemplate(PluginFactory pluginFactory, UtilsBundle utilsBundle, WSListenerCallback callback, Session session, long userId, String action, Class<? extends JavaTester> javaTesterClass, Class<? extends JavaTester> javaExtraTesterClass) {
		super(pluginFactory, utilsBundle, callback, session, userId, action);
		this.javaTesterClass = javaTesterClass;
		this.javaExtraTesterClass = javaExtraTesterClass;
		
		LabState state = null;
		try {
			state = HttpApi.getLabState(userId, pluginFactory.getLabId());
		} catch (IOException e) {
			this.alert(
					new TestResult(false, "Невозможно получить состояние лабораторной работы", e).getFinalMessage(),
					"Ошибка"
			);
			this.stop();
			return;
		} finally {
			this.labState = state;
		}
		
		switch (action) {
			case JAVA_ACTION: {
				String error = this.labState.canExecuteTest();
				if (error != null) {
					this.alert(error, "Невозможно начать основную проверку");
					this.stop();
					return;
				}
				
				new MessageFile(
						TEST_JAVA_FILE_ID, "Основная задача",
						CHOOSE_JAR_FILE, JAR_MASK
				).send(session);
				break;
			}
			case JAVA_EXTRA_ACTION: {
				String error = this.labState.canExecuteTestExtra();
				if (error != null) {
					this.alert(error, "Невозможно начать дополнительную проверку");
					this.stop();
					return;
				}
				
				new MessageFile(
						TEST_JAVA_EXTRA_FILE_ID, "Дополнительная задача",
						CHOOSE_JAR_FILE, JAR_MASK
				).send(session);
				break;
			}
			case REPORT_ACTION: {
				String error = this.labState.canSendReport();
				if (error != null) {
					this.alert(error, "Невозможно загрузить отчёт");
					this.stop();
					return;
				}
				
				new MessageFile(
						REPORT_FILE_ID, "Отчёт",
						CHOOSE_DOCX_FILE, DOCX_MASK
				).send(session);
				break;
			}
		}
	}
	
	@Override
	public void onMessage(MessageClose close) {
		long meta = close.meta;
		
		if (meta == TEST_JAVA_FILE_ID || meta == TEST_JAVA_EXTRA_FILE_ID || meta == REPORT_FILE_ID) {
			alert("Попробуйте ещё раз. Вы не выбрали файл", "Неудача");
		}
		this.stop();
	}
	
	@Override
	public void onMessage(MessageFile file) {
		final long meta = file.meta;
		
		if (meta == TEST_JAVA_FILE_ID || meta == TEST_JAVA_EXTRA_FILE_ID) {
			this.isExtraTest = meta == TEST_JAVA_EXTRA_FILE_ID;
			
			// Юзер всё ещё может прислать файл с метой другого теста
			String error = this.isExtraTest
					? this.labState.canExecuteTestExtra()
					: this.labState.canExecuteTest();
			if (error != null) {
				this.alert(error, "Ошибка");
				this.stop();
				return;
			}
			this.utilsBundle.getJavaUtils().startTest(
					this.factory,
					this,
					this.isExtraTest ? javaExtraTesterClass : javaTesterClass,
					file.file, this.userId, labState
			);
		} else if (meta == REPORT_FILE_ID) {
			String error = this.labState.canSendReport();
			if (error != null) {
				this.alert(error, "Ошибка");
				this.stop();
				return;
			}
			
			String path;
			try {
				path = HttpApi.getReportPath(userId, this.factory.getLabId(), this.factory.getCourseCode());
			} catch (IOException e) {
				this.alert(
						new TestResult(false, "Невозможно получить локальный адрес для загрузки отчёта", e).getFinalMessage(),
						"Ошибка"
				);
				this.stop();
				return;
			}
			try {
				this.moveWithOverwrite(file.file, new File(path + "report.docx"));
				this.alert("Отчёт успешно отправлен\nТеперь преподаватель должен проверить его", "Успех");
			} catch (IOException ioe) {
				TestResult exceptionResult = new TestResult(
						false,
						"Ошибка при сохранении файла отчёта",
						ioe
				);
				this.alert(exceptionResult.getFinalMessage(), "Неудача");
			}
			this.stop();
		} else {
			this.alert("Неверное значение meta у файла", "Ошибка");
			this.stop();
		}
	}
	
	@Override
	public void onMessage(MessageJavaTestResult javaTestResult) {
		TestResult testResult = javaTestResult.testResult;
		alert(testResult.getFinalMessage(), testResult.success ? "Успех" : "Неудача");
		
		if (testResult.success) {
			try {
				if (isExtraTest) {
					this.labState.setTestExtra(true);
				} else {
					this.labState.setTest(true);
				}
				
				HttpApi.setLabState(
						userId, factory.getLabId(), this.labState
				);
				
				this.reloadPage();
			} catch (IOException ioe) {
				this.alert(
						new TestResult(false, "Невозможно изменить состояние лабораторной работы", ioe).getFinalMessage(),
						"Ошибка"
				);
				this.stop();
				return;
			}
		}
		
		this.stop();
	}
	
}
