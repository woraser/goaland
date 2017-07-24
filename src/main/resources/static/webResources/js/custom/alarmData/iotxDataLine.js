/**
 * 
 */
$(document).ready(function(){
		
		var dynamicData = echarts.init(document.getElementById('iotxDataLine'));
		
		var showDatas = [];
		
		var iotxSN;
		var sensorSN;
		var category;
		var timeUnit;
		var maxVal;
		var minVal;
		
		function voluationAndStart(iotxSN_,sensorSN_,category_,timeUnit_,maxVal_,minVal_){
			iotxSN = iotxSN_;
			sensorSN = sensorSN_;
			category = category_;
			timeUnit = timeUnit_;
			maxVal = maxVal_;
			minVal = minVal_;
		
			//发请求从后台数据库中取动态数据
			function getDynamicData(){
				dynamicData.showLoading();
				$.ajax({
					type:"get",
					url:'/iotxData/dynamicData',
					data : {
						'iotxSN' : iotxSN,
						'sensorSN' : sensorSN,
						'category' : category,
						'timeUnit' : timeUnit,
					},
					async:true,
					success:function(data){
						//最终展示数据，x轴时间，y轴时间：val
						var showDatas = [];
						
						//x,y轴数据
						var x_data;
						var y_data;
						
						//遍历data,取出val和collect_time
						$.each(data,function(i,value){
							x_data = this.collectTime;
							y_data = this.val;
							showDatas.push({name:x_data.toString(),value:[x_data,y_data]})
						})
						
						dynamicData.hideLoading();
						
						// 填入数据
					    dynamicData.setOption({
					        series: [{
					            data: showDatas
					        }]
					    });
					},
					error:function(){
						alert('出错了!')
					},
				});		
			}
			
			var option_dynamicData = {
				    title: {
				        text: 'iotx动态数据'
				    },
				    tooltip: {
				        trigger: 'axis',
				        formatter: function (params) {
				        	 param = params[0];
				             return parseTimeStamp(param.name) + ' : ' + param.value[1];
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
				        showSymbol: false,
				        hoverAnimation: false,
				        smooth:true,//圆滑曲线
				        data: showDatas,
				        color:['#5399C1'],
				        areaStyle: {normal: {
			            	color: {
							    type: 'linear',
							    x: 0,
							    y: 0,
							    x2: 0,
							    y2: 1,
							    colorStops: [{
							        offset: 0, color: '#3580B6' // 0% 处的颜色
							    }, {
							        offset: 1, color: '#374D66' // 100% 处的颜色
							    }],
							    globalCoord: false // 缺省为 false
							}
			            }},
				        
			            markPoint: {
			                data: [
			                    {type: 'max', name: '最大值'},
			                    {type: 'min', name: '最小值'}
			                ]
			            },
			            
			            markLine: {
			                data: [
			                    {type: 'average', name: '平均值',
			                    	symbol: 'circle',
			                        label: {
			                            normal: {
			                                position: 'end',
			                                formatter: '{b}:{c}'//{a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。
			                            }
			                        },
			                        lineStyle:{
			                        	normal:{
			                        		
			                        	}
			                        }
			             		},
			                    
			                    {name: '上限基准线',yAxis: maxVal,
			             			symbol: 'circle',
			                        label: {
			                            normal: {
			                                position: 'end',
			                                formatter: '{b}:{c}'
			                            }
			                        },
			                        lineStyle:{
			                        	normal:{
			                        		
			                        	}
			                        }
			                    },
			                    {name: '下限基准线',yAxis: minVal,
			                    	symbol: 'circle',
			                        label: {
			                            normal: {
			                                position: 'end',
			                                formatter: '{b}:{c}'
			                            }
			                        },
			                        lineStyle:{
			                        	normal:{
			                        		
			                        	}
			                        }
			                    },
			                    
			                    [{
			                        symbol: 'none',
			                        x: '90%',
			                        yAxis: 'max'
			                    }, {
			                    	type: 'max',
			                        name: '最高点',
			                        symbol: 'circle',
			                        label: {
			                            normal: {
			                                position: 'start',
			                                formatter: '最大值'
			                            }
			                        },
			                    }], [{
			                        symbol: 'none',
			                        x: '90%',
			                        yAxis: 'min'
			                    } , {
			                    	type: 'min',
				                    name: '最低点',
			                        symbol: 'circle',
			                        label: {
			                            normal: {
			                                position: 'start',
			                                formatter: '最小值'
			                            }
			                        },
			                    }],
			                ]
			            }
				    }]
				};
			getDynamicData();
			dynamicData.hideLoading();
			dynamicData.setOption(option_dynamicData, true);
			
			//定时任务
			setInterval(function () {
		
				getDynamicData();
				
			}, 5000);
		
		}
		
		function parseTimeStamp(nS) {     
			  return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/,' ');     
		}   
		
	});