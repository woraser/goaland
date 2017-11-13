/**
 * 
 */
$(document).ready(function(){
	 //第二个参数可以指定前面引入的主题
    var myChart = echarts.init(document.getElementById('one'));
    
    //图表显示提示信息
    myChart.showLoading();
    
    //发请求从后台数据库中取动态数据
	function getDynamicData(){
		$.ajax({
			type:"get",
			url:'/devCategory/count/i18n',
			async:true,
			success:function(data){
				var counts=[]
				var names=[]
				$.each(data,function(){
					counts.push(this.count)
					names.push($.i18n.prop('device.'+this.name))
				})
				// 填入数据
				myChart.setOption({
					yAxis : {
						data : names
					},
			        series: [{
			            data : counts
			        }]
			    });
			},
			error:function(){
				alert('出错了!')
			},
		});		
	}
    
    option = {
        backgroundColor:'#FFFFFF',

        title: {
            left:'center',
            top:'20',
            text: '设备数量统计',
            subtext: '数据更新时间2017-10-24',
            textStyle: {
                align: 'right',
            },
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },

        grid: {
            top:'30%',
            left: '3%',
            right: '5%',
            bottom: '10%',
            containLabel: true
        },
        xAxis: {
            axisLine:{
                lineStyle:{
                    type:'dashed',
                    color:'#BFBFBF',
                    width:0,   //这里是坐标轴的宽度,可以去掉
                }
            },
            type: 'value',
            boundaryGap: [0, 0.01],
            splitNumber: 10,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#ccc',
                    type:'dashed',
                }
            },
        },
        yAxis: {
            axisLine:{
                lineStyle:{
                    type:'dashed',
                    color:'#BFBFBF',
                    width:0,   //这里是坐标轴的宽度,可以去掉
                }
            },
            type: 'category',
            axisLabel:{
                interval: 0,//标签设置为全部显示
                formatter:function(params){
                    var newParamsName = "";// 最终拼接成的字符串
                    var paramsNameNumber = params.length;// 实际标签的个数
                    var provideNumber = 7;// 每行能显示的字的个数
                    var rowNumber = Math.ceil(paramsNameNumber / provideNumber);// 换行的话，需要显示几行，向上取整

                    // 条件等同于rowNumber>1
                    if (paramsNameNumber > provideNumber) {

                        for (var p = 0; p < rowNumber; p++) {
                            var tempStr = "";// 表示每一次截取的字符串
                            var start = p * provideNumber;// 开始截取的位置
                            var end = start + provideNumber;// 结束截取的位置
                            // 此处特殊处理最后一行的索引值
                            if (p == rowNumber - 1) {
                                // 最后一次不换行
                                tempStr = params.substring(start, paramsNameNumber);
                            } else {
                                // 每一次拼接字符串并换行
                                tempStr = params.substring(start, end) + "\n";
                            }
                            newParamsName += tempStr;// 最终拼成的字符串
                        }

                    } else {
                        // 将旧标签的值赋给新标签
                        newParamsName = params;
                    }
                    //将最终的字符串返回
                    return newParamsName
                }

            }
        },
        series: [

            {

                type: 'bar',
                barWidth : 20,//柱图宽度
                itemStyle:{

                    normal:{
                        color:'#00ABF7',
                        barBorderRadius: 20
                    }
                },
            },

        ]
    };
    myChart.hideLoading();
    myChart.setOption(option, true);
    getDynamicData()
    
})