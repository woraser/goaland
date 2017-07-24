/**
 * 
 */
$(document).ready(function() {
	$("#sensorForm").validate({
		//debug:true,
		rules : {
			serialNo : {
				required : true,
				checkSerialNo : true,
			},
			iotx : {
				required : true,
			},
			sensorInterface : {
				required : true,
			},
			sensorPort : {
				required : true,
			},
			sensorCategory : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/sensor/saveSensor',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						info('操作成功');
						$("#sensorTable").trigger("reloadGrid");
					}else{
						warning('操作失败:'+data.result);
					}
				}
			};
			
			$.blockUI({message: '<img src="/webResources/img/loading/loading.gif" /> '});
			$(form).ajaxSubmit(options);     
		}  
	});

	jQuery.validator.addMethod("checkSerialNo", function(value, element) {
		var flag=true;
		$.ajax({
			url : '/sensor/checkExist',
			data : {
				'serialNo' : $("#serialNo").val(),
			},
			type : 'get',
			dataType : 'json',
			success : function(msg) {
				//如果存在相同序列号，判断id是否相等
				if (msg.result == true) {
					if($("#sensorId").val()!=null&&$("#sensorId").val()!=''){
						
						/*-----检查序列号和id----*/
						$.ajax({
							url : '/sensor/checkExist',
							data : {
								'serialNo' : $("#serialNo").val(),
								'id' : $("#sensorId").val(),
							},
							type : 'get',
							dataType : 'json',
							async : false,
							success : function(msg) {
								//如果序列号和id都相等，那么表明是同一个iotx
								if (msg.result != true) {
									flag=false
								} 
							}
						});
						/*-----检查序列号和id----*/
						
					}else{
						//为null肯定为重复序列号
						flag = false;
					}
				}
			}
		});
		return flag;
	}, "您输入的序列号已存在");
	
	//iotxSN autocomplete
	$( "#iotxSN" ).bind( "keydown", function( event ) {
	      if ( event.keyCode === $.ui.keyCode.TAB &&
	            $( this ).data( "ui-autocomplete" ).menu.active ) {
	          event.preventDefault();
	        }
	      }).autocomplete({
	        source: function( request, response ) {
	        	
	          $.ajax({
					url : '/iotx/autocomplete',
					data : {
						'label' : 'serialNo',
						'value' : 'id',
						'serialNo' : request.term,
						'company.code' : ($('#isAdmin').length > 0?null:$("#companyCode").val()),
					},
					type : 'get',
					dataType : 'json',
					async : false,
					success : function( datas ) {
						response($.each(datas,function(i,value) {
							return {
								label : this.label,
								value : this.value,
							}
						}));
					}
				
	          });
	          
	        },
	        search: function() {
	          // 自定义最小长度
	          var term = this.value;
	          if ( term.length < 3 ) {
	            return false;
	          }
	        },
	        focus: function() {
	          // 防止在获得焦点时插入值
	          return false;
	        },
	        select: function( event, ui ) {
	          console.info(ui.item);
	          $("#iotxSN").val(ui.item.label);
	          $( "#iotx" ).val(ui.item.value);
	          return false;
	        }
	      }); 
})
