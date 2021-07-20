package springboot1522;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Order
public class LoggingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    private ContentCachingRequestWrapper wrapRequest(HttpServletRequest httpServletRequest) {
        if (httpServletRequest instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) httpServletRequest;
        } else {
            return new ContentCachingRequestWrapper(httpServletRequest);
        }
    }

    private ContentCachingResponseWrapper wrapResponse(HttpServletResponse httpServletResponse) {
        if (httpServletResponse instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) httpServletResponse;
        } else {
            return new ContentCachingResponseWrapper(httpServletResponse);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain
            filterChain) throws IOException, ServletException {
        HttpServletRequest wrappedHttpServletRequest = wrapRequest((HttpServletRequest) servletRequest);
        HttpServletResponse wrappedHttpServletResponse = wrapResponse((HttpServletResponse) servletResponse);
        filterChain.doFilter(wrappedHttpServletRequest, wrappedHttpServletResponse);

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("remoteAddress", wrappedHttpServletRequest.getRemoteAddr());
        request.put("contentType", wrappedHttpServletRequest.getContentType());
        request.put("contentLength", wrappedHttpServletRequest.getContentLength());
        request.put("contextPath", wrappedHttpServletRequest.getContextPath());
        request.put("characterEncoding", wrappedHttpServletRequest.getCharacterEncoding());
        request.put("queryString", wrappedHttpServletRequest.getQueryString());
        request.put("requestUri", wrappedHttpServletRequest.getRequestURI());
        request.put("method", wrappedHttpServletRequest.getMethod());
        request.put("parameters", wrappedHttpServletRequest.getParameterMap());

        Enumeration<String> requestHeaderNames = wrappedHttpServletRequest.getHeaderNames();
        Map<String, Object> requestHeaders = new HashMap<>();
        while (requestHeaderNames.hasMoreElements()) {
            String header = requestHeaderNames.nextElement();
            requestHeaders.put(header, wrappedHttpServletRequest.getHeader(header));
        }
        request.put("headers", requestHeaders);

        Map<String, Object> cookies = new HashMap<>();
        Cookie[] requestCookies = wrappedHttpServletRequest.getCookies();
        if (requestCookies != null) {
            for (Cookie cookie : requestCookies) {
                cookies.put("name", cookie.getName());
                cookies.put("value", cookie.getValue());
                cookies.put("comment", cookie.getComment());
                cookies.put("domain", cookie.getDomain());
                cookies.put("maxAge", cookie.getMaxAge());
                cookies.put("path", cookie.getPath());
                cookies.put("secure", cookie.getSecure());
                cookies.put("version", cookie.getVersion());
                cookies.put("isHttpOnly", cookie.isHttpOnly());
            }
            request.put("cookies", cookies);
        }

        try {
            ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(wrappedHttpServletRequest, ContentCachingRequestWrapper.class);
            if (wrapper != null) {
                byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    request.put("payload", new String(buf, 0, buf.length, wrapper.getCharacterEncoding()));
                }
            }
        } catch (Exception e) {
        }

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("statusCode", wrappedHttpServletResponse.getStatus());
        response.put("contentType", wrappedHttpServletResponse.getContentType());
        response.put("characterEncoding", wrappedHttpServletResponse.getCharacterEncoding());

        response.put("headers", wrappedHttpServletResponse.getHeaderNames().stream().collect(Collectors.toMap(header -> header, wrappedHttpServletResponse::getHeader, (a, b) -> b)));

        try {
            ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(wrappedHttpServletResponse, ContentCachingResponseWrapper.class);
            if (wrapper != null) {
                byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    response.put("payload", new String(buf, 0, buf.length, wrapper.getCharacterEncoding()));
                    wrapper.copyBodyToResponse();
                }
            }
        } catch (Exception e) {
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("API Request - " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request) + "\n\nAPI Response" + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
        }

    }

    @Override
    public void destroy() {

    }
}