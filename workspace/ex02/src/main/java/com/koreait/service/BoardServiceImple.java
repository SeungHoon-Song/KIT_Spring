package com.koreait.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.koreait.domain.BoardVO;
import com.koreait.domain.Criteria;
import com.koreait.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@AllArgsConstructor
public class BoardServiceImple implements BoardService {

	//@AllArgsConstructor로  @Setter 사용X
	private BoardMapper mapper;
	
	@Override
	public void register(BoardVO board) {
		log.info("register........." + board);
		mapper.insertSelectKey_bno(board);
	}

	@Override
	public BoardVO get(Long bno) {
		log.info("get............" + bno);
		return mapper.read(bno);
	}

	@Override
	public boolean modify(BoardVO board) {
		log.info("modify............" + board);
		return mapper.update(board) == 1;
	}

	@Override
	public boolean remove(Long bno) {
		log.info("remove............" + bno);
		return mapper.delete(bno) == 1;
	}

	@Override
	public List<BoardVO> getList() {
		log.info("getList............");
		return mapper.getList();
	}
	
	@Override
	public List<BoardVO> getList(Criteria cri) {
		log.info("getList with criteria............" + cri);
		return mapper.getListWithPaging(cri);
	}
	
	@Override
	public int getTotal() {
		return mapper.getTotal();
	}
}
