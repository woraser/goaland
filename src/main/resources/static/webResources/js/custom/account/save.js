/**
 * 
 */
$(document).ready(function() {
	$("#accountForm").validate({
		debug:true,
		rules : {
			loginId : {
				required : true,
				minlength: 2,
				checkUniqueLoginId : true,
			},
			name : {
				minlength: 2,
				required : true,
			},
			password : {
				minlength: 2,
				required : true,
			},
			passwordConfirm : {
				minlength: 2,
				required : true,
				equalTo: "#password",
			},
			role : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/account/save',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						var func = function(){
							window.location.href="/account/management/view"
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
	
	jQuery.validator.addMethod("checkUniqueLoginId", function(value, element) {
		var flag=true;
		$.ajax({
			url : '/account/checkExist',
			data : {
				'loginId' : $("#loginId").val(),
			},
			type : 'get',
			dataType : 'json',
			success : function(msg) {
				if (msg.result == true) {
					flag = false;
				}
			}
		});
		return flag;
	}, "您输入的账户已存在");
	
	if($("#accountId").val()!=null&&$("#accountId").val()!=""){
		$("#showPassword").hide();
		$("#showPasswordConfirm").hide();
	}

	$("#passwordEdit").click(function(){
		$("#showPassword").show();
		$("#showPasswordConfirm").show();
		$("#showPasswordEdit").hide();
	})
	
	 var setting = {
		check: {
			enable: true,
			chkboxType: { "Y": "ps", "N": "s", }
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
	
	$.ajax({
		url : '/account/checkExist',
		type : 'get',
		dataType : 'json',
		success : function(zNodes) {
			$.fn.zTree.init($("#accountRoleFunctionTree"), setting, zNodes);
		}
	});
	
})
