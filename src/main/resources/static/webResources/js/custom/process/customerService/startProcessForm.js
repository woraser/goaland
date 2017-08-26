/**
 * 
 */
$(document).ready(function() {
	$("#customerServiceForm").validate({
		//debug:true,
		rules : {
			"startDetail.projectNo" : {
				required : true,
			},
			"startDetail.projectName" : {
				required : true,
			},
			"startDetail.projectLocation" : {
				required : true,
			},
			"startDetail.contactMan" : {
				required : true,
			},
			"startDetail.contactNumber" : {
				required : true,
				digits: true,
				minlength : 11,
				isMobile : true,
			},
			"startDetail.proposeTime" : {
				required : true,
			},
			"startDetail.sceneDescription" : {
				required : true,
			},
			"startDetail.estimateDevices" : {
				required : true,
			},
			engineeDep : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/customerServiceProcess/startProcess',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						var func = function(){
							window.location.href="/customerServiceProcess/startedProcess/view"
						}
						infoAndFunc('操作成功',func);
					}else if(data.result=='error'){
						warning('操作失败:'+data.message);
					}
				}
			};
			
			$.blockUI({message: '<img src="/webResources/img/loading/loading.gif" /> '});
			$(form).ajaxSubmit(options);     
		}  
	});

	// 手机号码验证
	jQuery.validator.addMethod("isMobile", function(value, element) {
		var length = value.length;
		var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
		return this.optional(element) || (length == 11 && mobile.test(value));
	}, "请正确填写您的手机号码");

	$("#proposeTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true'});
	
	$("#fileUpLoad").fileinput({
		language: 'zh', //设置语言
		maxFileCount: 10,
		showUpload: false, //是否显示上传按钮
		allowedFileExtensions: ["xlsx","zip","xls","rar","jpg","jpeg","png","pdf","7z","doc","docx","csv","txt"]
	});
	 
})
