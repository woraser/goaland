/**
 * 
 */
$(document).ready(function() {
	var objectId = null;
	 var hotbox = null;
	 
	 seajs.config({
	    base: '/webResources/plugins/hotbox/src'
	 });
		
	//初始化
	seajs.use(['hotbox'],function (hotbox){
		hotbox = new hotbox('#hotbox');
	    hotbox.control();
	    hotbox.hintDeactiveMainState = true;
	    hotbox.openOnContextMenu = true;
	    var main = hotbox.state('main');
	    var ringButtons = '下载|预览|附件信息'.split('|');
	    ringButtons.forEach(function(button) {
	        main.button({
	            position: 'ring',
	            label: button,
				action:function(){
					if(button=="下载"){
						window.location.href="/fileDownload/"+objectId
					}else if(button=="预览"){
						$("#"+objectId).click()
					}else if(button=="附件信息"){
						createModalPageToView("附件信息","/fileMetaData/"+objectId+"/view")
					}
					objectId = null;
				},
				enable:function(){
					if(objectId==null){
						return false;							
					}else{
						return true;	
					}
				}
	        });
	    });
	    
	    //右键显示
	    $(document).on("contextmenu","#existFiles",function(event){
	       // 获取相对文档坐标
		   var e = event || window.event;
           var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
           var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
           var x = e.pageX || e.clientX + scrollX;
           var y = e.pageY || e.clientY + scrollY;
           hotbox.active('main', { x: x, y: y });	
			return false;
		})
		 
		//单击隐藏
		document.onmousedown=function(e){
			hotbox.active("idle");	
			objectId = null;
		} 
	 });
	
	var detail = new Vue({
	   el: '#detail',
	   data: {
		   detailData : {},
		   fileDatas : [],
		   completeStartDetail : {'active':true},
		   examine : {'active':false},
		   evaluating : {'active':false},
		   distribute : {'active':false},
		   repair : {'active':false},
		   loginId : $("#loginId").val(),
		   active : null,
		   completeForm : true,
	   },
	   methods: {
		   handle: function (key,taskName,taskId,processId) {
			   if(key=='completeStartDetail'){
				   window.location.href="/customerServiceProcess/startProcess/form/view?processId="+processId 
			   }else{
				   var func=function(){
					 if($("#customerServiceForm").valid()){
						 $("#customerServiceForm").submit();
						 return true;
					 }else{
						 return false;
					 }
				   }
				   createModalPage("办理"+taskName,"/customerServiceProcess/runtimeTask/form/view?taskId="+taskId+"&processId="+processId,func)
			   }
			 
		  }, 
	   },
	   filters: {
		  yearFormat: function (value) {
			  return value.split(" ")[0];
		  },
		  timeFormat: function (value) {
			  return value.split(" ")[1];
		  }
	   }
	})
	
	// 加载detail数据
	var loadContent = function(){
		$.ajax({
			url : "/customerServiceProcess/process/detail/"+$("#processId").val(),
			data : {'showAttributes':'id,name,processInstanceId,applicant.name,device.serialNo,project*,startDetail*,examineDetail*,evaluatingDetail*,distributeDetail*,repairDetail*,agreementStatus*,historicProcessInstance.startTime,historicProcessInstance.endTime,task.id,task.name,task.assignee,task.taskDefinitionKey'},
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				detail.detailData = data;
				detail.detailData.startDetail.belong = $.i18n.prop('customerService.belong.' + data.startDetail.belong)
				detail.detailData.startDetail.productType = $.i18n.prop('customerService.productType.' + data.startDetail.productType)
				$.ajax({
					url : '/fileDownload/list/group/customerService_'+data.name,
					type : 'get',
					dataType : 'json',
					success : function( fileData ) {
						detail.fileDatas = fileData.content
						$.each(fileData.content,function(){
							var stringObjectId = this.stringObjectId;
							//绑定右击事件
							$(document).on("contextmenu","#"+stringObjectId,function(){
								objectId = stringObjectId;
							})
						})
					}
				 })
			}
		})
	}
	
	// 加载
	loadContent();
	
	// 确定当前活跃任务
	var activeTasks = function(){
		$.ajax({
			url : "/customerServiceProcess/process/detail/taskDatas/"+$("#processId").val(),
			data : {'showAttributes':'taskDefinitionKey,createTime,endTime'},
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				$.each(data,function(){
					checkActive(this,detail[this.taskDefinitionKey])
				})
			}
		})
	}
	
	function checkActive(data,node){
		node.taskDefinitionKey = data.taskDefinitionKey
		node.createTime = data.createTime
		if('endTime' in data){
			node.endTime = data.endTime
			node.active = false
		} else {
			node.active = true
			detail.active = $.i18n.prop('customerService.task.' + data.taskDefinitionKey)
		}
	}
	
	// 激活图标
	activeTasks()
	
})