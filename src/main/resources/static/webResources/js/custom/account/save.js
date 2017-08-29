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
			newPassword : {
				minlength: 2,
				required : true,
			},
			passwordConfirm : {
				minlength: 2,
				required : true,
				equalTo: "#newPassword",
			},
			role : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var afaTree = $.fn.zTree.getZTreeObj("accountRoleFunctionTree");
     	    var afaTrueNodes = afaTree.getCheckedNodes(true);
     	    var selIdRoleFunction="";
	   	    for(var i=0;i<afaTrueNodes.length;i++){
	   	       var id = afaTrueNodes[i].id.toString();
	   	       if(id!="menu_0") {
	   	    	   if(selIdRoleFunction==""){
	   	    		   selIdRoleFunction+=afaTrueNodes[i].id;
	   	    	   }else{
	   	    		selIdRoleFunction+=","+afaTrueNodes[i].id;
	   	    	   }
	   	       }
	   	    } 
			var options = {
				type : "post",
				url : '/account/save',
				data : {'selRolesFunctionNode' : selIdRoleFunction},
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						var func = function(){
							window.location.href="/account/management/view"
						}
						infoAndFunc('操作成功',func);
					}else if(data.result=='error'){
						warning('操作失败:'+data.message);
					}else{
						warning('操作失败');
					}
				}
			};
			
			$.blockUI({message: '<img src="/webResources/img/loading/loading.gif" /> '});
			$(form).ajaxSubmit(options);     
		}  
	});
	
	jQuery.validator.addMethod("checkUniqueLoginId", function(value, element) {
		var flag;
		$.ajax({
			url : '/account/checkExist',
			data : {
				'loginId' : $("#loginId").val(),
			},
			type : 'get',
			dataType : 'json',
			async:false,
			success : function(msg) {
				// true表示已经存在
				if (msg.result == true) {
					flag = false;
				} else{
					flag = true;
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
		url : '/account/roleFunction/tree/data',
		data : {'accountId' : $('#accountId').val()},
		type : 'get',
		dataType : 'json',
		success : function(zNodes) {
			console.info(zNodes)
			$.fn.zTree.init($("#accountRoleFunctionTree"), setting, zNodes);
		}
	});
	
})
