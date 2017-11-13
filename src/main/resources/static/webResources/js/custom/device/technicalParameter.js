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
  	];
  	
  	 //每页显示多少行
  	 var rowNum=6;
  	 var page=0;
  	 var url='/sensor/management/data/GRID';
  	 var sort;
  	 var selectRowId;
  	 
  	 //请求参数
  	 var params={}
  	 //设置请求需要的一些参数
  	 params['rowId']='id';
  	 params['showAttributes']='name,parameterDescribe,actualValue,unit';//要获取的属性名
  	 params['page']=page;
  	 params['size']=rowNum;
  	 params['sort']=sort;
  	 params['actual']=true;
  	 params['dust.device.serialNo']=$("#deviceSN").val();
  	 
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