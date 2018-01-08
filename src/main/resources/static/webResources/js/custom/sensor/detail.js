/**
 * 
 */
$(document).ready(function(){
	
	var detail = new Vue({
	   el: '#detail',
	   data: {
		   detailData : {},
		   deviceId : null,
		   nowReading : 0,
	   },
	   methods: {
		   
	   },
	   filters: {
		   
	   }
	})
	
	var loadDetail = function(){
		$.ajax({
			url : "/sensor/management/data/one",
			data : {
				"serialNo" : $("#serialNo").val(),
				"showAttributes" : "serialNo,dust.iotx.id,dust.iotx.serialNo,dust.device.serialNo,alarmQuantity,maxVal,minVal,unit,isWorked",
			},
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				detail.detailData = data;
				$.ajax({
					url : "/device/management/data/one",
					data : {
						"serialNo" : data['dust.device.serialNo'],
						"showAttributes" : "id",
					},
					type : 'get',
					dataType : 'json',
					success : function( result ) {
						detail.deviceId = result.id;
					}
				})
			}
		})
	}
	
	//加载detail
	loadDetail();
	
	
	//线图
	var dynamicData = echarts.init(document.getElementById('loy-tab'));
	
	//图表显示提示信息
	dynamicData.showLoading();
	
	//发请求从后台数据库中取动态数据
	function getDynamicData(){
		$.ajax({
			type:"get",
			url:'/iotxData/dynamicData',
			data : {
				'sensorSN' : $("#serialNo").val(),
				'showAttributes':'collectTime,val',
			},
			async:true,
			success:function(data){
				if(data != null && data.content != null){
					//设置当前读数
					var size = data.content.length
					detail.nowReading = data.content[size-1].val
					
					//x,y轴数据
					var x_data;
					var y_data;
					var showDatas = [];
					var allDatas = [];
				
					//遍历data,取出val和collect_time
					$.each(data.content,function(i,value){
						x_data = this.collectTime;
						y_data = this.val;
						allDatas.push(y_data);
						showDatas.push({name:x_data.toString(),value:[x_data,y_data]})
					})

					var max_y_data = Math.max.apply(null,allDatas);
                    var min_y_data = Math.min.apply(null,allDatas);
					
					// 填入数据
				    dynamicData.setOption({
				        series: [{
				            data: showDatas
				        }],
                        yAxis:{
				        	max : max_y_data,
							min : min_y_data,
						}
				    });
				    
				}
			},
			error:function(){
				alert('出错了!')
			},
		});		
	}
	
	var option_dynamicData = {
		    title: {
		        text: ''
		    },
		    grid: {
                left: '10%',
                right: '10%',
                top: '16%',
                bottom: '6%',
                containLabel: true
            },
		    tooltip: {
		        trigger: 'axis',
		        formatter: function (params) {
		        	 param = params[0];
		             return param.name + ' : ' + param.value[1];
		        },
		        axisPointer: {
		            animation: false
		        }
		    },
		    xAxis: {
		        type: 'time',
		        splitLine: {
		            show: false
		        }
		    },
		    yAxis: {
		        type: 'value',
		        boundaryGap: [0, '50%'],
                splitLine: {
		            show: false
		        }
		    },
		    series: [{
		        name: 'dynamicData',
		        type: 'line',
				symbol: 'none',
		    }]
		};
	getDynamicData();
	dynamicData.hideLoading();
	dynamicData.setOption(option_dynamicData, true);
	
	//定时任务
	setInterval(function () {

		getDynamicData();
		
	}, 5000);
	
})