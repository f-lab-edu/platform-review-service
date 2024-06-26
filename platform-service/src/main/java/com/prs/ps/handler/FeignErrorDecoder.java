package com.prs.ps.handler;

import com.prs.ps.exception.PlatformAccessDeniedException;
import com.prs.ps.exception.PlatformCreationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final Map<String, Class<? extends Exception>> exceptionMap500 = new HashMap<>();
    private final Map<String, Class<? extends Exception>> exceptionMap400 = new HashMap<>();

    private final Map<Integer, Map<String, Class<? extends Exception>>> errorCodeMap = new HashMap<>();


    {
        exceptionMap500.put("Member", PlatformCreationException.class);
        exceptionMap400.put("Member", PlatformAccessDeniedException.class);

        errorCodeMap.put(500, exceptionMap500);
        errorCodeMap.put(400, exceptionMap400);
    }


    @Override
    public Exception decode(String methodKey, Response response) {

        Map<String, Class<? extends Exception>> exceptionMap = errorCodeMap.getOrDefault(
            response.status(), new HashMap<>());

        for (String key : exceptionMap.keySet()) {
            if (methodKey.contains(key)) {
                Class<? extends Exception> exceptionClass = exceptionMap.get(key);
                try {
                    return exceptionClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    break;
                }

            }

        }

        return new Exception(response.reason());
    }
}