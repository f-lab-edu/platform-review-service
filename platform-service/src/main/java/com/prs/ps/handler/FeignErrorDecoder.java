package com.prs.ps.handler;

import com.prs.ps.exception.PlatformCreationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 500:
                if(methodKey.contains("getMemberInfo")){
                    return new PlatformCreationException("요청에 필요한 유저 정보를 찾을 수 없습니다.");
                }
            default:
                return new Exception(response.reason());
        }
    }
}