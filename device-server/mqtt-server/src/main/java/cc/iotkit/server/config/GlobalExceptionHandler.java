package cc.iotkit.server.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RequestResult handleException(Exception e, HttpServletResponse response) {
        log.error("handler exception", e);
        if(e.getMessage().contains("Unauthorized")){
            response.setStatus(403);
            return new RequestResult("403", "没有权限");
        }
        response.setStatus(500);
        return new RequestResult("500", e.getMessage());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class RequestResult {

        private String code;

        private String message;

    }

}


