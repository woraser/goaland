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
	    {label:$.i18n.prop('materiel.number'),name:'name',index:'name', width: '120',  align: 'center'},
  	    {label:$.i18n.prop('materiel.name'),name:'name',index:'name', width: '120',  align: 'center'},
     	{label:$.i18n.prop('materiel.beginTime'),name:'beginTime',index:'beginTime', width: '120',  align: 'center'},
		{label:$.i18n.prop('materiel.lastCheckTime'),name:'lastCheckTime',index:'lastCheckTime', width: '120',  align: 'center'},
		{label:$.i18n.prop('materiel.checkDate'),name:'checkDate', index:'checkDate',sortable: false, width:'120', align: 'center'},
		{label:$.i18n.prop('materiel.remindDate'),name:'remindDate', index:'remindDate',sortable: false, width:'120', align: 'center'},
		{label:$.i18n.prop('materiel.status'),name:'status', index:'status', width:'120', align: 'center',
			formatter: function (cellvalue, options, rowObject) {
	          	var detail = ""
	          	if(cellvalue=="NORMAL"){
	          		detail+="<img src='/webResources/img/device/material/clock2.png' style='margin-right: 10%;'>"
	          	}else if(cellvalue=="OVER"){
	          		detail+="<img src='/webResources/img/device/material/clock1.png' style='margin-right: 10%;'>"
	          	}else if(cellvalue=="REMIND"){
	          		detail+="<img src='/webResources/img/device/material/clock3.png' style='margin-right: 10%;'>"
	          	}
	          	detail += "<span style='color:#4F6A7F;'>"+$.i18n.prop('materiel.status.'+cellvalue)+"</span>";
	          	return detail;
	          },
	    },
		{
          label:$.i18n.prop('operate'), name: 'operate', index: 'operate', width: 150,sortable: false, align:'center',
          formatter: function (cellvalue, options, rowObject) {
          	var detail = "";
          	detail += "<a href='#' edit='"+options.rowId+"'><img src='/webResources/img/device/material/edit_normal.png'/></a>"
      		detail += "<a href='#' delete='"+options.rowId+"'><img src='/webResources/img/device/material/delete_normal.png'/></a>"
          	return detail;
          },
		},
	];
    	
	 //每页显示多少行
	 var rowNum=10;
	 var page=0;
	 var url='/materiel/management/data/GRID';
	 var sort;
	 var selectRowId;
	 
	 //请求参数
	 var params={}
	 //设置请求需要的一些参数
	 params['rowId']='id';
	 params['showAttributes']='number,name,beginTime,lastCheckTime,checkDate,remindDate,status,id';//要获取的属性名
	 params['page']=page;
	 params['size']=rowNum;
	 params['sort']=sort;
	 params['device.id']=$("#deviceId").val()
	 params['deviceSN']=$("#deviceSN").val()
	 params['searchContent']=null
	 
	 var materielSearch = new Vue({
	   el: '#materielSearch',
	   data: params,
	   methods:{
		   downloadTemplate : function(){
			   window.location.href="/webResources/templates/materiel.xlsx"
		   },
		   batchSave : function(){
		     var func=function(){
				 if($("#excelForm").valid()){
					 $("#excelForm").submit();
					 return true;
				 }else{
					 return false;
				 }
			 };
			 createModalPage("批量上传","/materiel/save/batch/view",func); 
		   }
	   },
	 })
	 
	 var myGrid = jQuery("#materielTable");
  	 var myPager = jQuery("#materielPager");
  	 
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
  	 
  	 //add按钮被点击
	 $("#addMateriel").click(function(){
		 var func=function(){
			 if($("#materielForm").valid()){
				 $("#materielForm").submit();
				 return true;
			 }else{
				 return false;
			 }
		 };
		 createModalPage("添加物料","/materiel/save?deviceId="+$("#deviceId").val(),func); 
	 });
	 
	// 编辑按钮绑定事件
	 $(document).on("click","a[edit]",function(){
		 var func=function(){
			 if($("#materielForm").valid()){
				 $("#materielForm").submit();
				 return true;
			 }else{
				 return false;
			 }
		 };
		 createModalPage("编辑物料","/materiel/save?id="+$(this).attr("edit")+"&deviceId="+$("#deviceId").val(),func); 
	 });
	 
	 // 删除按钮绑定事件
	 $(document).on("click","a[delete]",function(){
		 var id = $(this).attr("delete");
		 var func = function(){
			 $.blockUI({
				message: '<div class="lds-css ng-scope"><div class="lds-spinner" style="100%;height:100%"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div></div>',
				// 指的是提示框的css
				css: {
                    width: "0px",
                    top: "40%",
                    left: "50%"
                },
			 });
			 $.ajax({
				url : '/materiel/delete',
				data : {
					'id' : id,
				},
				type : 'post',
				dataType : 'json',
				success : function(msg) {
					$.unblockUI();
					if(msg.result=='success'){
						info('操作成功');
						$("#materielTable").trigger("reloadGrid");
					}else if(msg.result=='error'){
						warning('操作失败:'+msg.message);
					}else{
						warning('操作失败');
					}
				}
			});
		 };
		 confirm("是否删除",func);
	 });
	 
	 $("#searchMateriel").click(function(){
		 myGrid.jqGrid().setGridParam({
				url:url,
				postData:params,
		 }).trigger("reloadGrid");
	 })
	
})