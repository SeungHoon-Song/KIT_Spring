package com.koreait.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Setter;

/*②*/
@Component
@Data
public class Restaurant {
	/*③*/
	
	/*onMethod 속성은 생성되는 setChef에 @Autowired 어노테이션을 추가하도록 할 때 사용된다.*/
	/*버전에 따라 onMethod 혹은 onMethod_를 사용하게 된다.*/
	@Setter(onMethod_ = @Autowired)
	private Chef chef;
}
