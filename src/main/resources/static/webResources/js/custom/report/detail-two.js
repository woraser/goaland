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
    
    function loadyys2() {
        //第二个参数可以指定前面引入的主题
        var myChart = echarts.init(document.getElementById('charts'));
        //图表显示提示信息
        myChart.showLoading();
        option = {
            backgroundColor:'#FFFFFF',
            tooltip : {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
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
            title: {
                left:'center',
                top:'20',
                text: '产品维修量统计',

                textStyle: {
                    align: 'right',
                },
            },
            legend: {
                type: 'scroll',

                top:'20%',
                data:['直流输电换流阀水冷设备','柔性交流输配电晶闸管阀纯水冷却设备','新能源发电变流器纯水冷却设备','大功率电气传动变频器纯水冷却设备','蓄冷设备']
            },
            grid: {
                top:'35%',
                left: '3%',
                right: '5%',
                bottom: '10%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : ['周一','周二','周三','周四','周五','周六','周日'],
                    axisLine:{
                        lineStyle:{
                            type:'dashed',
                            color:'#BFBFBF',
                            width:0,   //这里是坐标轴的宽度,可以去掉
                        }
                    },
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    axisLine:{
                        lineStyle:{
                            type:'dashed',
                            color:'#BFBFBF',
                            width:0,   //这里是坐标轴的宽度,可以去掉
                        }
                    },
                    splitLine: {
                        show: true,
                        lineStyle: {
                            color: '#ccc',
                            type:'dashed',
                        }
                    },
                }
            ],
            series : [
                {
                    name:'直流输电换流阀水冷设备',
                    type:'line',
                    stack: '总量',

                    data:[120, 132, 101, 134, 90, 230, 210]
                },
                {
                    name:'柔性交流输配电晶闸管阀纯水冷却设备',
                    type:'line',
                    stack: '总量',

                    data:[220, 182, 191, 234, 290, 330, 310]
                },
                {
                    name:'新能源发电变流器纯水冷却设备',
                    type:'line',
                    stack: '总量',

                    data:[150, 232, 201, 154, 190, 330, 410]
                },
                {
                    name:'大功率电气传动变频器纯水冷却设备',
                    type:'line',
                    stack: '总量',

                    data:[320, 332, 301, 334, 390, 330, 320]
                },
                {
                    name:'蓄冷设备',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },

                    data:[820, 932, 901, 934, 1290, 1330, 1320]
                }
            ]
        };
        myChart.hideLoading();
        myChart.setOption(option, true);
    }
    
    loadyys2();
    
    $('select').selectOrDie({

    });
})