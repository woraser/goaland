/**
 * 
 */
$(document).ready(function(){
	
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
    loadyys2();//柱状图
    loadyys3();//折线图
})