/**
 * 
 */
$(document).ready(function() {
	$("#accountForm").validate({
		//debug:true,
		ignore: ":hidden:not(select)",
		rules : {
			loginId : {
				required : true,
				minlength: 2,
				checkUniqueLoginId : true,
			},
			name : {
				minlength: 2,
				required : true,
				checkUniqueName : true,
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
			emailAddress : {
				required : true,
				checkEmail : true,
			}
		},
		errorPlacement: function(error, element) {
            if(element.parent('#roleSelect').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
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
						info('操作成功');
						$("#accountTable").trigger("reloadGrid");
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
	
	//邮箱验证
	jQuery.validator.addMethod("checkEmail", function(value, element) {
		var length = value.length;
		var mobile = /\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,6}\b/i;
		return this.optional(element) || mobile.test(value);
	}, "请正确填写您的邮箱地址");
	
	jQuery.validator.addMethod("checkUniqueLoginId", function(value, element) {
		var flag;
		$.ajax({
			url : '/account/checkExist',
			data : {
				'loginId' : value,
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
	
	jQuery.validator.addMethod("checkUniqueName", function(value, element) {
		var flag;
		$.ajax({
			url : '/account/checkExist',
			data : {
				'name' : value,
			},
			type : 'get',
			dataType : 'json',
			async:false,
			success : function(msg) {
				// true表示已经存在
				if (msg.result == true) {
					if($("#accountId").val()!=null&&$("#accountId").val()!=""){
						// 判断是否存在当前用户
						$.ajax({
							url : '/account/checkExist',
							data : {
								'id' : $("#accountId").val(),
								'name' : $("#name").val(),
							},
							type : 'get',
							dataType : 'json',
							async:false,
							success : function(msg) {
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
				} else{
					flag = true;
				}
			}
		});
		return flag;
	}, "您输入的用户名已存在");
	
	if($("#accountId").val()!=null&&$("#accountId").val()!=""){
		$("#showPassword").hide();
		$("#showPasswordConfirm").hide();
		$.each(eval($("#roleIds").val()),function(index,item){
			$("#role option[value='"+ item.id +"']").attr("selected","selected");
		}); 
		$.each(eval($("#groupIds").val()),function(index,item){
			$("#roleFunctionGroup option[value='"+ item.id +"']").attr("selected","selected");
		}); 
	}
	
	$("#role").chosen();
	$("#roleFunctionGroup").chosen();

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
	
	/*//role选中后，其他选项必须属于同部门
	$('#role').change(function(){
		var role = $('#role').val();
		console.info("role:"+role)
		if(role instanceof Array && role.length>1){
			var depId=$('#role').attr("depId");
			console.info("depId:"+depId)
			if(depId instanceof Array  && depId.length>1){
				var first=0;
				$.each(depId,function(){
					if(first==0){
						first=this
					}else{
						if(first!=this){
							warning("选择不同角色但部门必须相同");
						}
					}
				})
			}
		}
	})*/
	
	/*function getRole(role,depId){
		var roleUrl;
		if(role==null){
			roleUrl='/role/management/data/REMOTE'
		}else{
			roleUrl='/role/management/data/REMOTE?depGroup.department.id='+depId
		}
		$.ajax({
			url : roleUrl,
			data : {
				'showAttributes':'id,name,depGroup.name,depGroup.department.name,depGroup.department.id',
			},
			type : 'get',
			dataType : 'json',
			success : function(data) {
				var selectRole = $("#role");
				selectRole.empty();
				$.each(data.content,function(){
					selectRole.append("<option value=" + this['id'] + " depId="+this['depGroup.department.id']+">"+ this['name']+'-'+ this['depGroup.name']+'-'+ this['depGroup.department.name'] + "</option>");
				})
				if(role!=null){
					$("#role option[value='"+ role +"']").attr("selected","selected");
				}
			}
		})
	}*/
	
	 var k = document.getElementById("roleFunctionGroup");  
	 var stri = "";  
	 for(i=0;i<k.length;i++){     
        if(k.options[i].selected){ 
            stri+=k.options[i].value+" "; 
        }  
     }  
     var nowOp= stri.substr(0,stri.length-1);
	
	//权限组
	$('#roleFunctionGroup').change(function(){
		//1.获取修改的一项
		//2.找出这项所有相关id
		//3.增加时，for循环匹配id，匹配项增加checked:true
		//4.减少时，统计各选中项中id出现次数，出现次数为1的项修改checked:false
		
		 var o = document.getElementById("roleFunctionGroup");
		 
		 var str = "";  
		 var n=0;
		 for(i=0;i<o.length;i++){     
            if(o.options[i].selected){ 
                str+=o.options[i].value+" "; 
                n+=1;
            }  
         }  
		 var beforeOp=nowOp;
		 //alert( str.substr(0,str.length-1) );  
		 nowOp= str.substr(0,str.length-1);
		
		 if(n>1){
			var a=beforeOp.split(" ");
			var b=nowOp.split(" ");
			var c=parseInt(getUniqueSet( a, b ));
		 }else if(n==1 && (beforeOp==null || beforeOp=="")){
			var c=nowOp;
		 }else if(n==1 && beforeOp.length>0){
			var a=beforeOp.split(" ");
			var b=nowOp.split(" ");
			var c=parseInt(getUniqueSet( a, b ));
		 }else if(n==0){
			var c=beforeOp;
		 }
		 
		 if(beforeOp==null || beforeOp=="" || beforeOp.length<nowOp.length){
			 $.ajax({
	    		url:'/roleFunctionGroup/management/data/REMOTE',
	    		data:{
	    			"id":c,
	    			"showAttributes":'roleFunctionList*.id,roleFunctionBtnList*.id',
	    			},
	    		type:'GET',
	    		dataType:'json',
	    		success:function(data){
	    			 console.info(data)
	    			 var afaTree = $.fn.zTree.getZTreeObj("accountRoleFunctionTree");
	    			 var rootnode = afaTree.getNodeByParam("id",'menu_'+0);
    				 afaTree.checkNode(rootnode, true, false);
	    			 $.each(data.content,function(i,item){
	    				  $.each(item.roleFunctionList,function(i,subItem){
	    					  var role = afaTree.getNodeByParam("id",'menu_'+subItem.id);
	    					  afaTree.checkNode(role, true, false);
	    				  })
	    				  $.each(item.roleFunctionBtnList,function(i,subItem){
	    					  var btn = afaTree.getNodeByParam("id",subItem.id);
	    					  afaTree.checkNode(btn, true, false);
	    				  })
	    			 });
	    			 if(nowOp==""){
	    				before=""; 
	    			 }
	    		}
	    	}); 
		 }else if(nowOp=="" || nowOp==null || beforeOp.length>nowOp.length){
			    var afaTree = $.fn.zTree.getZTreeObj("accountRoleFunctionTree");
	     	    var afaTrueNodes = afaTree.getCheckedNodes(true);
	     	    var str = "";  
	     	    for(var i=0;i<afaTrueNodes.length;i++){
	     	       var id = afaTrueNodes[i].id.toString();
	     	       if(id!=0) {
	     	    	  str+=id+",";
	     	       }
	     	    } 
			 $.ajax({
	    		url:'/roleFunctionGroup/zTree/decrease',
	    		data:{"groupId":c,"allSelectedGroup":nowOp,"allSelectedId":str},
	    		type:'GET',
	    		dataType:'json',
	    		success:function(data){
	    			 var afaTree = $.fn.zTree.getZTreeObj("accountRoleFunctionTree");
	    			 $.each(data.result,function(i,item){
	    				  var node = afaTree.getNodeByParam("id",item);
	    				  afaTree.checkNode(node, false, false);
	    			});
	    		}
	    	});
		 }
	})
	
	function getUniqueSet( setA, setB ){
	    var temp = {};
	    for( var i = 0, len = setA.length; i < len ; i++ ){
	        temp[ setA[i] ] = 0;
	    }
	    for( var j = 0, len = setB.length; j < len ; j++ ){
	        if( typeof temp[ setB[j] ] === 'undefined' ){
	            temp[ setB[j] ] = 0;
	        }else{
	            temp[ setB[j] ]++;
	        }
	    }
	    //output
	    var ret = [];
	    for( var item in temp ){
	        !temp[item] && ret.push( item );
	    }
	    return ret;
	}
 
	
})
