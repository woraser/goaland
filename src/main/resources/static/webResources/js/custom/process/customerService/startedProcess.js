/**
 * 
 */
$(document).ready(function() {
	 
	 //每页显示多少行
	 var rowNum=3;
	 var page=0;//初始页
	 var pageNum=0;//总页数
	 var url='/customerServiceProcess/startedProcess/data/REMOTE';
	 var sort;
		 
	 //请求参数
	 var params={}
	 params['showAttributes']='name,historicProcessInstance.startTime,historicProcessInstance.endTime,applicant.name,startDetail.projectName,startDetail.projectLocation,startDetail.sceneDescription,finishType.name';//要获取的属性名
	 params['size']=rowNum;
	 params['sort']=sort;
	 
	 /***
	  * 获取流程数据
	  */
	 var reloadContent = function(page){
		 params['page']=page;
		 $.ajax({
				url : url,
				data : params,
				type : 'get',
				dataType : 'json',
				async : false,
				success : function( data ) {
					pageNum=data.total;
					page=data.page;
					//加载数据
					$("#tasks").html('')
					$.each(data.content,function(i,value){
						convertContent(this)
					})
					$("#tasks").append(' <div class="zxf_pagediv" id="dataPager"></div>');
					//刷新分页插件
					createPage($("#dataPager"),pageNum,page,11,reloadContent)
				}
		 });
	 }
	 
	 function convertContent(data){
		 $("#tasks").append('<div class="taskModel" >'+
			        '<div class="taskAllSimple">'+
		            '<div class="taskSimple" style="color:#ffff;text-align: center;width:18%;height: 100%;">'+
		            	$.i18n.prop('customerService.projectNo')+':'+data.name+
		            '</div>'+
		            '<div class="taskSimple" style="color:#ABADB3;width:25%;height: 100%;">'+
		            	$.i18n.prop('customerService.startTime')+':'+data['historicProcessInstance.startTime']+
		            '</div>'+
		            '<div class="taskSimple" style="color:#ABADB3;width:25%;height: 100%;">'+
		            	$.i18n.prop('customerService.applicant')+':'+data['applicant.name']+
	                '</div>'+
		        '</div>'+
		        '<div class="taskAll">'+
		            '<div class="taskPicture">'+
		                '<img src="/webResources/img/process/22.png" style="width: 75%;height: 60%;margin-top: 22%;margin-left: 30%;"/>'+
		            '</div>'+
		            '<div class="deviceTable" >'+
		                '<table class="table" >'+
		                    '<tbody >'+
		                    '<tr>'+
		                        '<td class="deviceInfo" >'+$.i18n.prop('customerService.projectName')+'</td>'+
		                        '<td class="deviceNewInfo">'+data['startDetail.projectName']+'</td>'+
		                    '</tr>'+
		                    '<tr>'+
		                        '<td class="deviceInfo">'+$.i18n.prop('customerService.projectLocation')+'</td>'+
		                        '<td class="deviceNewInfo">'+data['startDetail.projectLocation']+'</td>'+
		                    '</tr>'+
		                    '</tbody>'+
		                '</table>'+
		            '</div>' +
		            '<div class="describeDevice" >'+
		               	data['startDetail.sceneDescription']+
		            '</div>'+
		            '<div class="taskState">'+
		                '<div class="state">'+
		                    data['finishType.name']+
		                '</div>'+
		                '<div class="taskData" id='+data.name+'_endTime>'+
		                	
		                '</div>'+
		            '</div>'+
		            '<div class="taskButton">'+
		                '<button class="doButton">'+$.i18n.prop('customerService.detail')+'</button>'+
		            '</div>'+
		        '</div>'+
		    '</div>');
		 	if('historicProcessInstance.endTime' in data){
		 		$('#'+data.name+'_endTime').html(data['historicProcessInstance.endTime'])
		 	}
	 }
	 
	 /***
	  * 点击新增
	  */
	 $("#createProcess").click(function(){
		 var func=function(){
			 if($("#customerServiceForm").valid()){
				 $("#customerServiceForm").submit();
				 return true;
			 }else{
				 return false;
			 }
		 };
		 createModalPage("新建售后流程","/customerServiceProcess/startProcess/form",func); 
	 })
	 
	 //加载数据
	 reloadContent(page)
	 
})