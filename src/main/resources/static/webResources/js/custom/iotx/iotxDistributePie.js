/**
 * 
 */
$(document).ready(
		function() {

			$.ajax({
				url : '/iotx/iotxDistribute/data',
				type : 'get',
				dataType : 'json',
				success : function(datas) {
					initPie(datas);
				},
				error : function(data){
					warning('节点分布加载失败，请联系管理员或刷新页面重试');
				}
			});

			function initPie(datas) {
				var iotxPie = echarts.init(document
						.getElementById('iotxDistributePie'));

				iotxPie.showLoading();

				var option = {
					color:['#E56A38', '#EB8B64','#F0AC90','#F6CDBC','#FCEEE8'],
					title : {
						text : 'iot-x节点分布统计',
						x : 'center',
						textStyle: {
				            color: '#ccc'
				        },
					},
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b} : {c} ({d}%)"
					},
					legend: {
		                type:'scroll',
		                orient: 'vertical',
		                left:'68%',
		                top:'20%',
		                textStyle: {
		                    color:'black',
		                },
		                data:(function(){
	                        var res = [];
	                        for(var i=0; i<datas.length; i++) {
		                        res.push({
		                        	name: datas[i].name,
		                        	value: datas[i].amount,
	                        	});
	                        }
	                        return res;
                        })(),

		            },
		            series: [
		                {
		                    name:'',
		                    type:'pie',
		                    center: ['25%', '50%'],
		                    radius: ['45%', '70%'],
		                    avoidLabelOverlap: false,
		                    label: {
		                        normal: {
		                            show: false,
		                            position: 'center'
		                        },
		                        emphasis: {
		                            show: true,

		                            textStyle: {
		                                formatter:function(val){    return val.split("-").join("\n");},
		                                fontSize: '10',
		                                fontWeight: 'bold'
		                            }
		                        }
		                    },
		                    labelLine: {
		                        normal: {
		                            show: false
		                        }
		                    },
		                    data:(function(){
		                        var res = [];
		                        for(var i=0; i<datas.length; i++) {
			                        res.push({
			                        	name: datas[i].name,
			                        	value: datas[i].amount,
		                        	});
		                        }
		                        return res;
	                        })(),
		                }
		            ]
				};
				iotxPie.hideLoading();
				iotxPie.setOption(option, true);
			}
		})