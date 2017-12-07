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
	    {label:$.i18n.prop('device.technicalParameter.name'),name:'name',index:'name', width: '150',  align: 'center'},
       	{label:$.i18n.prop('device.technicalParameter.parameterDescribe'),name:'parameterDescribe',index:'parameterDescribe', width: '150',  align: 'center'},
  		{label:$.i18n.prop('device.technicalParameter.actualValue'),name:'actualValue',index:'actualValue', width: '150',  align: 'center'},
  		{label:$.i18n.prop('device.technicalParameter.unit'),name:'unit', index:'unit', width:'150', align: 'center'},
  		{
            label:$.i18n.prop('operate'), name: 'operate', index: 'operate', width: 150,sortable: false, align:'center',
            formatter: function (cellvalue, options, rowObject) {
            	var url = "/sensor/management/detail/"+ cellvalue +"/view"
            	var detail = "<a href="+ url +"><img src='/webResources/img/operate/detail.png'/></a>";
            	return detail;
            },
        },
  	];
  	
  	 //每页显示多少行
  	 var rowNum=6;
  	 var page=0;
  	 var url='/sensor/management/data/GRID';
  	 var sort;
  	 
  	 //请求参数
  	 var params={}
  	 //设置请求需要的一些参数
  	 params['rowId']='id';
  	 params['showAttributes']='name,parameterDescribe,actualValue,unit,serialNo';//要获取的属性名
  	 params['page']=page;
  	 params['size']=rowNum;
  	 params['sort']=sort;
  	 params['actual']=true;
  	 params['dust.device.serialNo']=$("#deviceSN").val();
  	 
  	var paging = new Vue({
 	   el: '#paging',
 	   data: {
 		   first:false,
 		   last:false,
 	   },
 	   methods: {
 		  nextPageClick : function(){
 			 page+=1
 			 params['page'] = page
 			 myGrid.jqGrid().setGridParam({
 				url:url,
 				postData:params,
 			 }).trigger("reloadGrid");
 		 },
 		 lastPageClick : function(){
 			 page-=1
 			 params['page'] = page
 			 myGrid.jqGrid().setGridParam({
 				url:url,
 				postData:params,
 			 }).trigger("reloadGrid");
 		 }
 	   },
 	})
  	 
  	 var myGrid = jQuery("#parameterTable");
	 var myPager = jQuery("#parameterPager");
	 
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
	   		//判断是否第一页
	   		if(page==0){
	   			paging.first=false
	   		}else{
	   			paging.first=true
	   		}
	   		//判断是否最后一页
	   		if(page==lastPage-1){
	   			paging.last=false
	   		}else{
	   			paging.last=true
	   		}
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