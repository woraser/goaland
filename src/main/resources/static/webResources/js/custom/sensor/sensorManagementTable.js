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
               	{label:$.i18n.prop('sensor.serialNo'),name:'serialNo', index:'serialNo', width:'120', align: 'center'},
				{label:$.i18n.prop('sensor.alarmQuantity'),name:'alarmQuantity', index:'alarmQuantity', width:'120', align: 'center'},
				{label:$.i18n.prop('sensor.unConfirmAlarmQuantity'),name:'unConfirmAlarmQuantity', index:'unConfirmAlarmQuantity', width:'120',align: 'center'},
				{label:$.i18n.prop('sensor.belong.serialNo'),name:'dust.iotx.serialNo', index:'dust.iotx.serialNo', width:'120', align: 'center'},
				{label:$.i18n.prop('dust.frequency'),name:'dust.frequency', index:'dust.frequency', width:'120',align: 'center'},
				{label:$.i18n.prop('sensor.maxVal'),name:'maxVal', index:'maxVal', width:'120', align: 'center'},
				{label:$.i18n.prop('sensor.minVal'),name:'minVal', index:'minVal', width:'120', align: 'center'},
				{label:$.i18n.prop('sensor.isWorked'),name:'isWorked', index:'isWorked', width:'120', sortable: false,align: 'center'},
				{label:$.i18n.prop('dust.device'),name:'dust.device.serialNo', index:'dust.device.serialNo', width:'120',align: 'center'},
				{
                    label:$.i18n.prop('operate'), name: 'operate', index: 'operate', width: 50, sortable: false, align:'center',
                    formatter: function (cellvalue, options, rowObject) {
                    	var hrefUrl='/sensor/management/detail/'+options.rowId+"/view";
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
		 var url='/sensor/management/data/GRID';
		 var sort;
		 
		 //请求参数
		 var params={}
		 params['rowId']='serialNo'
		 params['showAttributes']='serialNo,alarmQuantity,unConfirmAlarmQuantity,dust.iotx.serialNo,dust.frequency,maxVal,minVal,isWorked,dust.device.serialNo';//要获取的属性名
		 params['page']=page;
		 params['size']=rowNum;
		 params['sort']=sort;
		 params['searchContent']=null;
		 
		 var sensorSearch = new Vue({
		   el: '#sensorSearch',
		   data: params,
		   methods:{
			   
		   },
		 })
		 
		 if($('#iotxId').length > 0){
			params['dust.iotx.id']=$('#iotxId').val();
			params['iotxId']=$('#iotxId').val();
		 }
		 
		 var myGrid = jQuery("#sensorTable");
		 var myPager = jQuery("#sensorPager");
		 
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
		    	},
	    		
	    	});
		 
		 //查询按钮点击事件
		 $('#search').click(function(){
			myGrid.jqGrid().setGridParam({
				url:url,
				postData:params,
			}).trigger("reloadGrid");
		 })
		 
	});