/**
 * 
 */
$(document).ready(function(){
	//第二个参数可以指定前面引入的主题
    var myChart = echarts.init(document.getElementById('three'));
    //图表显示提示信息
    myChart.showLoading();
    
    var dates = []
	var completes = []
	var unCompletes = []
    
    //发请求从后台数据库中取动态数据
	function getDynamicData(){
		$.ajax({
			type:"get",
			url:'/customerServiceProcessDailyPer/management/data/REMOTE',
			data: {
				"showAttributes":"completed,unCompleted,countDate",
			},
			async:true,
			success:function(data){
				$.each(data.content,function(){
					dates.push(this.countDate.split(" ")[0])
					completes.push(this.completed)
					unCompletes.push(this.unCompleted)
				})
				
				// option
			    var option = {
			        backgroundColor:'#FFFFFF',
			        title: {
			            left:'center',
			            top:'20',
			            text: '工单数量统计',
			            subtext: '数据更新时间2017-10-24',
			            textStyle: {
			                align: 'right',
			            },
			        },
			        grid: {
			            top:'30%',
			            left: '3%',
			            right: '5%',
			            bottom: '10%',
			            containLabel: true
			        },
			        tooltip: {
			            trigger: 'axis',
			            axisPointer: {
			                type: 'shadow',
			                label: {
			                    show: true,
			                    backgroundColor: '#333'
			                }
			            }
			        },
			        legend: {

			            data: ['未完成', '已完成'],
			            top:'20%',
			            left:'70%',
			            textStyle: {
			            }
			        },
			        xAxis: {
			            axisLine:{
			                lineStyle:{
			                    type:'dashed',
			                    color:'#BFBFBF',
			                    width:0,   //这里是坐标轴的宽度,可以去掉
			                }
			            },
			            data: dates.reverse()
			        },
			        yAxis: {
			            splitLine: {
			                show: true,
			                lineStyle: {
			                    color: '#ccc',
			                    type:'dashed',
			                }
			            },
			            axisLine:{
			                lineStyle:{
			                    type:'dashed',
			                    color:'#BFBFBF',
			                    width:0,   //这里是坐标轴的宽度,可以去掉
			                }
			            },
			        },
			        series: [{
			            name: '已完成',
			            type: 'bar',
			            barWidth:'20',
			            itemStyle: {
			                normal: {
			                    color:'#00ABF7',
			                }
			            },
			            data: completes.reverse()
			        }, {
			            name: '未完成',
			            type: 'bar',
			            barWidth:'20',
			            barGap: '-100%',
			            z: -10,
			            itemStyle: {
			                normal: {
			                    //柱形图圆角，初始化效果
			                    color:'#FC8060',
			                    barBorderRadius:[2, 2, 2, 2],
			                    label: {
			                        show: false,//是否展示
			                        textStyle: {
			                            fontWeight:'bolder',
			                            fontSize : '12',
			                            fontFamily : '微软雅黑',
			                        }
			                    }
			                }
			            },
			            data: unCompletes.reverse()
			        }]
			    };
			    myChart.hideLoading();
			    myChart.setOption(option, true);
			},
			error:function(){
				alert('出错了!')
			},
		});	
	}
    
    getDynamicData()
})