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
           	{label:$.i18n.prop('roleFunctionGroup.name'),name:'name',index:'name', width: '120', sortable:false, align: 'center'},
			{label:$.i18n.prop('roleFunctionGroup.roleFunction'),name:'roleFunction',index:'roleFunction', width: '120', sortable:false, align: 'center',
				formatter: function (cellvalue, options, rowObject) {
                    var detail="";
                    $.each(cellvalue,function(){
                    	detail += this.name+"  ";
                    })
                    return detail;
                }
           	},
	   	  ];
		 
		 //每页显示多少行
		 var rowNum=20;
		 var page=0;
		 var url='/roleFunctionGroup/management/data/GRID';
		 var sort;
		 var selectRowId;
		 
		 //请求参数
		 var params={}
		 //设置请求需要的一些参数
		 params['rowId']='id'
		 params['showAttributes']='name,roleFunctionList*.name';//要获取的属性名
		 params['page']=page;
		 params['size']=rowNum;
		 params['sort']=sort;
		 
		 var myGrid = jQuery("#roleFunctionGroupTable");
		 var myPager = jQuery("#roleFunctionGroupPager");
		 
		 myGrid.jqGrid({
	    		datatype: "json",
	    		url:url,
	    		postData:params,
	    		height: '100%',
	    	   	colModel:colModel,
	    	   	multiselect: true,
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
		 
		 //add和edit按钮被点击
		 $("#add").click(function(){
			 var func=function(){
				 if($("#roleFunctionGroupForm").valid()){
					 $("#roleFunctionGroupForm").submit();
					 return true;
				 }else{
					 return false;
				 }
			 };
			 createModalPage("添加权限组","/roleFunctionGroup/save",func); 
		 })
		 
		 //add和edit按钮被点击
		 $("#edit").click(function(){
			 selectRowId = myGrid.getGridParam('selarrrow');
			 if(selectRowId!=null&&selectRowId!=""){
				 var func=function(){
					 if($("#roleFunctionGroupForm").valid()){
						 $("#roleFunctionGroupForm").submit();
						 return true;
					 }else{
						 return false;
					 }
				 };
				 createModalPage("编辑权限组","/roleFunctionGroup/save?id="+selectRowId,func); 
			 }else{
				 warning("编辑时必须选择一行");
			 }
		 })
		 
	});