/**
 * 
 */
$(document).ready(function(){
	var dom = document.getElementById("allmap");
	var myChart = echarts.init(dom);
	var planePath = 'arrow';
	var geoCoorddata = {
	        '广州': [113.2759952545166, 23.117055306224895],
	        '温哥华': [123.055606, 49.143602],
	        '北京': [116.40739499999995, 39.904211],
	        '巴黎': [2.201204, 48.513902],
	        '古巴':[82.23 ,23.08],
	        '堪培拉':[149.07,35.17],
	        '开罗':[31,30],
	    },
	    option = {
	        bmap: {
	            center: [82.23 ,23.08],
	            zoom: 1,
	            roam: true,
	            mapStyle: {
	                styleJson: [{
	                    'featureType': 'land', //调整土地颜色
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'color': '#303644'
	                    }
	                }, {
	                    'featureType': 'building', //调整建筑物颜色
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'color': '#04406F'
	                    }
	                }, {
	                    'featureType': 'building', //调整建筑物标签是否可视
	                    'elementType': 'labels',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'highway', //调整高速道路颜色
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'color': '#015B99'
	                    }
	                }, {
	                    'featureType': 'highway', //调整高速名字是否可视
	                    'elementType': 'labels',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'arterial', //调整一些干道颜色
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'color': '#003051'
	                    }
	                }, {
	                    'featureType': 'arterial',
	                    'elementType': 'labels',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'green',
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'water',
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'color': '#3b455d'
	                    }
	                }, {
	                    'featureType': 'subway', //调整地铁颜色
	                    'elementType': 'geometry.stroke',
	                    'stylers': {
	                        'color': '#003051'
	                    }
	                }, {
	                    'featureType': 'subway',
	                    'elementType': 'labels',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'railway',
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'railway',
	                    'elementType': 'labels',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'all', //调整所有的标签的边缘颜色
	                    'elementType': 'labels.text.stroke',
	                    'stylers': {
	                        'color': '#313131'
	                    }
	                }, {
	                    'featureType': 'all', //调整所有标签的填充颜色
	                    'elementType': 'labels.text.fill',
	                    'stylers': {
	                        'color': '#FFFFFF'
	                    }
	                }, {
	                    'featureType': 'manmade',
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'manmade',
	                    'elementType': 'labels',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'local',
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'local',
	                    'elementType': 'labels',
	                    'stylers': {
	                        'visibility': 'off'
	                    }
	                }, {
	                    'featureType': 'subway',
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'lightness': -65
	                    }
	                }, {
	                    'featureType': 'railway',
	                    'elementType': 'all',
	                    'stylers': {
	                        'lightness': -40
	                    }
	                }, {
	                    'featureType': 'boundary',
	                    'elementType': 'geometry',
	                    'stylers': {
	                        'color': '#363C4C',
	                        'weight': '1',
	                        'lightness': -29
	                    }
	                }]
	            }
	        },
	        legend: {
	            data:['物流','设备分布'],
	            textStyle: {
	                color: '#fff'
	            },

	        },
	        series: [{
	            name: '物流',
	            type: 'lines',
	            mapType: 'china',
	            coordinateSystem: 'bmap',
	            zlevel: 1,
	            data: [{
	                name: '广州',
	                toname: '温哥华',
	                coords: [geoCoorddata['广州'], geoCoorddata['温哥华']]
	            }, {
	                name: '广州',
	                toname: '北京',
	                coords: [geoCoorddata['广州'],geoCoorddata['北京'] ]
	            }, {
	                name: '广州',
	                toname: '巴黎',
	                coords: [geoCoorddata['广州'],geoCoorddata['巴黎']]
	            }, {
	                name: '广州',
	                toname: '古巴',
	                coords: [geoCoorddata['广州'],geoCoorddata['古巴']]
	            }, {
	                name: '广州',
	                toname: '堪培拉',
	                coords: [geoCoorddata['广州'],geoCoorddata['堪培拉']]
	            },  {
	                name: '广州',
	                toname: '开罗',
	                coords: [geoCoorddata['广州'],geoCoorddata['开罗']]
	            }],
	            //线上面的动态特效
	            effect: {
	                show: true,
	                period: 6,
	                trailLength: 0.7,
	                color: '#fff',
	                symbolSize: 4,
	                symbol:planePath,
	            },
	            lineStyle: {
	                normal: {
	                    width: '',
	                    color: '#a6c84c',
	                    curveness: 0.2
	                }
	            }
	        }, {
	            name:'设备分布',
	            type: 'effectScatter',
	            mapType: 'china',
	            coordinateSystem: 'bmap',
	            zlevel: 4, /*圆点大小 */
	            data: [{
	                name: '广州',
	                value: geoCoorddata['广州'].concat(200)
	            },{
	                name: '古巴',
	                value: geoCoorddata['古巴'].concat(200)
	            },{
	                name: '开罗',
	                value: geoCoorddata['开罗'].concat(200)
	            } ],
	            rippleEffect: {
	                period: 10,
	                scale: 5,
	                brushType: 'fill'
	            },
	        }]
	    };
	myChart.setOption(option);
	function loadyys1() {
	    //第二个参数可以指定前面引入的主题
	    var myChart = echarts.init(document.getElementById('circletable'));
	    //图表显示提示信息
	    myChart.showLoading();
	    option = {
	        color:['#1FA4FF', '#06CDFB','#06EAEF','#03B6D2','#397BD5'],
	        tooltip: {
	            trigger: 'item',
	            formatter: "{a} <br/>{b}: {c} ({d}%)"
	        },
	        legend: {
	            orient: 'vertical',
	            left:'50%',
	            top:'15%',
	            textStyle: {
	                color:'white',
	            },
	            data:['亚洲 ASIA:22,580','欧洲 EUROPE:4,240','大洋洲 OCEONIA:3,520','美洲 AMERICA:2.820','非洲 AFRICA:2,110'],

	        },
	        series: [
	            {
	                name:'访问来源',
	                type:'pie',
	                center: ['25%', '50%'],
	                radius: ['35%', '70%'],
	                avoidLabelOverlap: false,
	                label: {
	                    normal: {
	                        show: false,
	                        position: 'center'
	                    },
	                    emphasis: {
	                        show: false,
	                        textStyle: {
	                            fontSize: '30',
	                            fontWeight: 'bold'
	                        }
	                    }
	                },
	                labelLine: {
	                    normal: {
	                        show: false
	                    }
	                },
	                data:[
	                    {value:335, name:'亚洲 ASIA:22,580'},
	                    {value:310, name:'欧洲 EUROPE:4,240'},
	                    {value:234, name:'大洋洲 OCEONIA:3,520'},
	                    {value:135, name:'美洲 AMERICA:2.820'},
	                    {value:1548, name:'非洲 AFRICA:2,110'}
	                ]
	            }
	        ]
	    };
	    myChart.hideLoading();
	    myChart.setOption(option, true);
	}
	function loadyys3() {
	    //第二个参数可以指定前面引入的主题
	    var myChart = echarts.init(document.getElementById('healthindex'));
	    //图表显示提示信息
	    myChart.showLoading();
	    option = {
	        top:'80px',
	        tooltip : {
	            formatter: "{a} <br/>{b} : {c}%"
	        },
	        series: [
	            {
	                name: '业务指标',
	                type: 'gauge',
	                center: ['50%', '75%'],
	                startAngle: 180,
	                endAngle: 0,
	                axisLabel: {
	                    show: false,
	                },
	                min:0,
	                max:100,
	                splitNumber:10,
	                axisLine: {            // 坐标轴线
	                    lineStyle: {       // 属性lineStyle控制线条样式
	                        color: [[0.0, 'lime'],[0.6, '#06C0EB'],[1, '#0AE6EA']],
	                        width: 30
	                    }
	                },
	                detail: {formatter:'{value}分',
	                    show:true,
	                    textStyle: {
	                        fontSize: 20,
	                        color:'#5EAA65',
	                        fontWeight: '800',
	                    },
	                },
	                data: [{value: 50, name: ''}]
	            }
	        ]
	    };
	    setInterval(function () {
	        option.series[0].data[0].value = (Math.random() * 100).toFixed(0) - 0;
	        myChart.setOption(option, true);
	    },2000);

	    myChart.hideLoading();
	    myChart.setOption(option, true);
	}
	function loadyys2() {
	    //第二个参数可以指定前面引入的主题
	    var myChart = echarts.init(document.getElementById('warning'));
	    //图表显示提示信息
	    myChart.showLoading();
	    option = {
	        tooltip: {
	            //触发类型，默认（'item'）数据触发，可选为：'item' | 'axis'
	            trigger: 'axis'
	        },
	        grid:{
	          top:20,
	          left:40,
	          button:-10,
	        },
	        xAxis: {
	            splitLine:{show: false},
	            data: ['1', '2', '3', '4', '5', '6', '7'],
	            axisLabel: {
	                textStyle: {
	                    color: '#999999',
	                    //fontSize:'16'
	                },
	            },
	        },
	        yAxis: {
	            splitLine:{show: false},
	            axisLabel: {
	                textStyle: {
	                    color: '#999999',
	                    //fontSize:'16'
	                },
	            },
	        },
	        series: [{
	            type: 'bar',
	            barWidth:'55%',
	            data:[7000, 1200, 5000, 3000, 2000, 6000, 5600],

	            itemStyle: {
	                normal: {
	                    barBorderRadius:[10, 10, 10, 10],
	                    color: new echarts.graphic.LinearGradient(
	                        0, 0, 0, 1,
	                        [
	                            {offset: 0, color: '#05C3F6'},

	                            {offset: 1, color: '#1885A8'}
	                        ]
	                    )
	                },
	                emphasis: {
	                    color: new echarts.graphic.LinearGradient(
	                        0, 0, 0, 1,
	                        [
	                            {offset: 0, color: '#2378f7'},
	                            {offset: 0.7, color: '#2378f7'},
	                            {offset: 1, color: '#83bff6'}
	                        ]
	                    )
	                }
	            },
	        },
	            {
	                type: 'line',
	                data:[7000, 1200, 5000, 3000, 2000, 6000, 5600],
	                symbolSize: 5,  //设置折现圆点的大小
	                color: ['#3475C5'],
	            }]
	    };
	    myChart.hideLoading();
	    myChart.setOption(option, true);
	}
	function loadyys7() {
	    //第二个参数可以指定前面引入的主题
	    var myChart = echarts.init(document.getElementById('warning11'));
	    //图表显示提示信息
	    myChart.showLoading();
	    var data_val = [22700, 23066, 24066, 23492, 27532, 26694, 28757],
	        xAxis_val = ['1', '2', '3', '4', '5', '6', '7'];
	    var data_val1 = [0, 0, 0, 0, 0, 0, 0];
	    var option = {

	        grid: {
	            left: 10,
	            top: '10%',
	            bottom: 20,
	            right: 40,
	            containLabel: true
	        },
	        tooltip: {
	            show: true,
	            backgroundColor: '#384157',
	            borderColor: '#384157',
	            borderWidth: 1,
	            formatter: '{b}:{c}',
	            extraCssText: 'box-shadow: 0 0 5px rgba(0, 0, 0, 1)'
	        },
	        legend: {
	            right: 0,
	            top: 0,
	            data: ['距离'],
	            textStyle: {
	                color: '#5c6076'
	            }
	        },
	        title: {
	            show:false,
	            x: '4.5%',
	            top: '1%',
	            textStyle: {
	                color: '#fff'
	            }
	        },
	        xAxis: {
	            data: xAxis_val,
	            boundaryGap: false,
	            axisLine: {
	                show: false
	            },
	            axisLabel: {
	                textStyle: {
	                    color: '#fff'
	                }
	            },
	            axisTick: {
	                show: false
	            }
	        },
	        yAxis: {
	            ayisLine: {
	                show: false
	            },
	            axisLabel: {
	                textStyle: {
	                    color: '#fff'
	                }
	            },
	            min: 20000,
	            splitLine: {
	                show: true,
	                lineStyle: {
	                    color: '#2e3547'
	                }
	            },
	            axisLine: {
	                lineStyle: {
	                    color: '#384157'
	                }
	            }
	        },

	        series: [ {
	            type: 'line',
	            name: 'linedemo',
	            smooth: true,
	            symbolSize: 10,
	            animation: false,
	            lineWidth: 1.2,
	            hoverAnimation: false,
	            data: data_val,
	            symbol: 'circle',
	            itemStyle: {
	                normal: {
	                    color: '#08D1FF',
	                    shadowBlur: 40,
	                    label: {
	                        show: true,
	                        position: 'top',
	                        textStyle: {
	                            color: '#08D1FF',

	                        }
	                    }
	                }
	            },
	            areaStyle: {
	                normal: {
	                    color: '#08D1FF',
	                    opacity: 0.08
	                }
	            }

	        }]
	    };
	    myChart.hideLoading();
	    myChart.setOption(option, true);
	}
	
	loadyys1();//饼图
    loadyys2();//柱状图
    loadyys3();//健康指数
    loadyys7();//折线图
})

