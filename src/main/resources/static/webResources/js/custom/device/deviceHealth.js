/**
 * 
 */
$(document).ready(function() {
	function loadHealth() {
        //第二个参数可以指定前面引入的主题
        var myChart = echarts.init(document.getElementById('healthindex'));
        //图表显示提示信息
        myChart.showLoading();
        var gauge_value=66
        option = {
            title: {
                x: "center",
                bottom: 50,
                text: '健康度',
                textStyle: {
                    fontWeight: 'normal',
                    fontSize: 14,
                    color: "#999"
                },
            },
            tooltip: {
                show: true,
                backgroundColor: '#F7F9FB',
                borderColor: '#92DAFF',
                borderWidth: '1px',
                textStyle: {
                    color: 'black'
                },
                formatter: function(param) {
                    return '<em style="color:' + param.color + ';">' + param.value + '</em> 分'
                }

            },
            series: [ {
                name: '信用分',
                type: 'gauge',
                startAngle: 180,
                radius: 160,
                center: ['50%', '85%'], // 默认全局居中
                radius: '120%',
                endAngle: 0,
                min: 0,
                max: 100,

                axisLine: {
                    show: true,
                    lineStyle: {
                        width: 20,
                        shadowBlur: 0,
                        color: [
                            [0.2, '#E43F3D'],
                            [0.4, '#E98E2C'],
                            [0.6, '#DDBD4D'],
                            [0.8, '#7CBB55'],
                            [1, '#9CD6CE']
                        ]
                    }
                },
                axisTick: {
                    show: true,
                    splitNumber: 1
                },
                splitLine: {
                    show: true,
                    length: 20,

                },

                axisLabel: {
                    formatter: function(e) {
                        switch(e + "") {
                            case "10":
                                return "差";
                            case "30":
                                return "较差";
                            case "50":
                                return "中等";

                            case "70":
                                return "良好";

                            case "90":
                                return "优秀";

                            default:
                                return e;
                        }
                    },
                    textStyle: {
                        fontSize: 12,
                        fontWeight: ""
                    }
                },
                pointer: {
                    width: "3%",
                    length: '90%',
                    color: "black"
                },
                detail: {

                    offsetCenter: [0, -70],
                    textStyle: {
                        fontSize: 30
                    }
                },
                data: [{
                    name: "",
                    value: Math.floor(gauge_value)
                }]
            }]
        };
        setInterval(function () {
            option.series[0].data[0].value = (Math.random() * 100).toFixed(0) - 0;
            myChart.setOption(option, true);
        },2000);

        myChart.hideLoading();
        myChart.setOption(option, true);
    }
	
	loadHealth()
})