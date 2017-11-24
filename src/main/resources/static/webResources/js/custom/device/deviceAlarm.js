/**
 * 
 */
$(document).ready(function() {
	var deviceAlarm = new Vue({
	   el: '#deviceAlarm',
	   data: {
		   alarmDatas : [],
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
			url : "/alarmData/management/data/REMOTE",
			data : {
				'showAttributes':'sensor.dust.iotx.serialNo,collectTime,level',
				'sensor.dust.device.serialNo' : $("#deviceSN").val(),
				'page' : 0,
				'size' : 8,
				'sort' : 'collectTime,desc',
			},
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				deviceAlarm.alarmDatas = data.content;
				$.each(deviceAlarm.alarmDatas,function(){
					this.level = $.i18n.prop('iotxData.' + this.level)
				})
			}
		})
	}
	
	loadContent();
	
})