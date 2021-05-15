package com.koreait.domain;

import lombok.Data;

@Data
public class ReplyVO {
	/*RNO NUMBER(10),
	BNO NUMBER(10) NOT NULL,
	REPLY VARCHAR2(1000) NOT NULL,
	REPLYER VARCHAR2(100) NOT NULL,
	REPLYDATE DATE DEFAULT SYSDATE,
	UPDATEDATE DATE DEFAULT SYSDATE*/
	
	private Long rno;
	private Long bno;
	private String reply;
	private String replyer;
	private String replyDate;
	private String updatedate;
}
