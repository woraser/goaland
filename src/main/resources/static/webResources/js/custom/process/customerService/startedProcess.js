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
		   detail: function(id){
			   window.location.href="/customerServiceProcess/process/detail/view?id="+id 
		   },
		   rejectRewrit: function(id){
			   window.location.href="/customerServiceProcess/startProcess/form/view?processId="+id
		   }
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
	 var url='/customerServiceProcess/startedProcess/data/REMOTE';
	 var sort;
		 
	 //请求参数
	 var params={}
	 params['showAttributes']='id,name,historicProcessInstance.startTime,historicProcessInstance.endTime,applicant.name,startDetail.project.number,startDetail.project.name,startDetail.project.location,finishType.name,agreementStatus.agreement,agreementStatus.beginTime,agreementStatus.endTime,examineDetail.reject';//要获取的属性名
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
		 params['beginTime']=$("#beginTime").val();
		 params['endTime']=$("#endTime").val();
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