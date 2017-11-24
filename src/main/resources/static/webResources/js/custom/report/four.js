/**
 * 
 */
$(document).ready(function(){
	//第二个参数可以指定前面引入的主题
    var myChart = echarts.init(document.getElementById('four'));
    //图表显示提示信息
    myChart.showLoading();
    
    //发请求从后台数据库中取动态数据
	function getDynamicData(){
		$.ajax({
			type:"get",
			url:'/customerServiceProcess/task/distribute',
			async:true,
			success:function(data){
				var values=[]
				var names=[]
				$.each(data,function(){
					values.push({value : this.count, name : $.i18n.prop('customerService.taskDef.'+this.name)})
					names.push($.i18n.prop('customerService.taskDef.'+this.name))
				})
				// 填入数据
				myChart.setOption({
					legend : {
						data : names
					},
			        series: [{
			            data : values
			        }]
			    });
			},
			error:function(){
				alert('出错了!')
			},
		});		
	}
    
    option = {
        color:['#FC8060', '#00ABF7','#33BCF9','#66CDFA','#99DDFC'],
        backgroundColor:'#FFFFFF',
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        title: {
            left:'center',
            top:'20',
            text: '工单节点分布',
            subtext: '数据更新时间2017-10-24',
            textStyle: {
                align: 'right',
            },
        },
        legend: {
        	show: false,
            orient: 'vertical',
            orient: 'vertical',
            right: '20%',
            bottom: '35%',
            textStyle: {
                fontSize:'14',
            }
        },
        grid: {
            right:'50%',
        },
        series: [
            {
                name:'工单节点',
                type:'pie',
                radius: ['40%', '55%'],
                center: ['50%', '60%'],
                label: {
                    normal: {
                        backgroundColor: '#eee',
                        borderColor: '#aaa',
                        borderWidth: 1,
                        borderRadius: 4,
                        rich: {
                            a: {
                                color: '#999',
                                lineHeight: 22,
                                align: 'center'
                            },
                            hr: {
                                borderColor: '#aaa',
                                width: '100%',
                                borderWidth: 0.5,
                                height: 0
                            },
                            b: {
                                fontSize: 16,
                                lineHeight: 33
                            },
                            per: {
                                color: '#eee',
                                backgroundColor: '#334455',
                                padding: [2, 4],
                                borderRadius: 2
                            }
                        }
                    }
                },
            }
        ]
    };
    myChart.hideLoading();
    myChart.setOption(option, true);
    getDynamicData()
})