package com.v02.minback.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v02.minback.model.param.AccessLogParam;
import com.v02.minback.service.front.ThreadLocalAccessLogService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter implements Filter {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ThreadLocalAccessLogService threadLocalAccessLogService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper((HttpServletResponse) response);
        LocalDateTime requestAt = LocalDateTime.now();

        AccessLogParam accessLogParam = new AccessLogParam(
                req.getMethod(),
                req.getRequestURI(),
                req.getQueryString(),
                objectMapper.writeValueAsString(req.getParameterMap()),
                req.getReader().lines().reduce("", (acc, line) -> acc + line),
                Collections.list(req.getHeaderNames()).stream()
                        .collect(Collectors.toMap(h -> h, req::getHeader))
                        .toString(),
                req.getHeader("User-Agent"),
                req.getHeader("Referer"),
                Optional.ofNullable(req.getHeader("X-Forwarded-For")).orElse(req.getRemoteAddr()),
                req.getHeader("Host"),
                req.getHeader("Authorization")
        );

        try {

            threadLocalAccessLogService.putAccessLog(accessLogParam);
            chain.doFilter(req, res);
            LocalDateTime responseAt = LocalDateTime.now();

            final AccessLogParam accessLogParamAfter = threadLocalAccessLogService.getAccessLog();

            accessLogParamAfter.setResponseAt(responseAt);
            accessLogParamAfter.setRequestBody(new String(res.getContentAsByteArray()));
            accessLogParamAfter.setStatus(res.getStatus() >= 400 ? "ERROR" : "SUCCESS");
            accessLogParamAfter.setStatusCode(res.getStatus());
            accessLogParamAfter.setElapsed(Duration.between(requestAt, responseAt).toMillis());

            rabbitTemplate.convertAndSend("bank.exchange", "bank.log.access", accessLogParamAfter);

            res.copyBodyToResponse();
        } finally {
            threadLocalAccessLogService.removeThreadLocal();
        }

    }

}