package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.servlet.ModelAndView;

@RestController
public class Connect {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	

	@GetMapping("/te")
	public Object gt(){

		
		ArrayList arr3 = new ArrayList();
		
		String sql3 = "SELECT Article_ID FROM Article";
		String sql2 = "SELECT * FROM Comment WHERE Article_ID = ";
        List<Map<String, Object>> list =  jdbcTemplate.queryForList(sql3);
		int si = list.size();
		
		
		for(int i =0 ; i<si; i++){
			
			List<Map<String, Object>> lists =  jdbcTemplate.queryForList(sql2 + list.get(i).get("Article_ID"));
			arr3.add(lists.size());
			
		}
		Collections.reverse(arr3);
	
		String sql = "SELECT * FROM article ORDER BY Article_ID desc";
		List<Map<String, Object>> List =  jdbcTemplate.queryForList(sql);
		int ListCount = List.size();
		
		
		for(int i = 0 ; i<arr3.size(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			
			List.get(i).put("commentCount", arr3.get(i));
		}
 
		return List;
	}

	@GetMapping("")
	public ModelAndView helloIndex(){

		ArrayList arr2 = new ArrayList();
		ArrayList arr3 = new ArrayList();
		
		String sql3 = "SELECT Article_ID FROM article";
		String sql2 = "SELECT * FROM comment WHERE Article_ID = ";
        List<Map<String, Object>> list =  jdbcTemplate.queryForList(sql3);
		int si = list.size();
		
		
		for(int i =0 ; i<si; i++){
			arr2.add(sql2 + list.get(i).get("Article_ID"));	
			List<Map<String, Object>> lists =  jdbcTemplate.queryForList(sql2 + list.get(i).get("Article_ID"));
			arr3.add(lists.size());
			
		}
		Collections.reverse(arr3);
	
		String sql = "SELECT * FROM article ORDER BY Article_ID desc";
		List<Map<String, Object>> List =  jdbcTemplate.queryForList(sql);
		int ListCount = List.size();
		
		
		for(int i = 0 ; i<arr3.size(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			
			List.get(i).put("commentCount", arr3.get(i));
		}

		int count = List.size();

		ModelAndView model = new ModelAndView("index");

		model.addObject("articleList", List);
		model.addObject("articleCount", count);
		
		
		return model;
	}

	@GetMapping("/cou")
	public List<Map<String, Object>> getCount(){
		String sql = "Select * from article";
		List<Map<String, Object>> List =  jdbcTemplate.queryForList(sql);
		
		return List;
	}
	
	//新增文章至資料庫
	@GetMapping("result")
	public ModelAndView addArticle(@RequestParam String fav_language,@RequestParam String subject,@RequestParam String content) {
		
		String sql_update= "INSERT into article (Article_Subject,Article_Content,Article_Class,Article_Date) VALUES (?,?,?,now())";
		jdbcTemplate.update(sql_update,new Object[] {subject,content,fav_language});

		ModelAndView model = new ModelAndView("redirect:/");
		
		
		return model;
		
	}

	//顯示文章內容
	@GetMapping("artical")
	public ModelAndView Article(@RequestParam String id) {
		
		String sql = "Select * from article where Article_ID = ?";
		List<Map<String, Object>> List =  jdbcTemplate.queryForList(sql,id);
		
		String sqlComment = "Select * from comment where Article_ID = ?";
		List<Map<String, Object>> commentList =  jdbcTemplate.queryForList(sqlComment,id);

		ModelAndView model = new ModelAndView("artical");

		Integer commentCount = commentList.size();

		model.addObject("articleList", List);
		model.addObject("commentList", commentList);
		model.addObject("commentCount", commentCount);
		model.addObject("id", id);

		return model;
		
	}

	//送出留言
	@GetMapping("newCom")
	public ModelAndView newCom(@RequestParam int id,@RequestParam String newComment) {
		
		String sql_update= "INSERT into comment (Comment_Content,Date,Article_ID) VALUES (?,now(),?)";
		jdbcTemplate.update(sql_update,new Object[] {newComment,id});

		ModelAndView model = new ModelAndView("redirect:/artical?id="+id);
		return model;
		
	}

	//在心情板顯示
	@GetMapping("mood")
	public ModelAndView moodIndex(){

		ArrayList arr2 = new ArrayList();
		ArrayList arr3 = new ArrayList();
		
		String sql3 = "SELECT * FROM article WHERE Article_Class = 'mood'";
		
		String sql2 = "SELECT * FROM comment WHERE Article_ID = ";
        List<Map<String, Object>> list =  jdbcTemplate.queryForList(sql3);
		
		for(int i =0 ; i<list.size(); i++){
			arr2.add(sql2 + list.get(i).get("Article_ID"));	
			List<Map<String, Object>> lists =  jdbcTemplate.queryForList(sql2 + list.get(i).get("Article_ID"));
			arr3.add(lists.size());
			
		}
		Collections.reverse(arr3);
		
		String sql = "SELECT * FROM article WHERE Article_Class = 'mood' ORDER BY Article_ID desc";
		List<Map<String, Object>> List =  jdbcTemplate.queryForList(sql);
		int ListCount = List.size();
		
		for(int i = 0 ; i<arr3.size(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			
			List.get(i).put("commentCount", arr3.get(i));
		}
		
		int count = List.size();

		ModelAndView model = new ModelAndView("mood");

		model.addObject("articleList", List);
		model.addObject("articleCount", count);
		
		
		return model;
	}

	//在音樂板顯示
	@GetMapping("music")
	public ModelAndView musicIndex(){

		
		ArrayList arr3 = new ArrayList();
		
		String sql3 = "SELECT * FROM article WHERE Article_Class = 'music'";
		
		String sql2 = "SELECT * FROM comment WHERE Article_ID = ";
        List<Map<String, Object>> list =  jdbcTemplate.queryForList(sql3);
		
		for(int i =0 ; i<list.size(); i++){
			
			List<Map<String, Object>> lists =  jdbcTemplate.queryForList(sql2 + list.get(i).get("Article_ID"));
			arr3.add(lists.size());
			
		}
		Collections.reverse(arr3);
		
		String sql = "SELECT * FROM article WHERE Article_Class = 'music' ORDER BY Article_ID desc";
		List<Map<String, Object>> List =  jdbcTemplate.queryForList(sql);
		
		
		for(int i = 0 ; i<arr3.size(); i++){
			List.get(i).put("commentCount", arr3.get(i));
		}
		
		int count = List.size();

		ModelAndView model = new ModelAndView("music");

		model.addObject("articleList", List);
		model.addObject("articleCount", count);
		
		
		return model;
	}

	//在其他板顯示
	@GetMapping("other")
	public ModelAndView otherIndex(){

		
		ArrayList arr3 = new ArrayList();
		
		String sql3 = "SELECT * FROM article WHERE Article_Class = 'other'";
		
		String sql2 = "SELECT * FROM comment WHERE Article_ID = ";
        List<Map<String, Object>> list =  jdbcTemplate.queryForList(sql3);
		
		for(int i =0 ; i<list.size(); i++){
			
			List<Map<String, Object>> lists =  jdbcTemplate.queryForList(sql2 + list.get(i).get("Article_ID"));
			arr3.add(lists.size());
			
		}
		Collections.reverse(arr3);
		
		String sql = "SELECT * FROM article WHERE Article_Class = 'other' ORDER BY Article_ID desc";
		List<Map<String, Object>> List =  jdbcTemplate.queryForList(sql);
		
		
		for(int i = 0 ; i<arr3.size(); i++){
			
			
			List.get(i).put("commentCount", arr3.get(i));
		}
		
		int count = List.size();

		ModelAndView model = new ModelAndView("other");

		model.addObject("articleList", List);
		model.addObject("articleCount", count);
		
		
		return model;
	}
	

	
	
}
