package com.app.api;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface APIDao {
	List<Object> getOrders(@Param("accession_number")String accession_number);
	void newOrder(@Param("orderMap")Map<String,Object> orderMap);
	void updateOrder(@Param("orderMap")Map<String,Object> orderMap);
	void saveQRCode(@Param("accession_number")String accession_number,@Param("qrcode")String qrcode);
	void updateExamStatus(@Param("qrcode")String qrcode,@Param("examStatus")String examStatus,@Param("status")String status,@Param("accession_number")String accession_number);
	

	void newReport(@Param("qrcode")String qrcode,
			@Param("file_address")String file_address,
			@Param("report_id")String report_id);
	void reportCA(@Param("report_id")String report_id);
	
	void updatePayStatus(@Param("qrcode")String qrcode,@Param("pay_status")String payStatus);
}
