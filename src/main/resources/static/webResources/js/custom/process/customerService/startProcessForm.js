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
	    var ringButtons = '下载|预览|删除附件|附件信息'.split('|');
	    ringButtons.forEach(function(button) {
	        main.button({
	            position: 'ring',
	            label: button,
				action:function(){
					if(button=="下载"){
						window.location.href="/fileDownload/"+objectId
					}else if(button=="预览"){
						$("#"+objectId).click()
					}else if(button=="删除附件"){
						$.blockUI({message: '<img src="/webResources/img/loading/loading.gif" /> '});
						$.ajax({
							url : '/deleteUploadFile/'+objectId,
							type : 'post',
							dataType : 'json',
							success : function( data ) {
								$.unblockUI();
								if(data.result=='success'){
									loadFile();//重新加载文件列表
									info('操作成功');
								}else if(data.result=='error'){
									warning('操作失败:'+data.message);
								}else{
									warning('操作失败');
								}
							}
						});
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
	
	 var files = new Vue({
		 el: '#customerServiceForm',
		 data: {
			 fileDatas : [],// 用来渲染已上传附件
		 }
	 })
	 
	 var loadFile = function(){
		 if($("#reject").val()=="true"){
			 $.ajax({
				url : '/fileDownload/list/group/customerService_'+$("#processName").val(),
				type : 'get',
				dataType : 'json',
				success : function( data ) {
					files.fileDatas = data.content
					$.each(data.content,function(){
						var stringObjectId = this.stringObjectId;
						//绑定右击事件
						$(document).on("contextmenu","#"+stringObjectId,function(){
							objectId = stringObjectId;
						})
					})
				}
			 });
		 }
	 }
	 //加载文件列表
	 loadFile();
	 
	$("#customerServiceForm").validate({
		//debug:true,
		rules : {
			"startDetail.belong" : {
				required : true,
			},
			"project.number" : {
				required : true,
			},
			"project.name" : {
				required : true,
			},
			"startDetail.productName" : {
				required : true,
			},
			"startDetail.productNo" : {
				required : true,
			},
			"startDetail.productSpecifications" : {
				required : true,
			},
			"startDetail.productType" : {
				required : true,
			},
			"startDetail.customerMan" : {
				required : true,
			},
			"startDetail.customerNumber" : {
				required : true,
				digits: true,
				minlength : 11,
				isMobile : true,
			},
			"startDetail.projectMan" : {
				required : true,
			},
			"startDetail.projectNumber" : {
				required : true,
				digits: true,
				minlength : 11,
				isMobile : true,
			},
			"startDetail.nextAssignee" : {
				required : true,
			},
			"startDetail.estimatedTime" : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/customerServiceProcess/startProcess',
				data : {"examineDetail.reject" : false},
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						var func = function(){
							window.location.href="/customerServiceProcess/startedProcess/view"
						}
						infoAndFunc('操作成功',func);
					}else if(data.result=='error'){
						warning('操作失败:'+data.message);
					}else{
						warning('操作失败');
					}
				}
			};
			
			$.blockUI({
				message: '<div class="lds-css ng-scope"><div class="lds-spinner" style="100%;height:100%"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div></div>',
				// 指的是提示框的css
				css: {
                    width: "0px",
                    top: "40%",
                    left: "50%"
                },
			});
			$(form).ajaxSubmit(options);     
		}  
	});

	// 手机号码验证
	jQuery.validator.addMethod("isMobile", function(value, element) {
		var length = value.length;
		var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
		return this.optional(element) || (length == 11 && mobile.test(value));
	}, "请正确填写您的手机号码");

	$("#startDetail\\.estimatedTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	
	$("#fileUpLoad").fileinput({
		language: 'zh', //设置语言
		maxFileCount: 10,
		showUpload: false, //是否显示上传按钮
		allowedFileExtensions: ["jpg","jpeg","png"]
	});
	
	//search autocomplete
	 $( "#project\\.number" )
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
				url : '/project/autocomplete',
				data : {
					'number' :  request.term,
				},
				type : 'get',
				dataType : 'json',
				success : function( datas ) {
					response($.each(datas,function(i,value) {
						return {
							label : this.label,
							value : this.value
						}
					}));
				}
			
         });
         
       },
       search: function() {
         // 自定义最小长度
         var term = this.value;
         if ( term.length < 1 ) {
           return false;
         }
       },
       focus: function() {
         // 防止在获得焦点时插入值
         return false;
       },
       select: function( event, ui ) {
         var words=ui.item.label.split("-")
         $( "#project\\.number" ).val(words[0]);
         $( "#project\\.name" ).val(words[1]);
         $( "#project\\.location" ).val(words[2]);
         $( "#project\\.id" ).val(ui.item.value);
         return false;
       }
     }); 
	 
})
