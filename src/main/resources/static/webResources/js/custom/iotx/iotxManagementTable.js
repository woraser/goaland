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
		               	{label:$.i18n.prop('iotx.serialNo'),name:'serialNo', index:'serialNo', width:'120', align: 'center'},
		               	{label:$.i18n.prop('iotx.company'),name:'company.name',index:'company.name', width: '120',  align: 'center'},
						{label:$.i18n.prop('iotx.installLocation'),name:'installLocation',index:'installLocation', width: '120', sortable:false, align: 'center'},
						{label:$.i18n.prop('iotx.sensorQuantity'),name:'sensorQuantity', index:'sensorQuantity',sortable: true, width:'120', align: 'center'},
						{label:$.i18n.prop('iotx.alarmQuantity'),name:'alarmQuantity', index:'alarmQuantity',sortable: true, width:'120', align: 'center'},
						{label:$.i18n.prop('iotx.openTime'),name:'openTime', index:'openTime',sortable: true, width:'120', align: 'center'},
						{label:$.i18n.prop('iotx.status'),name:'status', index:'status',sortable: true, width:'120', align: 'center'},
						{
			                label:$.i18n.prop('operate'), name: 'operate', index: 'operate', width: 90,sortable: false, align:'center',
			                formatter: function (cellvalue, options, rowObject) {
			                	var hrefUrl='/iotx/management/detail/'+options.rowId+"/view";
		                        var detail;
		                    	var detailImg = "<a href="+hrefUrl+"><img src='/webResources/img/operate/detail.png'/></a>"
		                    	detail = detailImg
		                    	return detail;
			                },
			            },
				   	  ];
		 
		 //每页显示多少行
		 var rowNum=8;
		 var page=0;
		 var url='/iotx/management/data/GRID';
		 var sort;
		 var selectRowId;
		 
		 //请求参数
		 var params={}
		 params['rowId']='id'
		 params['showAttributes']='serialNo,company.name,installLocation,sensorQuantity,alarmQuantity,openTime,status.status';//要获取的属性名
		 params['page']=page;
		 params['size']=rowNum;
		 params['sort']=sort;
		 params['searchContent']=null;
		 
		 var iotxSearch = new Vue({
		   el: '#iotxSearch',
		   data: params,
		   methods:{
			  empty : function(){
				  $("#beginTime").val("")
				  $("#endTime").val("")
			  }, 
		   },
		 })
		 
		 $("#beginTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
		 $("#endTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
		 
		 var myGrid = jQuery("#iotxTable");
		 var myPager = jQuery("#iotxPager");
		 
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
		 
		 //查询按钮点击事件
		 $('#search').click(function(){
			myGrid.jqGrid().setGridParam({
				url:url,
				postData:params,
			}).trigger("reloadGrid");
		 })
		 
		 $("#confirm").click(function(){
			 params['beginTime']=$("#beginTime").val();
			 params['endTime']=$("#endTime").val();
			 myGrid.jqGrid().setGridParam({
					url:url,
					postData:params,
			 }).trigger("reloadGrid");
		 })
		 
	});