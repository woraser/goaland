/**
 * 
 */
$(document).ready(function() {
	var detail = new Vue({
	   el: '#detail',
	   data: {
		   detailData : {},
	   },
	   methods: {
		   
	   },
	   filters: {
		  yearFormat: function (value) {
			  return value.split(" ")[0];
		  },
		  timeFormat: function (value) {
			  return value.split(" ")[1];
		  }
	   }
	})
	
	// 加载detail数据
	var loadContent = function(){
		$.ajax({
			url : "/device/management/data/one",
			data : {
				'showAttributes':'project.number,project.name,project.location,productName,productNo,productSpecifications,commissioningTime,serialNo',
				'id':$("#deviceId").val(),
			},
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				detail.detailData = data;
			}
		})
	}
	
	loadContent();
	
	//上传
	 $("#upload").click(function(){
		 var func=function(){
			 if($("#documentForm").valid()){
				 $("#documentForm").submit();
				 return true;
			 }else{
				 return false;
			 }
		 };
		 createModalPage("文档上传","/device/document/upload/"+$("#deviceId").val()+"/view",func); 
	 });
	 
	 $("#viewDocument").click(function(){
		 window.location.href="/device/technologyDocument/manage/"+$("#deviceId").val()+"/view"
	 });
	
})