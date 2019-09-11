package brian.example.boot.rest.config.logging;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
	private final byte[] requestBody;

	public MultiReadHttpServletRequest(HttpServletRequest request) {
		super(request);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(new BufferedInputStream(request.getInputStream()), baos);
			requestBody = baos.toByteArray();
		} catch (IOException e) {
            throw new RuntimeException("Error occured while copying the HttpServletReqest", e); // TODO Change to the proper internal excecption
		}
	}

	@Override
	public ServletInputStream getInputStream() {
		return new MultiReadHttpServletInputStream(new ByteArrayInputStream(requestBody));
	}

	@Override
	public BufferedReader getReader() {
		return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
	}
	
	

	private class MultiReadHttpServletInputStream extends ServletInputStream {
		private final ByteArrayInputStream bis;

		private MultiReadHttpServletInputStream(ByteArrayInputStream bis) {
			this.bis = bis;
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			// Not Used
		}

		@Override
		public int read() throws IOException {
			return bis.read();
		}

	}
}
