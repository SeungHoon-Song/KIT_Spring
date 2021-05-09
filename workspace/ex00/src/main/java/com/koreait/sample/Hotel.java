package com.koreait.sample;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Component
@Getter
@ToString
//@AllArgsConstructor	//인스턴스 변수로 선언된 모든 것을 파라미터로 받는 생성자를 작성한다.
@RequiredArgsConstructor //특정 변수에 대해서만 생성자를 작성할 때 작성한다, @NonNull이나 final이 붙은 인스턴스 변수에 대한 생성자를 만들어 낸다.
public class Hotel {
	@NonNull
	private Chef chef;

//	public Hotel(Chef chef) {
//		super();
//		this.chef = chef;
//	}
}




















