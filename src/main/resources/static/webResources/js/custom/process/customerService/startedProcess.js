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
		   detail: function(taskId){
		   		  
		   },
	   },
	 })
	 
	 //每页显示多少行
	 var rowNum=4;
	 var page=0;//初始页
	 var pageNum=0;//总页数
	 var url='/customerServiceProcess/startedProcess/data/REMOTE';
	 var sort;
		 
	 //请求参数
	 var params={}
	 params['showAttributes']='name,historicProcessInstance.startTime,historicProcessInstance.endTime,applicant.name,project.number,project.name,project.location,finishType.name,agreementStatus.agreement.name,agreementStatus.beginTime,agreementStatus.endTime';//要获取的属性名
	 params['size']=rowNum;
	 params['sort']=sort;
	 params['timeType']="start";
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
			async : false,
			success : function( data ) {
				pageNum=data.total;
				page=data.page;
				processes.processDatas = data.content;
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