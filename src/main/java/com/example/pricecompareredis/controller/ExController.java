package com.example.pricecompareredis.controller;

import com.example.pricecompareredis.vo.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
    Redis에서 발생할 수 있는 예외 상황 중 중요한 부분은 데이터가 존재하지 않는 경우다.
    이를 처리하기 위해 ControllerAdvice를 사용하여 ExController를 구현했다.
    이 클래스는 NotFoundException이 발생했을 때 이를 잡아, 클라이언트에게 적절한 오류 메시지와 함께 HTTP 상태 코드를 반환하는 역할을 한다.
    이렇게 전역 예외 처리를 통해, 개별 컨트롤러에서 중복된 예외 처리 코드를 작성하지 않고도 예외 발생 시 일관된 처리가 가능해진다.
*/

// @ControllerAdvice를 사용하여 애플리케이션 전역에서 발생하는 특정 예외(NotFoundException)를 잡아내고 일관된 방식으로 처리한다.
@ControllerAdvice
@Slf4j
public class ExController {


//    @ExceptionHandler({RuntimeException.class})
//    public ResponseEntity<Object> BadRequestException(final RuntimeException ex) {
//        return ResponseEntity.badRequest().body(ex.getMessage());
//    }

//    @ExceptionHandler({ xxxx.class })
//    public ResponseEntity<Object> EveryException(final Exception ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @ExceptionHandler({ Exception.class })
//    public ResponseEntity<Object> EveryException(final Exception ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    // @ExceptionHandler 어노테이션은 NotFoundException이 발생했을 때 이를 처리하는 메서드를 정의한다.
    // 여기서는 (NotFoundExceptionResponse)를 지정한다. 이 메서드는 예외의 상세 메시지와 HTTP 상태 코드를 포함한 응답을 생성해 반환한다.
    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Object> NotFoundExceptionResponse(NotFoundException ex) {
        return new ResponseEntity<>(ex.getErrmsg(), ex.getHttpStatus());
    }
}

