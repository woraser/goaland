/**
 * 
 */
$(document).ready(function(){
	
	var detail = new Vue({
	   el: '#detail',
	   data: {
		   detailData : {},
	   },
	   methods: {
		   
	   },
	   filters: {
		   
	   }
	})
	
	var loadDetail = function(){
		$.ajax({
			url : "/iotx/management/data/one",
			data : {
				"id" : $("#iotxId").val(),
				"showAttributes" : "status,serialNo,openTime,installLocation,networkCategory,mac,location,sensorQuantity,alarmQuantity,continueTime,cpu,memory,hardDisk,usedMemoryPer,usedHardDiskPer,usedCpuPer,freeMemory,usedMemory,freeHardDisk,usedHardDisk",
			},
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				detail.detailData = data;
				detail.detailData.status = $.i18n.prop('iotx.'+detail.detailData.status);
				
				$('#cpuPer').radialIndicator({
					barColor: '#87CEEB',
					barWidth: 5,
					initValue: detail.detailData.usedCpuPer * 100,
					roundCorner: true,
					percentage: true,
					radius: 35,
				});
				$('#memoryPer').radialIndicator({
				     barColor: '#87CEEB',
				     barWidth: 5,
				     initValue: detail.detailData.usedMemoryPer * 100,
				     roundCorner: true,
				     percentage: true,
				     radius: 35,
				});
				$('#hardDiskPer').radialIndicator({
				     barColor: '#87CEEB',
				     barWidth: 5,
				     initValue: detail.detailData.usedHardDiskPer * 100,
				     roundCorner: true,
				     percentage: true,
				     radius: 35,
				});
				
			}
		})
	}
	
	//加载detail
	loadDetail()
	
})