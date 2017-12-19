/**
 * 
 */
$(document).ready(function() {
	 var processes = new Vue({
	   el: '#processes',
	   data: {
		   processDatas:[],
	   },
	   methods: {
		  handle: function (taskName,taskId,processId) {
			 if(taskName=="销售部/工程部/质检部发起") {
				 window.location.href="/customerServiceProcess/startProcess/form/view?processId="+processId 
			 } else {
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
	   	  detail: function(id){
	   		window.location.href="/customerServiceProcess/process/detail/view?id="+id 
	   	  },
	   },
	   filters: {
		  dateFormat: function (value) {
			  return value.split(" ")[0];
		  }
	   },
	 })
	 
	 //每页显示多少行
	 var rowNum=4;
	 var page=0;//初始页
	 var pageNum=0;//总页数
	 var url='/customerServiceProcess/runtimeTask/data/REMOTE';
	 var sort;
		 
	 //请求参数
	 var params={}
	 params['showAttributes']='id,name,historicProcessInstance.startTime,historicProcessInstance.endTime,applicant.name,startDetail.project.number,startDetail.project.name,startDetail.project.location,finishType.name,agreementStatus.agreement,agreementStatus.beginTime,agreementStatus.endTime,task.id,task.name';//要获取的属性名
	 params['size']=rowNum;
	 params['sort']=sort;
	 params['searchContent']=null;
	 
	 var selects = new Vue({
	   el: '#selects',
	   data: params,
	 })
	 
	 /***
	  * 获取流程数据
	  */
	 var reloadContent = function(page){
		 params['page']=page;
		 if($("#beginTime").val()!=null&&$("#beginTime").val()!=""){
			 params['beginTime']=$("#beginTime").val();
		 }else{
			 delete params['beginTime']
		 }
		 if($("#endTime").val()!=null&&$("#endTime").val()!=""){
			 params['endTime']=$("#endTime").val();
		 }else{
			 delete params['endTime']
		 }
		 $.ajax({
				url : url,
				data : params,
				type : 'get',
				dataType : 'json',
				success : function( data ) {
					pageNum=data.total;
					page=data.page;
					var content = data.content;
					$.each(content,function(){
						if('agreementStatus.agreement' in this){
							this.agreementPic = this['agreementStatus.agreement']
							this['agreementStatus.agreement'] = $.i18n.prop('customerService.agreement.status.'+this['agreementStatus.agreement'])
						}
					})
					processes.processDatas = content;
					//刷新分页插件
					createPage($("#dataPager"),pageNum,page,11,reloadContent)
				}
		 });
	 }
	 
	 $("#beginTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	 $("#endTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	 
	 $("#search").click(function(){
		 reloadContent(0)
	 })
	 
	 //加载数据
	 reloadContent(page)
	 
})