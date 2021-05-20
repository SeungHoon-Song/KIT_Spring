package com.koreait.service;

import java.util.List;

import com.koreait.domain.Criteria;
import com.koreait.domain.ReplyVO;

public interface ReplyService {
	public int register(ReplyVO reply);
	
	public ReplyVO get(Long rno);
	
	public int remove(Long rno);
	
	public int modify(ReplyVO reply);
	
	public List<ReplyVO> getListWithPaging(Criteria cri, Long bno);
}