/**
 * 
 */
$(document).ready(
		function() {

			$.ajax({
				url : '/iotx/iotxDistribute',
				data : {
					'company.code' : ($('#isAdmin').length > 0?null:$("#companyCode").val()),
				},
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
					series : [ {
						name : 'iotx分布',
						type : 'pie',
						radius : '55%',
						center : [ '50%', '60%' ],
						data : (function(){
			                        var res = [];
			                        for(var i=0; i<datas.length; i++) {
				                        res.push({
				                        	name: datas[i].name,
				                        	value: datas[i].amount,
			                        	});
			                        }
			                        return res;
		                        })(),
						itemStyle : {
							emphasis : {
								shadowBlur : 10,
								shadowOffsetX : 0,
								shadowColor : 'rgba(0, 0, 0, 0.5)'
							}
						}
					} ]
				};
				iotxPie.hideLoading();
				iotxPie.setOption(option, true);
			}
		})