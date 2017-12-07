/**
 * 
 */
$(document).ready(function(){
	//第二个参数可以指定前面引入的主题
    var myChart = echarts.init(document.getElementById('right-line'));
    //图表显示提示信息
    myChart.showLoading();
    
    function getLineData(){
    	$.ajax({
			type : "get",
			url : '/customerServiceProcess/process/started/daily',
			async : true,
			success : function(data) {
				var counts=[]
				var dates=[]
				$.each(data,function(){
					counts.push(this.count)
					dates.push(this.date)
				})
				// 填入数据
				myChart.setOption({
					xAxis: [{
			            data : dates
			        }],
			        series: [{
			            data : counts
			        }]
			    });
			},
			error : function() {
				alert('出错了!')
			},
		});
    }
    
    var option = {
        grid: {
            left: 10,
            top: '10%',
            bottom: 20,
            right: 40,
            containLabel: true
        },
        tooltip: {
            trigger: 'axis',
            formatter : function(params) {
            	param = params[0];
				return param.name + ' : ' + param.value;
			},
			axisPointer : {
				animation : false
			}
        },

        xAxis: {
            type: 'category',
            boundaryGap: false,
            splitLine: {
                show: true,
                interval: 'auto',
                lineStyle: {
                    color: '#ccc'
                }
            },
            axisTick: {
                show: false
            },
            axisLine: {
                show:false
            },
            axisLabel: {
                margin: 10,
                textStyle: {
                    fontSize: 14,
                    color: '#ccc'
                }
            }
        },
        yAxis: {
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#ccc',
                    type:'dashed',
                }
            },
            axisLine: {
                lineStyle: {
                    color: '#ccc'
                }
            }
        },
        series: [{
            name: '今日',
            type: 'line',
            smooth: true,
            showSymbol: false,
            symbol: 'circle',
            symbolSize: 6,
            areaStyle: {
                normal: {
                    color: '#E87D52',
                    opacity: 0.08
                }
            },

            itemStyle: {
                normal: {
                    color: '#E87D52',
                    shadowBlur: 40,
                    label: {
                        show: true,
                        position: 'top',
                        textStyle: {
                            color: '#E87D52',
                        }
                    }
                }
            },
            lineStyle: {
                normal: {
                    width: 3
                }
            }
        }, ]
    };
    myChart.hideLoading();
    myChart.setOption(option, true);
    
    getLineData();
})