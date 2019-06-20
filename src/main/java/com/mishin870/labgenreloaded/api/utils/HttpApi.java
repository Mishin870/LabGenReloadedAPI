package com.mishin870.labgenreloaded.api.utils;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.mishin870.labgenreloaded.api.types.LabState;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Интерфейс для общения с веб-частью лабгена посредством API шлюза
 */
public class HttpApi {
	private static final String API_URL = "https://labgen.zss.local/api/";
	
	/**
	 * Отправляет GET запрос и возвращает весь результат в текстовом виде
	 *
	 * @param url адрес запроса с параметрами
	 * @return результат запроса
	 * @throws IOException в случае ошибок ввода-вывода
	 */
	private static String GET(String url) throws IOException {
		// System.out.format("GET: %s", url);
		
		URLConnection connection = new URL(url).openConnection();
		try (InputStream inputStream = connection.getInputStream()) {
			String encoding = connection.getContentEncoding();
			if (encoding == null) {
				encoding = "UTF-8";
			}
			return IOUtils.toString(inputStream, Charsets.toCharset(encoding));
		}
	}
	
	/**
	 * Сгенерировать локальный адрес папки для загрузки отчёта по лабораторной работе<br>
	 * Возвращает путь вида: /var/www/html_ssl/labgen/static/uploads/...
	 *
	 * @param userId идентификатор пользователя
	 * @param labId идентификатор лабы
	 * @param course код курса
	 * @return локальный адрес папки для загрузки отчёта по лабораторной работе
	 * @throws IOException в случае ошибок ввода-вывода
	 */
	public static String getReportPath(long userId, long labId, String course) throws IOException {
		String request = String.format(
				"%s?action=getReportPath&userId=%d&labId=%d&course=%s",
				API_URL, userId, labId, course
		);
		String response = GET(request);
		if (response.equals("error")) {
			System.out.format(
					"Ошибка при получении адреса папки отчёта. request = \"%s\", response = \"%s\"",
					request, response
			);
			throw new IOException("Ошибка при выделении папки для отчёта");
		}
		return response;
	}
	
	/**
	 * Получить состояние лабораторной работы
	 *
	 * @param userId идентификатор пользователя
	 * @param labId идентификатор лабораторной работы
	 * @return состояние лабораторной работы в обёртке {@link LabState}
	 * @throws IOException в случае ошибок ввода-вывода
	 */
	public static LabState getLabState(long userId, long labId) throws IOException {
		String request = String.format(
				"%s?action=getLabState&userId=%d&labId=%d",
				API_URL, userId, labId
		);
		String response = GET(request);
		try {
			return new LabState(Integer.parseInt(response));
		} catch (NumberFormatException nfe) {
			System.out.format(
					"Ошибка при конвертировании результата getLabState в число. request = \"%s\" response = \"%s\"",
					request, response
			);
			nfe.printStackTrace();
			throw new IOException(nfe);
		}
	}
	
	/**
	 * Установить состояние лабораторной работы
	 *
	 * @param userId идентификатор пользователя
	 * @param labId идентификатор лабораторной работы
	 * @param labState новое состояние лабораторной работы в обёртке {@link LabState}
	 * @throws IOException в случае ошибок ввода-вывода
	 */
	public static void setLabState(long userId, long labId, LabState labState) throws IOException {
		String request = String.format(
				"%s?action=setLabState&userId=%d&labId=%d&state=%d",
				API_URL, userId, labId, labState.getValue()
		);
		String response = GET(request);
		if (!response.isEmpty()) {
			System.out.format(
					"API setLabState вернуло не пустой результат. request = \"%s\" response = \"%s\"",
					request, response
			);
			throw new IOException(response);
		}
	}
	
	/**
	 * Сгенерировать задачу и получить её JSON представление
	 *
	 * @param userId идентификатор пользователя
	 * @param labId идентификатор лабораторной работы
	 * @param course код курса
	 * @param extra генерация доп или основной задачи?
	 * @return JSON представление задачи
	 * @throws IOException в случае ошибок ввода-вывода
	 */
	public static Any task(long userId, long labId, String course, boolean extra) throws IOException {
		String request = String.format(
				"%s?action=task&userId=%d&labId=%d&course=%s&extra=%d",
				API_URL, userId, labId, course, extra ? 1 : 0
		);
		return JsonIterator.deserialize(GET(request));
	}
	
}
