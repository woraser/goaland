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
           	{label:$.i18n.prop('iotxData.collectTime'),name:'collectTime',index:'collectTime', width: '500',  align: 'center'},
      		{label:$.i18n.prop('iotxData.val'),name:'val',index:'val', width: '500',  align: 'center'},
      	];
      	
  	 //每页显示多少行
  	 var rowNum=8;
  	 var page=0;
  	 var url='/iotxData/management/data/GRID';
  	 var sort;
  	 
  	 //请求参数
  	 var params={}
  	 //设置请求需要的一些参数
  	 params['rowId']='id'
  	 params['showAttributes']='collectTime,val';//要获取的属性名
  	 params['page']=page;
  	 params['size']=rowNum;
  	 params['sort']=sort;
  	 params['sensorSN']=$("#serialNo").val();
  	 
  	 var myGrid = jQuery("#allDataTable");
	 var myPager = jQuery("#allDataPager");
	 
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
})