/**
 * 
 */
$(function(){
			$("#search").click(function(){
				$.ajax({
	              	url: "/technologyDocument/search/content",
	              	data:{"content":$("#content").val(),"rowId":"id","showAttributes":"highLight,fileId","type":"故障诊断文档"},
	              	type : "get",
	              	dataType : "json",
	              	success: function(data){
	              		var table = $("#result");
	              		table.empty()
	              		$.each(data.rows,function(i,value){
	              			var href="/fileDownload/"+this.cell[1]
	              			table.append("<tr>")
	              			table.append("<td>摘要:</td>")
	              			table.append("<td>")
	              			table.append(this.cell[0])
	              			table.append("</td>")
	              			table.append("<td>")
	              			table.append("<a href="+href+">download</a>")
	              			table.append("</td>")
	              			table.append("</tr>")
						})
	              	},
	    	        error:function(){alert('出错了')},
	           });
			})
			
			
			$("input:file").fileinput({
				allowedFileExtensions: ["xls","xlsx","pdf","doc","docx","csv","txt"]
			});
			
			$("#uploadForm").validate({
				submitHandler: function(form) {  
					var options = {
						type : "post",
						url : '/technologyDocument/upload',
						data : {'type':'故障文档'},
						success : function(data) {
							if(data.result=='upload success'){
								alert('操作成功');
							}else if(data.result=='error'){
								alert('操作失败:'+data.message);
							}
						}
					};
					
					$(form).ajaxSubmit(options);     
				}  
			})
			
			 //search autocomplete
			 $( "#content" )
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
						url : '/searchRecord/autocomplete',
						data : {
							'searchContent' :  request.term,
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
		          if ( term.length < 1 || term.length >10 ) {
		            return false;
		          }
		        },
		        focus: function() {
		          // 防止在获得焦点时插入值
		          return false;
		        },
		        select: function( event, ui ) {
		          console.info(ui.item);
		          $( "#content" ).val(ui.item.label);
		          return false;
		        }
		      }); 
		});