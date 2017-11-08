/**
 * 
 */
$(document).ready(function() {
	var size = 7

	var alarmDatas = new Vue({
		el : '#alarmDataTop',
		data : {
			sensors : [],
		},
		methods : {

		},
		filters : {

		}
	})

	var params = {
		'page' : 0,
		'size' : size,
		'alarmQuantity' : [ 0, -1 ],
		'sort' : 'unConfirmAlarmQuantity,desc',
		'collectTime' : null,
		'showAttributes' : 'serialNo,dust.iotx.installLocation,unConfirmAlarmQuantity',
	}

	// 从服务器获取传感器的告警数据
	function getAlarmData() {
		$.ajax({
			url : '/sensor/management/data/REMOTE',
			data : $.param(params, true),
			type : 'get',
			dataType : 'json',
			success : function(datas) {
				alarmDatas.sensors = datas.content
			},
			error : function(data) {
				warning('节点分布加载失败，请联系管理员或刷新页面重试');
			}
		});
	}

	getAlarmData();

})