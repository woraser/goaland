/**
 * 
 */
$(document).ready(function() {
	 
	 //每页显示多少行
	 var rowNum=3;
	 var initPage=0;//初始页
	 var pageNum=0;//总页数
	 var url='/customerServiceProcess/runtimeTask/data/REMOTE';
	 var sort;
		 
	 //请求参数
	 var params={}
	 params['showAttributes']='name,task.id,task.name,historicProcessInstance.startTime,historicProcessInstance.endTime,applicant.name,startDetail.projectName,startDetail.projectLocation,startDetail.sceneDescription,finishType.name';//要获取的属性名
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
				}
		 });
	 }
	 
	 function convertContent(data){
		 $("#tasks").append('<div class="taskModel" >'+
			        '<div class="taskAllSimple">'+
		            '<div class="taskSimple" style="color:#ffff;text-align: center;width:18%;height: 100%;">'+
		               ' 任务编号:'+data.name+
		            '</div>'+
		            '<div class="taskSimple" style="color:#ABADB3;width:25%;height: 100%;">'+
		                '申请时间:'+data['historicProcessInstance.startTime']+
		            '</div>'+
		            '<div class="taskSimple" style="color:#ABADB3;width:25%;height: 100%;">'+
	                	'申请人:'+data['applicant.name']+
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
		                        '<td class="deviceInfo" >设备名称</td>'+
		                        '<td class="deviceNewInfo">'+data['startDetail.projectName']+'</td>'+
		                    '</tr>'+
		                    '<tr>'+
		                        '<td class="deviceInfo">安装位置</td>'+
		                        '<td class="deviceNewInfo">'+data['startDetail.projectLocation']+'</td>'+
		                    '</tr>'+
		                    '<tr>'+
		                        '<td class="deviceInfo">使用客户</td>'+
		                        '<td class="deviceNewInfo">国家电网公司哈密南800AV换流站</td>'+
		                    '</tr>'+
		                    '</tbody>'+
		                '</table>'+
		            '</div> <!--设备信息 -->'+
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
		                '<button class="doButton">详情</button>'+
		                '<button class="newTaskButton" id="handle" taskName='+data['task.name']+' taskId='+data['task.id']+'>办理</button>'+
		            '</div>'+
		        '</div>'+
		    '</div>');
		 	if('historicProcessInstance.endTime' in data){
		 		$('#'+data.name+'_endTime').html(data['historicProcessInstance.endTime'])
		 	}
	 }
	 
	 /***
	  * 点击办理,由于按钮是后来生成的,所以使用事件委托
	  */
	 $(document).on("click", "#handle", function () {        
		 var func=function(){
			 if($("#customerServiceForm").valid()){
				 $("#customerServiceForm").submit();
				 return true;
			 }else{
				 return false;
			 }
		 };
		 createModalPage("办理"+$(this).attr('taskName'),"/customerServiceProcess/runtimeTask/form?taskId="+$(this).attr('taskId'),func);   
	 })
	 
	 //加载数据
	 reloadContent(initPage)
	 
	 //刷新分页插件
	 createPage($("#dataPager"),pageNum,initPage,11,reloadContent)
	 
})