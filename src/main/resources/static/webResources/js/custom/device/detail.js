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
	
})