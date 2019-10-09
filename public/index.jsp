<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
<base href="<%=basePath%>">
<script type="text/javascript" src="static/jquery.min.js">
</script>
<script type="text/javascript">
	function newOrder(){
		alert($("#newOrderData").val());
		$.ajax({url:"/api/newOrder",data:$("#newOrderData").val(),type:"POST",
		contentType:"application/json;charset=utf-8",
		success:function(r){alert(r);$("#deleteOrderData").val(r)}})
	}
	function udpateOrder(){
		alert($("#udpateOrderData").val());
		$.ajax({url:"/api/updateOrder",data:$("#udpateOrderData").val(),type:"POST",
		contentType:"application/json;charset=utf-8",
		success:function(r){alert(r)}})
	}
	function deleteOrder(){
		alert($("#deleteOrderData").val());
		$.ajax({url:"/api/deleteOrder?qrcode="+$("#deleteOrderData").val(),type:"GET",success:function(r){alert(r)}})
	}
	function examed(){
		alert($("#deleteOrderData").val());
		$.ajax({url:"/api/examed?qrcode="+$("#deleteOrderData").val(),type:"GET",success:function(r){alert(r)}})
	}function examedCA(){
		alert($("#deleteOrderData").val());
		$.ajax({url:"/api/examedCA?qrcode="+$("#deleteOrderData").val(),type:"GET",success:function(r){alert(r)}})
	}function releaseReport(){
		alert("releaseReport,qrcode="+$("#deleteOrderData").val());
		$.ajax({url:"/api/releaseReport?qrcode="+$("#deleteOrderData").val()+"&file_address="+$("#file_address").val(),
			type:"GET",
			success:function(r){
				$("#report_id").val(r);
				alert("releaseReport return:"+r);}
			})
	}function recallReport(){
		alert($("#report_id").val());
		$.ajax({url:"/api/recallReport?report_id="+$("#report_id").val(),type:"GET",success:function(r){alert("recallReport return:"+r)}})
	}function payStatus(){
		alert($("#deleteOrderData").val());
		$.ajax({url:"/api/notifyUpload?qrcode="+$("#deleteOrderData").val()+"&pay_status="+$("#pay_status").val(),
		type:"GET",success:function(r){alert("payStatus return:"+r)}})
	}function getQR(){
		alert($("#deleteOrderData").val());
		
		$.ajax({url:"/api/getQR?qr_code_value="+$("#deleteOrderData").val(),
		type:"GET",success:function(r){alert("getQR return:"+r)}});
		$("#QRIMG").attr("src","/api/getQR?qr_code_value="+$("#deleteOrderData").val());
	}
</script>
<title>TestAPI</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="static/demo.css">


</head>
<body>
<textarea id="newOrderData" cols="120" rows="10">
{
"hospital":"100000","department_mode":"放射科","department_src":"内科",
	"patient_name":"郭志洋","patient_gender":"女","patient_birthday":"1980-01-01",
	"patient_age":"63","patient_id":"81078125","study_date":"2018-10-12",
	"study_time":"09:23:21","study_modality":"门诊CT1","procedure_step":"NA",
	"accession_number":"00171125112066","inpatient_number":"700172","outpatient_number":"10028871123",
	"study_instance_uid":"NA","study_source":"门诊","schedule_time":"2018-11-11 14:30--15:00"
}
</textarea><br>
<button onclick="newOrder()" value="Submit Data">Test New Order</button><br>

<textarea id="udpateOrderData" cols="120" rows="10">
{
"hospital":"100000","department_mode":"放射科","department_src":"内科",
	"patient_name":"郭志洋","patient_gender":"女","patient_birthday":"1980-01-01",
	"patient_age":"63","patient_id":"81078125","study_date":"2018-10-12",
	"study_time":"09:23:21","study_modality":"门诊CT1","procedure_step":"NA",
	"accession_number":"00171125112066","inpatient_number":"700172","outpatient_number":"10028871123",
	"study_instance_uid":"NA","study_source":"门诊","qr_code_value":"","schedule_time":"2018-11-11 14:30--15:00"
}
</textarea><br>
<button onclick="udpateOrder()" value="Submit Data">Test Update Order</button><br>

qrcode:<br>
<textarea id="deleteOrderData" cols="120" rows="1"></textarea><br>
<button onclick="deleteOrder()" value="Submit Data">Test Delete Order</button>
<button onclick="examed()" value="Submit Data">Test Examed</button>
<button onclick="examedCA()" value="Submit Data">Test Examed Cancel</button><br>
pdf:<br><textarea id="file_address" cols="120" rows="1">ftp://ftprazor:razorftp@192.168.8.102/report/20180801/19.pdf</textarea><br>
<button onclick="releaseReport()" value="Submit Data">Test releaseReport</button><br>
report_id:<br><textarea id="report_id" cols="120" rows="1"></textarea><br>
<button onclick="recallReport()" value="Submit Data">Test recallReport</button><br>
pay_status:<br><textarea id="pay_status" cols="120" rows="1">Y</textarea><br>
<button onclick="payStatus()" value="Submit Data">Test Pay</button>
<button onclick="getQR()" value="Submit Data">QR</button><br>
<img id="QRIMG" src=""></img>
</body>
