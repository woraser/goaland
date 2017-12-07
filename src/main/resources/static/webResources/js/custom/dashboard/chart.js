/**
 * 
 */
$(document).ready(function(){
	
    function loadyys2() {
        //第二个参数可以指定前面引入的主题
        var myLine = echarts.init(document.getElementById('right-bar'));
        //图表显示提示信息
        myLine.showLoading();
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


        myLine.hideLoading();
        myLine.setOption(option, true);
    }
    loadyys2();//柱状图
})