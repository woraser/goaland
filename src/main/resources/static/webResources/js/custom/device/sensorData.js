/**
 * 
 */
$(document).ready(function(){
	var sensorSN = "2017010101_40020";

	// 线图
	var dynamicData = echarts.init(document.getElementById('charts'));

	// 图表显示提示信息
	dynamicData.showLoading();

	// 发请求从后台数据库中取动态数据
	function getDynamicData(serialNo) {
		$.ajax({
			type : "get",
			url : '/iotxData/dynamicData',
			data : {
				'sensorSN' : serialNo,
				'showAttributes' : 'collectTime,val',
			},
			async : true,
			success : function(data) {
				if (data != null && data.content != null) {
					// 设置当前读数
					var size = data.content.length
					detail.nowReading = data.content[size - 1].val

					// x,y轴数据
					var x_data;
					var y_data;
					var showDatas = [];

					// 遍历data,取出val和collect_time
					$.each(data.content, function(i, value) {
						x_data = this.collectTime;
						y_data = this.val;
						showDatas.push({
							name : x_data.toString(),
							value : [ x_data, y_data ]
						})
					})

					// 填入数据
					dynamicData.setOption({
						series : [ {
							data : showDatas
						} ]
					});

				}
			},
			error : function() {
				alert('出错了!')
			},
		});
	}

	var option_dynamicData = {
		title : {
			text : ''
		},
		grid: {
            left: '10%',
            right: '10%',
            top: '16%',
            bottom: '6%',
            containLabel: true
        },
		tooltip : {
			trigger : 'axis',
			formatter : function(params) {
				param = params[0];
				return param.name + ' : ' + param.value[1];
			},
			axisPointer : {
				animation : false
			}
		},
		xAxis : {
			type : 'time',
			splitLine : {
				show : false
			}
		},
		yAxis : {
			type : 'value',
			boundaryGap : [ 0, '50%' ],
			splitLine : {
				show : false
			}
		},
		series : [ {
			name : 'dynamicData',
			type : 'line',
		} ]
	};
	getDynamicData();
	dynamicData.hideLoading();
	dynamicData.setOption(option_dynamicData, true);

	// 定时任务
	setInterval(function() {

		if (sensorSN != null) {
			getDynamicData(sensorSN);
		}

	}, 5000);
})
