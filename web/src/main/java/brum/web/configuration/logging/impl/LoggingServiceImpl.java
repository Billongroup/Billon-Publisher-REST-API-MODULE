package brum.web.configuration.logging.impl;

import brum.common.logger.BrumLogger;
import brum.common.logger.factory.BrumLoggerFactory;
import brum.web.configuration.logging.LoggingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoggingServiceImpl implements LoggingService {
    @Value("${logging.logRequestsEnabled}")
    private boolean logRequestsEnabled;
    @Value("${logging.logResponsesEnabled}")
    private boolean logResponsesEnabled;

    private final BrumLogger log = BrumLoggerFactory.create(this.getClass());

    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        if (!logRequestsEnabled) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameters = buildParametersMap(httpServletRequest);

        stringBuilder.append("REQUEST ");
        stringBuilder.append("\n\tmethod=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("\n\tpath=[").append(httpServletRequest.getRequestURI()).append("] ");
        stringBuilder.append("\n\theaders=[").append(buildHeadersMap(httpServletRequest)).append("] ");

        if (!parameters.isEmpty()) {
            stringBuilder.append("\n\tparameters=[").append(parameters).append("] ");
        }

        if (body != null) {
            stringBuilder.append("\n\tbody=[").append(body).append("]");
        }

        log.info(stringBuilder.toString());
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        if (!logResponsesEnabled) {
            return;
        }

        String stringBuilder = "RESPONSE " +
                "\n\tmethod=[" + httpServletRequest.getMethod() + "] " +
                "\n\tpath=[" + httpServletRequest.getRequestURI() + "] " +
                "\n\tresponseHeaders=[" + buildHeadersMap(httpServletResponse) + "] " +
                "\n\tresponseBody=[" + body + "] ";

        log.info(stringBuilder);
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            appendHeader(key, request.getHeader(key), map);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            appendHeader(header, response.getHeader(header), map);
        }
        return map;
    }

    private void appendHeader(String headerName, String headerValue, Map<String, String> headersMap) {
        headersMap.put(headerName, headerValue);
    }
}