package kr.erp.mapper;

import java.util.List;

import kr.erp.vo.ReportResultVO;

public interface ReportResultMapper {
	public void add(ReportResultVO vo); // 입력 
	
	public int delete (Long idx); // 삭제 
	
	public List<ReportResultVO> get(ReportResultVO vo); // 상세 
}
