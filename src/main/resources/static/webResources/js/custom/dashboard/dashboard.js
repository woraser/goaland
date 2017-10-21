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
                                'color': '#FFFFFF'
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
                                'color': '#DBDBDA'
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
                        },  {
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
                                'color': '#e25921',
                                'weight': '0',
                                'lightness': -29
                            }
                        },  {
                            "featureType": "all",
                            "elementType": "labels.text.fill",
                            "stylers": {
                                "color": "#a6a4a3",
                                "weight": "6",
                                "saturation": -65
                            }
                        }
                        ]
                    }
                },
                legend: {
                    data:['物流','设备分布'],
                    textStyle: {
                        color: '#A8A8A8',
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
                        color: 'red',
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
        var myChart = echarts.init(document.getElementById('right-pie'));
        //图表显示提示信息
        myChart.showLoading();
        option = {
            color:['#E56A38', '#EB8B64','#F0AC90','#F6CDBC','#FCEEE8'],
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left:'50%',
                top:'20%',
                textStyle: {
                    color:'black',
                },
                data:['亚洲:22,580','欧洲:4,240','大洋洲:3,520','美洲:2.820','非洲:2,110'],

            },
            series: [
                {
                    name:'访问来源',
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
                    data:[
                        {value:335, name:'亚洲:22,580'},
                        {value:310, name:'欧洲:4,240'},
                        {value:234, name:'大洋洲:3,520'},
                        {value:135, name:'美洲:2.820'},
                        {value:1548, name:'非洲:2,110'}
                    ]
                }
            ]
        };
        myChart.hideLoading();
        myChart.setOption(option, true);
    }
    function loadyys2() {
        //第二个参数可以指定前面引入的主题
        var myChart = echarts.init(document.getElementById('right-bar'));
        //图表显示提示信息
        myChart.showLoading();
        // Generate data
        var aaa = ['1','2','3','4','5','6','7','8','9','10','11','12'];

        var lineData = ['100','300','200','300','200','300','200','300','200','300','200','250'];
        var barData = ['50','50','30','50','30','50','30','50','30','50','30','60'];
        // option
        option = {
            backgroundColor: '#FFFFFF',
            height:'63%',
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
                data: ['一级告警', '告警总量'],

                textStyle: {
                    color: '#ccc'
                }
            },
            xAxis: {
                data: aaa,
                axisLine: {
                    lineStyle: {
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
                name: '告警总量',
                type: 'bar',

                itemStyle: {
                    normal: {
                        color:'#E56A38',
                    }
                },
                data: barData
            }, {
                name: '一级告警',
                type: 'bar',
                barGap: '-100%',
                z: -10,
                itemStyle: {
                    normal: {
                        //柱形图圆角，初始化效果
                        color:'#DBDBDA',
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
                data: lineData
            }]
        };


        myChart.hideLoading();
        myChart.setOption(option, true);
    }
    function loadyys3() {
        //第二个参数可以指定前面引入的主题
        var myChart = echarts.init(document.getElementById('right-line'));
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
                        lineStyle: {
                            color: '#ccc'
                        }
                },
                axisLabel: {
                    textStyle: {
                        color: '#ccc'
                    }
                },
                axisTick: {
                    show: false
                }
            },
            yAxis: {
                ayisLine: {
                    lineStyle: {
                        color: '#ccc'
                    }
                },
                axisLabel: {
                    textStyle: {
                        color: '#ccc'
                    }
                },
                min: 20000,
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
                areaStyle: {
                    normal: {
                        color: '#E87D52',
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
    loadyys3();//折线图
})

