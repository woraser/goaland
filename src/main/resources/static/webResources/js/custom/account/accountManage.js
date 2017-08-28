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
		               	{label:'账号',name:'loginId',index:'loginId', width: '120', sortable:false, align: 'center'},
						{label:'用户名',name:'name',index:'name', width: '120', sortable:false, align: 'center'},
						{label:'部门',name:'role.depGroup.department.name', index:'role.depGroup.department.name', width:'120', sortable: false,align: 'center'},
						{label:'组',name:'role.depGroup.name', index:'role.depGroup.name',sortable: true, width:'120', align: 'center'},
						{label:'角色',name:'role.name', index:'role.name',sortable: true, width:'120', align: 'center'},
				   	  ];
		 
		 //每页显示多少行
		 var rowNum=20;
		 var page=0;
		 var url='/account/management/data/GRID';
		 var sort;
		 var selectRowId;
		 
		 //请求参数
		 var params={}
		 //设置请求需要的一些参数
		 params['rowId']='id'
		 params['showAttributes']='loginId,name,role.name,role.depGroup.name,role.depGroup.department.name';//要获取的属性名
		 params['page']=page;
		 params['size']=rowNum;
		 params['sort']=sort;
		 
		 var myGrid = jQuery("#accountTable");
		 var myPager = jQuery("#accountPager");
		 
		 myGrid.jqGrid({
	    		datatype: "json",
	    		url:url,
	    		postData:params,
	    		height: '100%',
	    	   	colModel:colModel,
	    	   	caption:'用户信息管理',
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
	    	   		createPage(myGrid,myPager,lastPage,page,11,url,params);//调用自定义的方法来生成pager
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
				 if($("#accountForm").valid()){
					 $("#accountForm").submit();
					 return true;
				 }else{
					 return false;
				 }
			 };
			 createModalPage("添加用户","/account/save",func); 
		 })
		 
		 //add和edit按钮被点击
		 $("#edit").click(function(){
			 selectRowId = myGrid.getGridParam('selarrrow');
			 if(selectRowId!=null&&selectRowId!=""){
				 var func=function(){
					 if($("#accountForm").valid()){
						 $("#accountForm").submit();
						 return true;
					 }else{
						 return false;
					 }
				 };
				 createModalPage("编辑用户","/account/save?id="+selectRowId,func); 
			 }else{
				 warning("编辑时必须选择一行");
			 }
		 })
		 
	});