/**
 * 
 */
$(document).ready(function(){
	var names=[$.i18n.prop('device.HVSCCOOLING'),$.i18n.prop('device.FACTS'),
               $.i18n.prop('device.ELECTRICDRIVECOOLING'),$.i18n.prop('device.RENEWABLEENERGYCOOLING'),
               $.i18n.prop('device.ACCUMULATIONOFCOLD')];
	
	//第二个参数可以指定前面引入的主题
    var myChart = echarts.init(document.getElementById('two'));
    //图表显示提示信息
    myChart.showLoading();
    
    //发请求从后台数据库中取动态数据
	function getDynamicData(){
		$.ajax({
			type:"get",
			url:'/repairedDeviceDailyPer/report/data/',
			async:true,
			success:function(data){
				// 填入数据
				myChart.setOption({
					legend : {
						data : names
					},
					xAxis: [{
			            data : data.date
			        }],
			        series : [{
	                      name : names[0],
	                      type:'line',
	                      stack: '总量',

	                      data:data.HVSCCOOLING
	                  },
	                  {
	                      name : names[1],
	                      type:'line',
	                      stack: '总量',

	                      data:data.FACTS
	                  },
	                  {
	                      name : names[2],
	                      type:'line',
	                      stack: '总量',

	                      data:data.ELECTRICDRIVECOOLING
	                  },
	                  {
	                      name : names[3],
	                      type:'line',
	                      stack: '总量',

	                      data:data.RENEWABLEENERGYCOOLING
	                  },
	                  {
	                      name : names[4],
	                      type:'line',
	                      stack: '总量',
	                      data:data.ACCUMULATIONOFCOLD
	                  }
	              ]
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
            text: '产品维修量统计',
            subtext: '数据更新时间2017-10-24',
            textStyle: {
                align: 'right',
            },
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            type: 'scroll',

            top:'20%',
            data:names,
        },
        grid: {
            top:'35%',
            left: '3%',
            right: '5%',
            bottom: '10%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                axisLine:{
                    lineStyle:{
                        type:'dashed',
                        color:'#BFBFBF',
                        width:0,   //这里是坐标轴的宽度,可以去掉
                    }
                },
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLine:{
                    lineStyle:{
                        type:'dashed',
                        color:'#BFBFBF',
                        width:0,   //这里是坐标轴的宽度,可以去掉
                    }
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: '#ccc',
                        type:'dashed',
                    }
                },
            }
        ],
    };
    myChart.hideLoading();
    myChart.setOption(option, true);
    getDynamicData()
})