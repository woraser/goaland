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
		               	{label:'iotx序列号',name:'iotxSN', index:'iotxSN', width:'120', sortable: false,align: 'center'},
						{label:'传感器序列号',name:'sensorSN',index:'sensorSN', width: '120', sortable:false, align: 'center'},
						{label:'类型',name:'category', index:'category', width:'120', sortable: false,align: 'center'},
						{label:'数值',name:'val', index:'val', width:'120', sortable: false,align: 'center'},
						{label:'上限阈值',name:'maxVal', index:'maxVal', width:'120', sortable: false,align: 'center'},
						{label:'下限阈值',name:'minVal', index:'minVal', width:'120', sortable: false,align: 'center'},
						{label:'告警信息',name:'message', index:'message', width:'120', sortable: false,align: 'center'},
						{label:'等级',name:'level', index:'level', width:'120', sortable: false,align: 'center'},
						{label:'采集时间',name:'collectTime', collectTime:'minVal', width:'120', sortable: false,align: 'center',
							formatter:'date', 
							formatoptions:{srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'}
						},
						{label:'关闭时间',name:'closeTime', index:'closeTime', width:'120', sortable: false,align: 'center',
							formatter:'date', 
							formatoptions:{srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'}
						},
						{
		                    label:'状态', name: 'operate', index: 'operate', width: 120, align:'center',
		                    formatter: function (cellvalue, options, rowObject) {
		                    	var detail;
		                    	if(rowObject.closeTime==null){
		                    		detail="<img id='open0' height='35' width='35' src='/webResources/img/icon/uncomfirm.png'/>&nbsp;&nbsp;&nbsp;<button  class='btn btn-small btn-primary btn-xs' style='margin:4px;' id='fileId5' onclick='confirm()'>确认</button>"
		                    	}else{
		                    		detail="<img id='open1' height='35' width='35' src='/webResources/img/icon/comfirm.png'>&nbsp;&nbsp;&nbsp;<button  class='btn btn-small btn-primary btn-xs' style='margin:4px;' id='fileId5' >确认</button>" 
		                    	}
		                        return detail;
		                    },
		                },
		    	   	  ];
		 
		 //每页显示多少行
		 var rowNum=20;
		 var page=0;
		 var url='/sensor/management/data';
		 var sort;
		 
		 //请求参数
		 var params={}
		 //设置请求需要的一些参数
		 params['rowId']='id'
		 params['showAttributes']='iotxSN,sensorSN,category,val,maxVal.minVal,message,level,collectTime,closeTime';//要获取的属性名
		 params['page']=page;
		 params['size']=rowNum;
		 params['sort']=sort;
		 
		 if($("#sensorSN")){
			 params['sensorSN']=$("#sensorSN").val();
		 }
		 
		 var myGrid = jQuery("#iotxDataTable");
		 var myPager = jQuery("#iotxDataPager");
		 
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
	    	   		createPage(myGrid,myPager,lastPage,page,11,url,params);//调用自定义的方法来生成pager
		    	},
		 
			 	//当触发排序时
		    	onSortCol:function(index,iCol,sortorder){
		    		params['sort']=index+","+sortorder;
		    		myGrid.jqGrid().setGridParam({
						url:url,
						postData:params,
					}).trigger("reloadGrid");
		    	},
	    		
	    	});
		 
	});