/**
 * 
 */
$(document).ready(function() {
	//不使用jqgrid默认的参数
	$.extend(jQuery.jgrid.defaults, {
	    prmNames: {
	        id: "_rowid", page: "_page", rows: "_rows",
	        oper: "_oper", sort: "_sidx", order: "_sord"
	    }
	});
	
	var colModel=[
         	{label:$.i18n.prop('iotxData.collectTime'),name:'collectTime',index:'collectTime', width: '150',  align: 'center'},
         	{
         		label:$.i18n.prop('iotxData.level'),name:'val',index:'val', width: '120',sortable: false,  align: 'center',
    			formatter: function (cellvalue, options, rowObject) {
    				var detail = $.i18n.prop('iotxData.' + cellvalue)
                    return detail;
                },
    		},
    		{
    			label:$.i18n.prop('operate'),name:'val',index:'val', width: '150',sortable: false,  align: 'center',
    			formatter: function (cellvalue, options, rowObject) {
    				var detail;
                	if(cellvalue==null){
                		detail = '<button style="color:#FFFFFF;border:0px;width: 60%;height: 25px;background: #FF955E;border-radius: 5px" confirm="'+options.rowId+'">确认</button>'
                	}else{
                		detail = '<button style="cursor:default;color:#FFFFFF;border:0px;width: 60%;height: 25px;background: #B6B6BA;border-radius: 5px">已确认</button>'
                	}
    				return detail;
                },
    		},
    	];
    	
	 //每页显示多少行
	 var rowNum=8;
	 var page=0;
	 var url='/alarmData/management/data/GRID';
	 var sort;
	 
	 //请求参数
	 var params={}
	 //设置请求需要的一些参数
	 params['rowId']='id'
	 params['showAttributes']='collectTime,level,closeTime';//要获取的属性名
	 params['page']=page;
	 params['size']=rowNum;
	 params['sort']=sort;
	 params['sensor.serialNo']=$("#serialNo").val();
	 
	 var myGrid = jQuery("#alarmDataTable");
  	 var myPager = jQuery("#alarmDataPager");
  	 
  	 myGrid.jqGrid({
  		datatype: "json",
  		url:url,
  		postData:params,
  		height: '100%',
  	   	colModel:colModel,
  	   	multiselect: false,
  	   	multiboxonly: true,
  	   	multiselectWidth: 30,
  	   	rowNum: rowNum,
  	   	autowidth: true,
  	   	forceFit: false,
  	   	altRows: false,
  	   	viewrecords: true,
  	   	
  	   	gridComplete:function(){
  	   	 	var lastPage = myGrid.getGridParam('lastpage');//获取总页数
  	   		createPage(myGrid,myPager,lastPage,params.page,11,url,params);//调用自定义的方法来生成pager
  	    },
  	    	
  	   	//当触发排序时
  	   	onSortCol:function(index,iCol,sortorder){
  	   		params['sort']=index+","+sortorder;
  	   		myGrid.jqGrid().setGridParam({
  					url:url,
  					postData:params,
  				}).trigger("reloadGrid");
  	   	}
  		    	
      });
  	 
  	$(document).on("click","button[confirm]",function(){
		 $.ajax({
			type:"post",
			url:'/alarmData/save',
			data : {
				'alarmDataId' : $(this).attr("confirm"),
				'closeTime' : new Date().format("yyyy-MM-dd HH:mm:ss"),
			},
			async:true,
			success:function(data){
				myGrid.jqGrid().setGridParam({
					url:url,
					postData:params,
				}).trigger("reloadGrid");
			}
		 });
	 });
	
})