package com.koreait.mapper;

import java.util.List;

import com.koreait.domain.BoardAttachVO;

public interface BoardAttachMapper {	//해당 쿼리문이 실행(서비스, 루트, 빈)되면서 인터페이스가 구현됨.
	public void insert(BoardAttachVO vo);
	public void delete(String uuid);
	public void deleteAll(Long bno);
	public List<BoardAttachVO> findByBno(Long bno);
	public List<BoardAttachVO> getOldFiles();
}
