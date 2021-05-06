package com.koreait.sample;

import org.springframework.stereotype.Component;

import lombok.Data;

/*②*/
@Component //스프링에게 해당 클래스가 스프링에서 관리해야 하는 대상임을 표시
@Data //Lombok의 setter를 생성하는 기능을 사용하기 위함과 생성자, toString()등을 자동으로 생성하기 위함
public class Chef {
}
