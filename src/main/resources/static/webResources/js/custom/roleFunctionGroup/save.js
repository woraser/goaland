/**
 * 
 */
$(document).ready(function() {
	$("#roleFunctionGroupForm").validate({
		debug:true,
		rules : {
			name : {
				minlength: 2,
				required : true,
				checkUniqueName : true,
			},
		},
		submitHandler: function(form) {  
			var afaTree = $.fn.zTree.getZTreeObj("roleFunctionGroupRoleFunctionTree");
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
				url : '/roleFunctionGroup/save',
				data : {'selRolesFunctionNode' : selIdRoleFunction},
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						var func = function(){
							window.location.href="/roleFunctionGroup/management/view"
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
	
	jQuery.validator.addMethod("checkUniqueName", function(value, element) {
		var flag;
		$.ajax({
			url : '/roleFunctionGroup/checkExist',
			data : {
				'name' : $("#name").val(),
			},
			type : 'get',
			dataType : 'json',
			async:false,
			success : function(msg) {
                if (msg.result == true) {
                    if($("#roleFunctionGroupId").val()!=null&&$("#roleFunctionGroupId").val()!=""){
                        $.ajax({
                            url : '/roleFunctionGroup/checkExist',
                            data : {
                                'name' : $("#name").val(),
                                'id' : $("#roleFunctionGroupId").val(),
                            },
                            type : 'get',
                            dataType : 'json',
                            async:false,
                            success : function(msg) {
                                // true表示已经存在
                                if (msg.result == true) {
                                    flag = true;
                                } else{
                                    flag = false;
                                }
                            }
                        });
                    }else{
                        flag = false;
                    }
				}else{
                    flag = true;
                }
			}
		});
		return flag;
	}, "您输入的权限组已存在");
	
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
		url : '/roleFunctionGroup/roleFunction/tree/data',
		data : {'roleFunctionGroupId' : $('#roleFunctionGroupId').val()},
		type : 'get',
		dataType : 'json',
		success : function(zNodes) {
			console.info(zNodes)
			$.fn.zTree.init($("#roleFunctionGroupRoleFunctionTree"), setting, zNodes);
		}
	});
	
})
