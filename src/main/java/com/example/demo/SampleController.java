package com.example.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class SampleController {
	/*
	@GetMapping("/")
	@ResponseBody
	public String index() {
		return "index";
	}
*/
	@GetMapping("/hello")
	public void hello(Model model) {
		log.info("hello");
		model.addAttribute("msg", "hello world");
		
		
	}
	
	@GetMapping("/ex/ex1")
	public void ex1(Model model) {
		List<String> list = Arrays.asList("Aaaserfsdfldlf", "B", "c", "d");
		log.info("ex/ex1");
		model.addAttribute("list", list);
	}
	
	// p425 Thymeleaf 특별한 기능 - 인라인 처리
	class SampleDTO {
		private String p1,p2,p3;
		public String getP1() {
			return p1;
		}
		public String getP2() {
			return p2;
		}
		public String getP3() {
			return p3;
		}
	}
	
	@GetMapping("/ex/ex2")
	public void ex2(Model model) {
		log.info("ex/ex2");
		List<String> list = IntStream.range(1, 10).mapToObj(i -> "Data"+i).collect(Collectors.toList());
		
		model.addAttribute("list", list);
		
		Map<String, String> map = new HashMap<>();
		map.put("A", "AAAA");
		map.put("B", "BBBB");
		
		model.addAttribute("map", map);
		
		SampleDTO sampleDTO = new SampleDTO();
		sampleDTO.p1 = "Value -- p1";
		sampleDTO.p1 = "Value -- p2";
		sampleDTO.p1 = "Value -- p3";
		model.addAttribute("dto", sampleDTO);
	}
	
	@GetMapping("/ex/ex3")
	public void ex3(Model model) {
		log.info("ex/ex3");
		
		
		model.addAttribute("arr", new String[] {"AAA", "BBB", "CCC"});
		
		
	}

	@GetMapping("/ex/ex4")
	public void ex4(Model model) {
		log.info("ex/ex4");
		model.addAttribute("arr", new String[] {"AAA", "BBB", "CCC"});
	}


	
	
}
