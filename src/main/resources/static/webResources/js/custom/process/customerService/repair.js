/**
 * 
 */
$(document).ready(function() {
	var customerService = new Vue({
	  el: '#customerServiceForm',
	  data: {
	    isEntrust:false,
	  }
	})
	
	$("#customerServiceForm").validate({
		//debug:true,
		rules : {
			"device.serialNo" : {
				required : true,
				checkExist : true,
			},
			"repairDetail.problemDescription" : {
				required : true,
			},
			"repairDetail.failureCause" : {
				required : true,
			},
			"repairDetail.processMode" : {
				required : true,
			},
			mandatary : {
				required : true,
			},
			reason : {
				required : true,
			},
			fileUpLoad : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var url;
			if($('#isEntrust').is(':checked')) {
				url='/customerServiceProcess/entrust'
			}else{
				url='/customerServiceProcess/repair'
			}
			var options = {
				type : "post",
				url : url,
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						var func = function(){
							window.location.href="/customerServiceProcess/runtimeTask/view"
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
	
	jQuery.validator.addMethod("checkExist", function(value, element) {
		var flag;
		$.ajax({
			url : '/device/checkExist',
			data : {
				'serialNo' : $("#device\\.serialNo").val(),
			},
			type : 'get',
			dataType : 'json',
			async:false,
			success : function(msg) {
				// true表示已经存在
				flag = msg.result;
				// 如果不是通过autocomplete点击的,那么回后台获取id
				console.info($("#device.\\id").val())
				if($("#device.\\id").val()==""||$("#device.\\id").val()==null){
					$.ajax({
						url : '/device/management/data/one',
						data : {
							'serialNo' : $("#device\\.serialNo").val(),
							'showAttributes' : 'id'
						},
						type : 'get',
						dataType : 'json',
						async:false,
						success : function(data) {
							$("#device\\.id").val(data.id)
						}
					});
				}
			}
		});
		return flag;
	}, "您输入的设备不存在");
	
	//search autocomplete
	 $(document)
    // 当选择一个条目时不离开文本域
    .on( "keydown","#device\\.serialNo", function( event ) {
      if ( event.keyCode === $.ui.keyCode.TAB &&
          $( this ).data( "ui-autocomplete" ).menu.active ) {
        event.preventDefault();
      };
      $(this).autocomplete({
          source: function( request, response ) {
            	
              $.ajax({
      				url : '/device/autocomplete',
      				data : {
      					'serialNo' :  "start$"+request.term,
      					'label' : 'serialNo',
      					'value' : 'id',
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
              $( "#device\\.serialNo" ).val(ui.item.label);
              $( "#device\\.id" ).val(ui.item.value);
              return false;
            }
          })
    }); 
	 
	 $("#fileUpLoad").fileinput({
		language: 'zh', //设置语言
		maxFileCount: 10,
		showUpload: false, //是否显示上传按钮
		maxFileSize : 50000,
		allowedFileExtensions: ["jpg","jpeg","png"]
	});
	
})
