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
			
			$.blockUI({message: '<img src="/webResources/img/loading/loading.gif" /> '});
			$(form).ajaxSubmit(options);     
		}  
	});
	
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
      					'number' :  request.term,
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
	
})
