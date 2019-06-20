package com.mishin870.labgenreloaded.api.java.shadow;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

/**
 * Теневой System.out<br>
 * См. также {@link ShadowType}
 */
public class PrintStreamShadow extends PrintStream {
	private final PrintStream out;
	private OutputHandler handler;
	private StringBuilder output = new StringBuilder();
	
	public PrintStreamShadow(PrintStream out) {
		this(out, null);
	}
	
	public PrintStreamShadow(PrintStream out, OutputHandler handler) {
		super(out);
		this.out = out;
		this.handler = handler;
	}
	
	public OutputHandler getHandler() {
		return handler;
	}
	
	public void setHandler(OutputHandler handler) {
		this.handler = handler;
	}
	
	private void redirectOutput(String text, OutputType outType) {
		this.output.append(text);
		if (this.handler != null) {
			this.handler.outputCallback(text, false, outType, this.out);
		}
	}
	
	private void redirectOutputLn(String text, OutputType outType) {
		this.output.append(text).append('\n');
		if (this.handler != null) {
			this.handler.outputCallback(text, true, outType, this.out);
		}
	}
	
	@Override
	public void close() {
		throw new ShadowNotImplementedException(ShadowType.PRINT_STREAM, "close()");
	}
	
	@Override
	public void write(int b) {
		throw new ShadowNotImplementedException(ShadowType.PRINT_STREAM, "write(int)");
	}
	
	@Override
	public void write(byte[] buf, int off, int len) {
		throw new ShadowNotImplementedException(ShadowType.PRINT_STREAM, "write(byte[], int, int)");
	}
	
	@Override
	public void print(boolean b) {
		redirectOutput(String.valueOf(b), OutputType.BOOLEAN);
	}
	
	@Override
	public void print(char c) {
		redirectOutput(String.valueOf(c), OutputType.CHAR);
	}
	
	@Override
	public void print(int i) {
		redirectOutput(String.valueOf(i), OutputType.INT);
	}
	
	@Override
	public void print(long l) {
		redirectOutput(String.valueOf(l), OutputType.LONG);
	}
	
	@Override
	public void print(float f) {
		redirectOutput(String.valueOf(f), OutputType.FLOAT);
	}
	
	@Override
	public void print(double d) {
		redirectOutput(String.valueOf(d), OutputType.DOUBLE);
	}
	
	@Override
	public void print(char[] s) {
		redirectOutput(String.valueOf(s), OutputType.CHAR_ARRAY);
	}
	
	@Override
	public void print(String s) {
		redirectOutput(String.valueOf(s), OutputType.STRING);
	}
	
	@Override
	public void print(Object obj) {
		redirectOutput(String.valueOf(obj), OutputType.OBJECT);
	}
	
	@Override
	public void println() {
		redirectOutputLn("", OutputType.NEW_LINE);
	}
	
	@Override
	public void println(boolean x) {
		redirectOutputLn(String.valueOf(x), OutputType.BOOLEAN);
	}
	
	@Override
	public void println(char x) {
		redirectOutputLn(String.valueOf(x), OutputType.CHAR);
	}
	
	@Override
	public void println(int x) {
		redirectOutputLn(String.valueOf(x), OutputType.INT);
	}
	
	@Override
	public void println(long x) {
		redirectOutputLn(String.valueOf(x), OutputType.LONG);
	}
	
	@Override
	public void println(float x) {
		redirectOutputLn(String.valueOf(x), OutputType.FLOAT);
	}
	
	@Override
	public void println(double x) {
		redirectOutputLn(String.valueOf(x), OutputType.DOUBLE);
	}
	
	@Override
	public void println(char[] x) {
		redirectOutputLn(String.valueOf(x), OutputType.CHAR_ARRAY);
	}
	
	@Override
	public void println(String x) {
		redirectOutputLn(String.valueOf(x), OutputType.STRING);
	}
	
	@Override
	public void println(Object x) {
		redirectOutputLn(String.valueOf(x), OutputType.OBJECT);
	}
	
	@Override
	public PrintStream printf(String format, Object... args) {
		return format(format, args);
	}
	
	@Override
	public PrintStream printf(Locale l, String format, Object... args) {
		throw new ShadowNotImplementedException(ShadowType.PRINT_STREAM, "printf(Locale, String, Object...)");
	}
	
	@Override
	public PrintStream format(String format, Object... args) {
		redirectOutput(String.format(format, args), OutputType.FORMAT_OBJECTS_ARGS);
		return this;
	}
	
	@Override
	public PrintStream format(Locale l, String format, Object... args) {
		throw new ShadowNotImplementedException(ShadowType.PRINT_STREAM, "format(Locale, String, Object...)");
	}
	
	@Override
	public PrintStream append(CharSequence csq) {
		print(csq == null ? "null" : csq.toString());
		return this;
	}
	
	@Override
	public PrintStream append(CharSequence csq, int start, int end) {
		print(csq == null ? "null" : csq.subSequence(start, end));
		return this;
	}
	
	@Override
	public PrintStream append(char c) {
		print(c);
		return this;
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		throw new ShadowNotImplementedException(ShadowType.PRINT_STREAM, "write(byte[])");
	}
	
	/**
	 * @return весь собранный вывод пользовательского приложения и
	 *          очищает собранного вывода
	 */
	public String getOutput() {
		String result = output.toString();
		output.setLength(0);
		return result;
	}
}
