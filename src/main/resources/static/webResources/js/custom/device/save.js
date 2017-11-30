/**
 * 
 */
$(document).ready(function() {
	$("#deviceForm").validate({
		//debug:true,
		ignore: ":hidden:not(select)",
		rules : {
			"project.number" : {
				required : true,
			},
			productNo : {
				required : true,
			},
			productName : {
				required : true,
			},
			productSpecifications : {
				required : true,
			},
			devCategory : {
				required : true,
			},
			commissioningTime : {
				required : true,
			},
			serialNo : {
				required : true,
				checkUniqueSerialNo : true,
			},
			rfid : {
				checkUniqueRfid : true,
			},
			remindReceivers : {
				required : true,
			},
		},
		errorPlacement: function(error, element) {
            if(element.parent('#roleSelect').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        },
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/device/save',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						info('操作成功');
						$("#deviceTable").trigger("reloadGrid");
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
	
	 $("#remindReceivers").chosen();
	 
	 if($("#deviceId").val()!=null&&$("#deviceId").val()!=""){
		$.each(eval($("#receiverIds").val()),function(index,item){
			$("#remindReceivers option[value='"+ item.id +"']").attr("selected","selected");
		}); 
	 }
	
	jQuery.validator.addMethod("checkUniqueSerialNo", function(value, element) {
		var flag;
		$.ajax({
			url : '/device/checkExist',
			data : {
				'serialNo' : $("#serialNo").val(),
			},
			type : 'get',
			dataType : 'json',
			async:false,
			success : function(msg) {
				// true表示已经存在
				if (msg.result == true) {
					if($("#deviceId").val()!=null&&$("#deviceId").val()!=""){
						// 判断存在的序列号是否是当前设备
						$.ajax({
							url : '/device/checkExist',
							data : {
								'id' : $("#deviceId").val(),
								'serialNo' : $("#serialNo").val(),
							},
							type : 'get',
							dataType : 'json',
							async:false,
							success : function(msg) {
								// true当前序列号就是属于当前设备
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
	}, "您输入的序列号已存在");
	
	$("#commissioningTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	
	jQuery.validator.addMethod("checkUniqueRfid", function(value, element) {
		if($("#rfid").val() == null || $("#rfid").val() == ""){
			return true
		}
		var flag;
		$.ajax({
			url : '/device/checkExist',
			data : {
				'rfid' : $("#rfid").val(),
			},
			type : 'get',
			dataType : 'json',
			async:false,
			success : function(msg) {
				// true表示已经存在
				if (msg.result == true) {
					// 判断存在的rfid是否是当前设备
					if($("#deviceId").val()!=null&&$("#deviceId").val()!=""){
						$.ajax({
							url : '/device/checkExist',
							data : {
								'id' : $("#deviceId").val(),
								'rfid' : $("#rfid").val(),
							},
							type : 'get',
							dataType : 'json',
							async:false,
							success : function(msg) {
								// true当前rfid就是属于当前设备
								if (msg.result == true) {
									flag = true;
								} else{
									flag = false;
								}
							}
						});
					} else {
						flag = false;
					}
					
				} else{
					flag = true;
				}
			}
		});
		return flag;
	}, "您输入的rfid已存在");
	
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