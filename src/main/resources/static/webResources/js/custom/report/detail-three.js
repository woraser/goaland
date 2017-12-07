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
    
    function loadyys3() {
        //第二个参数可以指定前面引入的主题
        var myChart = echarts.init(document.getElementById('charts'));
        //图表显示提示信息
        myChart.showLoading();
        // Generate data
        var aaa = ['1','2','3','4','5','6','7','8','9','10','11','12'];

        var lineData = ['100','300','200','300','200','300','200','300','200','300','200','250'];
        var barData = ['50','50','30','50','30','50','30','50','30','50','30','60'];
        // option
        option = {
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
                data: aaa,

                axisLine:{
                    lineStyle:{
                        type:'dashed',
                        color:'#BFBFBF',
                        width:0,   //这里是坐标轴的宽度,可以去掉
                    }
                },
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
                data: barData
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
                data: lineData
            }]
        };
        myChart.hideLoading();
        myChart.setOption(option, true);
    }

    loadyys3()
    
    $('select').selectOrDie({

    });
})