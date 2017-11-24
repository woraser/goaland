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
           	{label:$.i18n.prop('advertisement.name'),name:'name',index:'name', width: '120', align: 'center'},
			{label:$.i18n.prop('advertisement.creater'),name:'creater.name',index:'creater.name', width: '120', align: 'center'},
			{label:$.i18n.prop('advertisement.createTime'),name:'createTime', index:'createTime', width:'120', align: 'center'},
			{label:$.i18n.prop('advertisement.sendTime'),name:'sendTime', index:'sendTime', width:'120', align: 'center'},
			{
	            label:$.i18n.prop('operate'), name: 'operate', index: 'operate', width: 150,sortable: false, align:'center',
	            formatter: function (cellvalue, options, rowObject) {
	            	var url = "/advertisement/content/edit?id="+options.rowId
	            	var detail = "<a href="+url+"><img src='/webResources/img/operate/edit.png'/></a>"
	            	return detail;
	            },
	        },
	   	  ];
		 
		 //每页显示多少行
		 var rowNum=20;
		 var page=0;
		 var url='/advertisement/management/data/GRID';
		 var sort;
		 
		 //请求参数
		 var params={}
		 //设置请求需要的一些参数
		 params['rowId']='id'
		 params['showAttributes']='name,creater.name,createTime,sendTime,id';//要获取的属性名
		 params['page']=page;
		 params['size']=rowNum;
		 params['sort']=sort;
		 params['searchContent']=null;
		 
		 var contentSearch = new Vue({
			   el: '#contentSearch',
			   data: params,
		 })
		 
		 var myGrid = jQuery("#advertisementTable");
		 var myPager = jQuery("#advertisementPager");
		 
		 myGrid.jqGrid({
    		datatype: "json",
    		url:url,
    		postData:params,
    		height: '100%',
    	   	colModel:colModel,
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
		 
		 //add按钮被点击
		 $("#add").click(function(){
			 var func=function(){
				 if($("#advertisementForm").valid()){
					 $("#advertisementForm").submit();
					 return true;
				 }else{
					 return false;
				 }
			 };
			 createModalPage("创建广告","/advertisement/save",func); 
		 })
		 
		 $("#search").click(function(){
			 myGrid.jqGrid().setGridParam({
					url:url,
					postData:params,
			 }).trigger("reloadGrid");
		 })
		 
	});