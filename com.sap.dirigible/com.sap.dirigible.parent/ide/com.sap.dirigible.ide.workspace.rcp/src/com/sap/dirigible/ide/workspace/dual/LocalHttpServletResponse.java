package com.sap.dirigible.ide.workspace.dual;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class LocalHttpServletResponse implements HttpServletResponse {
	
//	private ByteArrayOutputStream baos;
	
	private LocalServletOutputStream sos;
	
	private PrintWriter printWriter; 
	
	public LocalHttpServletResponse(ByteArrayOutputStream baos) {
//		this.baos = baos;
		this.sos = new LocalServletOutputStream(baos);
		this.printWriter = new PrintWriter(baos);
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return this.sos;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return this.printWriter;
	}

	@Override
	public void setCharacterEncoding(String charset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentLength(int len) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentType(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBufferSize(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocale(Locale loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsHeader(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String encodeURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendError(int sc) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeader(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addHeader(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(int sc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(int sc, String sm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	static class LocalServletOutputStream extends ServletOutputStream {

		private ByteArrayOutputStream baos;
		
		LocalServletOutputStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}
		
		@Override
		public void write(int b) throws IOException {
			baos.write(b);
			
		}

		@Override
		public void print(boolean arg0) throws IOException {
			// TODO Auto-generated method stub
			super.print(arg0);
		}

		@Override
		public void print(char c) throws IOException {
			// TODO Auto-generated method stub
			super.print(c);
		}

		@Override
		public void print(double d) throws IOException {
			// TODO Auto-generated method stub
			super.print(d);
		}

		@Override
		public void print(float f) throws IOException {
			// TODO Auto-generated method stub
			super.print(f);
		}

		@Override
		public void print(int i) throws IOException {
			// TODO Auto-generated method stub
			super.print(i);
		}

		@Override
		public void print(long l) throws IOException {
			// TODO Auto-generated method stub
			super.print(l);
		}

		@Override
		public void print(String arg0) throws IOException {
			// TODO Auto-generated method stub
			super.print(arg0);
		}

		@Override
		public void println() throws IOException {
			// TODO Auto-generated method stub
			super.println();
		}

		@Override
		public void println(boolean b) throws IOException {
			// TODO Auto-generated method stub
			super.println(b);
		}

		@Override
		public void println(char c) throws IOException {
			// TODO Auto-generated method stub
			super.println(c);
		}

		@Override
		public void println(double d) throws IOException {
			// TODO Auto-generated method stub
			super.println(d);
		}

		@Override
		public void println(float f) throws IOException {
			// TODO Auto-generated method stub
			super.println(f);
		}

		@Override
		public void println(int i) throws IOException {
			// TODO Auto-generated method stub
			super.println(i);
		}

		@Override
		public void println(long l) throws IOException {
			// TODO Auto-generated method stub
			super.println(l);
		}

		@Override
		public void println(String s) throws IOException {
			// TODO Auto-generated method stub
			super.println(s);
		}
		
		
	}
}
