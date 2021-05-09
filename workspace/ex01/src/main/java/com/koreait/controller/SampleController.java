package com.koreait.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.koreait.domain.SampleDTO;

import lombok.extern.log4j.Log4j;

@Controller	//자동으로 스프링의 객체(Bean)로 등록된다.
@RequestMapping("/sample/*")	//현재 클래스의 모든 메소드들의 기본 경로 설정(예 : /sample/aaa, /sample/bbb 등)
@Log4j
public class SampleController {
	@RequestMapping("")	//GET, POST 상관없이 Controller 로직을 수행한다면 method 옵션을 사용하지 않는다.
	public void basic() {
		log.info("basic..........");
	}
	
	@RequestMapping(value="/basic", method= {RequestMethod.GET, RequestMethod.POST})
	public void basicGet() {
		log.info("basic get..........");
	}
	
	@GetMapping("/basicOnlyGet")
	public void basicGet2() {
		log.info("basic get only get............");
	}
	
	//Controller는 파라미터 타입에 따라 자동으로 매핑된다.
	@GetMapping("/ex01")
	public String ex01(SampleDTO dto) {
		log.info(dto);
		return "ex01";
	}
	
	@GetMapping("/ex02")
	public String ex02(@RequestParam("data1") String name,@RequestParam("data2") int age, Model model) {
		log.info("name : " + name);
		log.info("age : " + age);
		model.addAttribute("name", name);
		model.addAttribute("age", age);
		return "ex02";
	}
	
	@GetMapping("/ex03")
	public String ex03(@RequestParam("data") ArrayList<String> datas, Model model) {
		log.info("datas : " + datas);
		model.addAttribute("datas", datas);
		return "ex03";
	}
	
	//배열에 파라미터 담기
	@GetMapping("/ex04")
	public String ex04(@RequestParam("data") String[] datas, Model model) {
		log.info("datas : " + Arrays.toString(datas));
		model.addAttribute("datas", datas);
		return "ex04";
	}
	
	@GetMapping("/ex05")
	public /*String*/ void ex05(SampleDTO dto, @ModelAttribute("gender") String gender) {
		log.info(dto);
		log.info("gender : " + gender);
//		return "sample/ex05";
	}
}
