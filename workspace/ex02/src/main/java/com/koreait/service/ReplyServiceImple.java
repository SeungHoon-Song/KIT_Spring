package com.koreait.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koreait.domain.Criteria;
import com.koreait.domain.ReplyPageDTO;
import com.koreait.domain.ReplyVO;
import com.koreait.mapper.BoardMapper;
import com.koreait.mapper.ReplyMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@AllArgsConstructor
public class ReplyServiceImple implements ReplyService {

	private ReplyMapper mapper;
	private BoardMapper board_mapper;
	
	@Transactional
	@Override
	public int register(ReplyVO reply) {
		log.info("register........" + reply);
		board_mapper.updateReplyCnt(reply.getBno(), 1);
		return mapper.insert(reply);
	}

	@Override
	public ReplyVO get(Long rno) {
		log.info("get........" + rno);
		return mapper.read(rno);
	}

	@Transactional
	@Override
	public int remove(Long rno) {
		log.info("remove........" + rno);
		
		board_mapper.updateReplyCnt(mapper.read(rno).getBno(), -1);
		return mapper.delete(rno);
	}

	@Override
	public int modify(ReplyVO reply) {
		log.info("modify........" + reply);
		return mapper.update(reply);
	}

	@Override
	public ReplyPageDTO getListWithPaging(Criteria cri, Long bno) {
		log.info("get Reply List of a Board........" + bno);
		//댓글을 구현하기 위해 필요한 두 개의 요소를 ReplyPageDTO에 전달한다.
		return new ReplyPageDTO(mapper.getTotal(bno), mapper.getListWithPaging(cri, bno));
	}

}
