/**
 * 
 */
$(document).ready(function(){
	var myGrid = jQuery("#deviceTable");
    var myPager = "#devicePager";
    myGrid.jqGrid({
        height: '100%',
        datatype: "local",
        colNames:['工单编号','项目名称','项目编号', '产品名称', '产品编号','产品规格',  '生产厂家','联系地址','完成时间', '操作'],
        colModel:[
            {name:'snNo', index:'assetCoding', width:'60', sortable: true, search: true,searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'},
            {name:'assetCoding', index:'assetCoding', width:'60', sortable: true, search: true,searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'},
            {name:'devFactory', index:'devFactory', width:'60', sortable: true, search: true,searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'},
            {name:'devType', index:'devType', width:'120', sortable: true, search: true,searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'},
            {name:'devModel', index:'devModel', width:'150', sortable: true, search: true,searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'},
            {name:'devProfession', index:'devProfession', width:'120', sortable: true, search: true,searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'},
            {name:'baseStation', index:'baseStation', width:'160', sortable: true, search: true,searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'},
            {name:'serialNo', index:'serialNo', width:'160', sortable: true, searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'},
            {name:'networkCategory', index:'networkCategory', width:'80', sortable: true, searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'},
            {name:'remark', index:'remark', width:'200', sortable: true, search: false,searchoptions: {sopt: ['cn', 'eq'], required: true }, align: 'center'}
        ],
        multiselect: false,
        multiboxonly: true,
        multiselectWidth: 30,
        pager: myPager,
        rowNum: 20,
        rowList: [20, 50, 100],
        height: '100%',
        altRows: false,
        viewrecords: true,
        hidegrid: true,
        autowidth: true,
        forceFit: false,
        shrinkToFit: true,
        autoScroll: true,
    });

    myGrid.jqGrid('navGrid', myPager,
        {edit: false, add: false, addtext: "增加", del: false, deltext: '删除', search: false, searchtext: '查找', refreshtext: '刷新'},
        {}, // edit options
        {}, // add options
        {}, // del options
        {multipleSearch: false, multipleGroup: false, showQuery: false});// search options

    mydata = [
        {snNo:"",assetCoding: "荣信墨西哥BrovaBia钢厂电炉SVCLWW168水冷系统 ",devFactory: "密封式循环纯水冷却装置", devType: "12112458", devModel: "200124785", devProfession: "2017/05/31", baseStation: "广州高澜节能股份有限公司", serialNo: "广州市高新技术产业开发区科学城南云五路3号 ", networkCategory: "201710202-0001", quantity: "201710202-0001", remark: ""},
        {assetCoding: "荣信墨西哥BrovaBia钢厂电炉SVCLWW168水冷系统 ",devFactory: "密封式循环纯水冷却装置", devType: "12112458", devModel: "200124785", devProfession: "2017/05/31", baseStation: "广州高澜节能股份有限公司", serialNo: "广州市高新技术产业开发区科学城南云五路3号 ", networkCategory: "201710202-0001", quantity: "201710202-0001", remark: ""},
        {assetCoding: "荣信墨西哥BrovaBia钢厂电炉SVCLWW168水冷系统 ",devFactory: "密封式循环纯水冷却装置", devType: "12112458", devModel: "200124785", devProfession: "2017/05/31", baseStation: "广州高澜节能股份有限公司", serialNo: "广州市高新技术产业开发区科学城南云五路3号 ", networkCategory: "201710202-0001", quantity: "201710202-0001", remark: ""},
        {assetCoding: "荣信墨西哥BrovaBia钢厂电炉SVCLWW168水冷系统 ",devFactory: "密封式循环纯水冷却装置", devType: "12112458", devModel: "200124785", devProfession: "2017/05/31", baseStation: "广州高澜节能股份有限公司", serialNo: "广州市高新技术产业开发区科学城南云五路3号 ", networkCategory: "201710202-0001", quantity: "201710202-0001", remark: ""},
        {assetCoding: "荣信墨西哥BrovaBia钢厂电炉SVCLWW168水冷系统 ",devFactory: "密封式循环纯水冷却装置", devType: "12112458", devModel: "200124785", devProfession: "2017/05/31", baseStation: "广州高澜节能股份有限公司", serialNo: "广州市高新技术产业开发区科学城南云五路3号 ", networkCategory: "201710202-0001", quantity: "201710202-0001", remark: ""},
    ];

    for(var i = 0; i <= mydata.length; i++){
        myGrid.jqGrid('addRowData', i+1, mydata[i]);
    }
    myGrid.setGridParam({total: "2"}).trigger("reloadGrid");
    
    function loadyys4() {
        //第二个参数可以指定前面引入的主题
        var myChart = echarts.init(document.getElementById('charts'));
        //图表显示提示信息
        myChart.showLoading();
        option = {
            color:['#FC8060', '#00ABF7','#33BCF9','#66CDFA','#99DDFC'],
            backgroundColor:'#FFFFFF',
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            toolbox: {
                show: true,
                top:'50',
                feature: {
                    dataView: {
                        readOnly: false,
                        lang:['数据视图', '关闭', '导出'],
                    },
                    magicType: {type: [ 'bar']},
                    restore: {},
                    saveAsImage: {}
                }
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
                orient: 'vertical',
                data:['发起工单','上级审批','问题评估','派单','维修'],
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
                    center: ['30%', '60%'],
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
                    data:[
                        {value:310, name:'发起工单'},
                        {value:234, name:'上级审批'},
                        {value:135, name:'问题评估'},
                        {value:1048, name:'派单'},
                        {value:251, name:'维修'},
                    ]
                }
            ]
        };
        myChart.hideLoading();
        myChart.setOption(option, true);
    }
    function loadyys2() {   //未完成工单
        var myChart = echarts.init(document.getElementById('charts2'));
        //工单数据
        option = {
            title: {
                left:'center',
                top:'20',
                text: '工单量分析',
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
                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            toolbox: {
                show: true,
                top:'50',
                feature: {
                    dataView: {
                        readOnly: false,
                        lang:['数据视图', '关闭', '导出'],
                    },
                    magicType: {type: [ 'bar']},
                    restore: {},
                    saveAsImage: {}
                }
            },
            legend: {
                data: ['工单增量', '工单完成量'],
                align: 'left',
                itemWidth: 45,
                itemHeight: 25,
                orient: 'horizontal',  //vertical朝下
                top:'60',
            },
            xAxis: {
                type: 'category',
                data: ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15'],
                boundaryGap: false,
                splitLine: {
                    show: true,
                    interval: 'auto',
                    lineStyle: {
                        color: ['#D4DFF5']
                    }
                },
                axisTick: {
                    show: false
                },
                axisLine:{
                    lineStyle:{
                        type:'dashed',
                        color:'#BFBFBF',
                        width:0,   //这里是坐标轴的宽度,可以去掉
                    }
                },
                axisLabel: {
                    margin: 10,
                    textStyle: {
                        fontSize: 14
                    }
                }
            },
            yAxis: {
                type: 'value',
                splitLine: {
                    lineStyle: {
                        color: ['#D4DFF5']
                    }
                },

                axisTick: {
                    show: false
                },
                axisLine:{
                    lineStyle:{
                        type:'dashed',
                        color:'#BFBFBF',
                        width:0,   //这里是坐标轴的宽度,可以去掉
                    }
                },
                axisLabel: {
                    margin: 10,
                    textStyle: {
                        fontSize: 14
                    }
                }
            },
            series: [{
                name: '工单增量',
                type: 'line',
                smooth: true,
                showSymbol: false,
                symbol: 'circle',
                symbolSize: 6,
                data: ['1200', '1400', '1008', '1411', '1026', '1288', '1300', '800', '1100', '1000', '1118', '1322', '1008', '1411', '1026'],
                areaStyle: {
                    normal: {
                        color: {
                            type: 'linear',
                            x: 0,
                            y: 0,
                            x2: 0,
                            y2: 1,
                            colorStops: [{
                                offset: 0, color: '#FC8060' // 0% 处的颜色
                            }, {
                                offset: 1, color: '#fdf59f' // 下
                            }],
                            globalCoord: false // 缺省为 false
                        },
                        opacity: 0.3,
                    }
                },
                itemStyle: {
                    normal: {
                        color: '#FC8060'
                    }
                },
                lineStyle: {
                    normal: {
                        width: 1
                    }
                }
            }, {
                name: '工单完成量',
                type: 'line',
                smooth: true,
                showSymbol: false,
                symbol: 'circle',
                symbolSize: 6,
                data: ['1200', '1400', '808', '811', '626', '488', '1600', '1100', '500', '300', '1998', '822', '626', '488', '1600'],
                areaStyle: {
                    normal: {
                        color: {
                            type: 'linear',
                            x: 0,
                            y: 0,
                            x2: 0,
                            y2: 1,
                            colorStops: [{
                                offset: 0, color: '#0098DC' // 0% 处的颜色
                            }, {
                                offset: 1, color: '#c9e3ff' // 下
                            }],
                            globalCoord: false // 缺省为 false
                        },
                        opacity: 0.3,
                    }
                },
                itemStyle: {
                    normal: {
                        color: '#0098DC'
                    }
                },
                lineStyle: {
                    normal: {
                        width: 1
                    }
                }
            }]
        };
        myChart.hideLoading();
        myChart.setOption(option, true);
    }
    
    loadyys4();
    loadyys2();
    
    $('select').selectOrDie({

    });
})