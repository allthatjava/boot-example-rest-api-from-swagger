package brian.example.boot.rest.config.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

public class LoggingFilter extends DispatcherServlet {

	private static final long serialVersionUID = 6185374582039630926L;

	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (request == null || response == null) { // Filter Out Swagger UI loading request/response
			super.doDispatch(request, response);
		}

		MultiReadHttpServletRequest cachedRequest = new MultiReadHttpServletRequest(request);
		HttpServletResponse cachedResponse = new ContentCachingResponseWrapper(response);

		logRequest(cachedRequest); // Log request params and body
		try {
			super.doDispatch(cachedRequest, cachedResponse); // Proceed to original request/response
		} finally {
			logResponse(cachedResponse); // Log response data
			updateResponse(cachedResponse); // Copy data back to Response
		}
	}

	private void logRequest(final HttpServletRequest request) {

		List<String> headerList = Collections.list(request.getHeaderNames()).stream()
				.map(name -> name + "=" + request.getHeader(name)).collect(Collectors.toList());
		List<String> paramList = Collections.list(request.getParameterNames()).stream()
				.map(name -> name + "=" + request.getParameter(name)).collect(Collectors.toList());
		logger.info(">>> Request {} {}", request.getMethod(), request.getRequestURI());
		logger.info("Parameters ---");
		paramList.forEach(p -> logger.info(p));
		logger.info("Header ---");
		headerList.forEach(h -> logger.info(h));

		logger.info("Body ---");
		logger.info(getRequestData(request));

		logger.info("<<< Request {}", request.getRequestURI());
	}

	private static String getRequestData(final HttpServletRequest request) {

		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			IOUtils.copy(request.getInputStream(), baos);
		} catch (IOException e) {
			throw new RuntimeException("Error occurred while reading body part of the request"); // TODO change to proper internal exception
		}
		return new String(baos.toByteArray(), StandardCharsets.UTF_8);
	}

	private void logResponse(final HttpServletResponse response) {

		ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
				ContentCachingResponseWrapper.class);

		List<String> headerList = response.getHeaderNames().stream().map(name -> name + "=" + response.getHeader(name))
				.collect(Collectors.toList());
		logger.info("[[[ Response");
		logger.info("Header ---");
		headerList.forEach(h -> logger.info(h));

		logger.info("Body ---");
		logger.info(new String(wrapper.getContentAsByteArray()));
		logger.info("]]] Response");
	}

	private void updateResponse(HttpServletResponse response) {
		ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response,
				ContentCachingResponseWrapper.class);
		try {
			responseWrapper.copyBodyToResponse();
		} catch (IOException e) {
			logger.error("IOException occurred during updateResponse", e);
		}
	}
}
