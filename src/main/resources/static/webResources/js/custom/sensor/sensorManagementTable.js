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
		               	{label:'序列号',name:'serialNo', index:'serialNo', width:'120', sortable: false,align: 'center'},
						{label:'所属iotx',name:'iotx.serialNo',index:'iotx.serialNo', width: '120', sortable:false, align: 'center'},
						{label:'接口',name:'sensorPort.sensorInterface.name', index:'sensorPort.sensorInterface.name', width:'120', sortable: false,align: 'center'},
						{label:'端口',name:'sensorPort.name', index:'sensorPort.name', width:'120', sortable: false,align: 'center'},
						{label:'传感器种类',name:'sensorCategory.name', index:'sensorCategory.name', width:'120', sortable: false,align: 'center'},
						{label:'告警数量',name:'alarmQuantity', index:'alarmQuantity', width:'120', sortable: false,align: 'center'},
						{
		                    label:'操作', name: 'operate', index: 'operate', width: 50, align:'center',
		                    formatter: function (cellvalue, options, rowObject) {
		                    	var hrefUrl='/sensor/management/detail/'+options.rowId;
		                        var detail="<input value='详情' type='button' onclick='window.location.href=\""+hrefUrl+"\"' class='btn btn-small btn-primary btn-xs' style='background:#434A5D;border-color:#0192D7;color:white;border-radius:15px 15px;width:60px'/>";
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
		 if(!$('#isAdmin').length > 0){
			 params['iotx.company.code']=$("#companyCode").val();
		 }
		 params['rowId']='id'
		 params['showAttributes']='serialNo,iotx.serialNo,sensorPort.sensorInterface.name,sensorPort.name,sensorCategory.name,alarmQuantity';//要获取的属性名
		 params['page']=page;
		 params['size']=rowNum;
		 params['sort']=sort;
		 
		 if($('#iotxId').length > 0){
			params['iotx.id']=$('#iotxId').val();
		 }
		 
		 var myGrid = jQuery("#sensorTable");
		 var myPager = jQuery("#sensorPager");
		 
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
		 
		 //add和edit按钮被点击
		 $("#add").click(function(){
			 var func=function(){
				 if($("#sensorForm").valid()){
					 $("#sensorForm").submit();
					 return true;
				 }else{
					 return false;
				 }
			 };
			 createModalPage("添加传感器","/sensor/saveSensor",func); 
		 })
		 
		 //add和edit按钮被点击
		 $("#edit").click(function(){
			 selectRowId = myGrid.getGridParam('selarrrow');
			 if(selectRowId!=null&&selectRowId!=""){
				 var func=function(){
					 if($("#sensorForm").valid()){
						 $("#sensorForm").submit();
						 return true;
					 }else{
						 return false;
					 }
				 };
				 createModalPage("编辑传感器","/sensor/saveSensor?id="+selectRowId,func); 
			 }else{
				 warning("编辑时必须选择一行");
			 }
		 })
		 
		 $("#delete").click(function(){
			 selectRowId = myGrid.getGridParam('selarrrow');
			 
			 if(selectRowId!=null&&selectRowId!=""){
				 //定义删除的请求
				 var func=function(){
					 $.blockUI({message: '<img src="/webResources/img/loading/loading.gif" /> '});
					 /*----ajax---*/
					 $.ajax({
						url : '/sensor/deleteSensor?id='+selectRowId,
						type : 'post',
						dataType : 'json',
						success : function(data) {
							$.unblockUI();
							if(data.result=='success'){
								info('操作成功');
								myGrid.jqGrid().trigger("reloadGrid");
							}else{
								warning('操作失败，请联系管理员');
							}
						}
					});
					/*----ajax---*/ 
				 }
				 confirm('确定要删除这条记录么?',func);
			 }else{
				 warning("删除时必须选择一行");
			 }
		 })
		 
		 //search autocomplete
		 $( "#toSearch" )
	      // 当选择一个条目时不离开文本域
	      .bind( "keydown", function( event ) {
	        if ( event.keyCode === $.ui.keyCode.TAB &&
	            $( this ).data( "ui-autocomplete" ).menu.active ) {
	          event.preventDefault();
	        }
	      })
	      .autocomplete({
	        source: function( request, response ) {
	        	
	          $.ajax({
					url : '/sensor/autocomplete',
					data : {
						'label' : 'serialNo',
						'value' : 'id',
						'serialNo' : request.term,
						'iotx.company.code' : ($('#isAdmin').length > 0?null:$("#companyCode").val()),
					},
					type : 'get',
					dataType : 'json',
					async : false,
					success : function( datas ) {
						response($.each(datas,function(i,value) {
							return {
								label : this.label,
								value : this.value,
							}
						}));
					}
				
	          });
	          
	        },
	        search: function() {
	          // 自定义最小长度
	          var term = this.value;
	          if ( term.length < 3 ) {
	            return false;
	          }
	        },
	        focus: function() {
	          // 防止在获得焦点时插入值
	          return false;
	        },
	        select: function( event, ui ) {
	          console.info(ui.item);
	          $( "#toSearch" ).val(ui.item.label);
	          $( "#toSearchValue" ).val(ui.item.value);
	          return false;
	        }
	      }); 
		 
		 //查询按钮点击事件
		 $('#searchBtn').click(function(){
			var search = $('#toSearchValue').val();
			if(search!=null&&search!=''){
				params['id']=search;
				myGrid.jqGrid().setGridParam({
					url:url,
					postData:params,
				}).trigger("reloadGrid");
				//查询完成后清掉这个参数
				delete params['id'];
			}else{
				warning('请先填写要查找的序列号');
			}
		 })
		 
		 //确定按钮点击事件
		 $('#confirm').click(function(){
			params['sensorCategory.id'] = ($('#sensorCategoryOption').val()==0)?null:$('#sensorCategoryOption').val();
			params['sensorInterface.id'] = ($('#sensorInterfaceOption').val()==0)?null:$('#sensorInterfaceOption').val();
			params['sensorPort.id'] = ($('#sensorPortOption').val()==0)?null:$('#sensorPortOption').val();
			params['page']=0; 
			
			myGrid.jqGrid().setGridParam({
				url:url,
				postData:params,
			}).trigger("reloadGrid");
			
		});
	   
		//清空按钮点击事件
		$("#empty").click(function(){
			$("#sensorCategoryOption").val(0);
			$("#sensorInterfaceOption").val(0);
			$("#sensorPortOption").val(0);
			
			delete params['sensorCategory.id'];
			delete params['sensorInterface.id'];
			delete params['sensorPort.id'];
			
		});
		 
	});