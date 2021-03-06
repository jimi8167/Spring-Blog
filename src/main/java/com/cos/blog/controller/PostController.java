package com.cos.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cos.blog.model.RespCM;
import com.cos.blog.model.post.dto.RequestUpdateDto;
import com.cos.blog.model.post.dto.RequestWriteDto;
import com.cos.blog.model.user.User;
import com.cos.blog.service.PostService;

@Controller
public class PostController {
	@Autowired
	private PostService postService;
	
	@Autowired
	private HttpSession session;

	@GetMapping({"","/","/post"})
	public String posts(Model model) {
		//view resolver가 관여해야하기 떄문에 @responsebody ㄴ ㄴ 
		

		model.addAttribute("Post", postService.목록보기());
		return"/post/list";
	}
	
	
	@GetMapping("/post/detail/{id}")
	public String post(@PathVariable int id, Model model) {
		model.addAttribute("post", postService.상세보기(id));
		
		return "/post/detail";
	}
	
	@GetMapping("/post/write")
	public String write() {
		
		return "/post/write";
	}
	
	@PostMapping("/post/write")
	public ResponseEntity<?> write(@RequestBody RequestWriteDto dto){
		
		User principal=(User) session.getAttribute("principal");
		dto.setUserId(principal.getId());
		
		int result=postService.글쓰기(dto);
		
		if (result==1) {
			return new ResponseEntity<RespCM>(new RespCM(200, "ok"), HttpStatus.OK);
		}else {
			return new ResponseEntity<RespCM>(new RespCM(400, "fail"), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/post/update/{id}")
	public String update(@PathVariable int id, Model model) {
		
		model.addAttribute("post", postService.수정하기(id));
		return "/post/update";
	}
	
	@DeleteMapping("/post/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		int result=postService.삭제하기(id);
		if (result==1) {
			return new ResponseEntity<RespCM>(new RespCM(200, "ok"), HttpStatus.OK);
		}else if (result==-3) {
			return new ResponseEntity<RespCM>(new RespCM(403, "fail"), HttpStatus.FORBIDDEN);
		}else {
			return new ResponseEntity<RespCM>(new RespCM(400, "fail"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/post/update")
	public ResponseEntity<?> update(@RequestBody RequestUpdateDto dto) {
		int result=postService.수정완료(dto);
		
		if (result==1) {
			return new ResponseEntity<RespCM>(new RespCM(200, "ok"), HttpStatus.OK);
		}else if (result==-3) {
			return new ResponseEntity<RespCM>(new RespCM(403, "fail"), HttpStatus.FORBIDDEN);
		}else {
			return new ResponseEntity<RespCM>(new RespCM(400, "fail"), HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
