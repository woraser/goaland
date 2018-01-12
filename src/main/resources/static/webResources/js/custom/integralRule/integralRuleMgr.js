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
     	{label:$.i18n.prop('integralRule.key'),name:'key',index:'key', width: '60',  align: 'center'},
        {label:$.i18n.prop('integralRule.ruleContext'),name:'ruleContext',index:'ruleContext', width: '60',  align: 'center'},
        {label:$.i18n.prop('integralRule.express'),name:'express',index:'express', width: '180',  align: 'center'},
        {label:$.i18n.prop('integralRule.note'),name:'note',index:'note', width: '180',  align: 'center'},
		{
            label:$.i18n.prop('operate'), name: 'operate', index: 'operate', width: 60,sortable: false, align:'center',
            formatter: function (cellvalue, options, rowObject) {
            	var detail = "<a href='#' edit='"+options.rowId+"'><img src='/webResources/img/operate/edit.png'/></a>"
            	return detail;
            },
        },
	];
	
	 //每页显示多少行
	 var rowNum=20;
	 var page=0;
	 var url='/integralRule/management/data/GRID';
	 var sort;
	 var selectRowId;
	 
	 //请求参数
	 var params={}
	 //设置请求需要的一些参数
	 params['rowId']='id'
	 params['showAttributes']='key,ruleContext,express,note,id';//要获取的属性名
	 params['page']=page;
	 params['size']=rowNum;
	 params['sort']=sort;

	 var myGrid = jQuery("#integralRuleTable");
	 var myPager = jQuery("#integralRulePager");
	 
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

	 // 添加
	 $("#add").click(function(){
         var func=function(){
             if($("#ruleForm").valid()){
                 $("#ruleForm").submit();
                 return true;
             }else{
                 return false;
             }
         };
         createModalPage("添加积分规则","/integralRule/save",func);
	 })

	 // 编辑按钮绑定事件
	 $(document).on("click","a[edit]",function(){
		 var func=function(){
			 if($("#ruleForm").valid()){
				 $("#ruleForm").submit();
				 return true;
			 }else{
				 return false;
			 }
		 };
		 createModalPage("修改积分规则","/integralRule/save?id="+$(this).attr("edit"),func);
	 });
	 
})