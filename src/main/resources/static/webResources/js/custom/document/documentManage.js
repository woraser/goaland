/**
 * 
 */
$(document).ready(function() {
	var files = new Vue({
	  el: '#files',
	  data: {
	    fileDatas:[],
	    seenDetail:false,
	  }
	})
	 //每页显示多少行
	 var rowNum=6;
	 var page=0;//初始页
	 var pageNum=0;//总页数
	 var url='/technologyDocument/search/REMOTE';
	 var sort;
		 
	 //请求参数
	 var params={}
	 params['showAttributes']='suffix,highLightFileName,highLightContent,uploader,uploadTime,type,fileId';//要获取的属性名
	 params['size']=rowNum;
	 params['sort']=sort;
	 
	 /***
	  * 获取文档数据
	  */
	 var reloadContent = function(page){
		 params['page']=page;
		 $.ajax({
				url : url,
				data : params,
				type : 'get',
				dataType : 'json',
				success : function( data ) {
					pageNum=data.total;
					page=data.page;
					files.fileDatas = data.content;
					//刷新分页插件
					createPage($("#dataPager"),pageNum,page,11,reloadContent);
				}
		 });
	 }
	 
	 //上传
	 $("#upload").click(function(){
		 var func=function(){
			 if($("#documentForm").valid()){
				 $("#documentForm").submit();
				 return true;
			 }else{
				 return false;
			 }
		 };
		 createModalPage("文档上传","/technologyDocument/upload/view",func); 
	 });
	 
	 $("#lowerLimit").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	 $("#upperLimit").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	 
	 //搜索
	 $("#search").click(function(){
		 params['searchContent']=$("#searchContent").val();
		 params['type']=$("#type").val();
		 if($("#lowerLimit").val()!=null&&$("#lowerLimit").val()!=""){
			 params['lowerLimit']=$("#lowerLimit").val();
		 }else{
			 delete params['lowerLimit'];
		 }
		 if($("#upperLimit").val()!=null&&$("#upperLimit").val()!=""){
			 params['upperLimit']=$("#upperLimit").val();
		 }else{
			 delete params['upperLimit'];
		 }
		 search(0);
	 })
	 
	 var search = function(page){
		 params['page']=page;
		 $.ajax({
			url : url,
			data : params,
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				pageNum=data.total;
				page=data.page;
				if(params.searchContent==""||params.searchContent==null){
					files.seenDetail=false;
				}else{
					files.seenDetail=true;
				}
				files.fileDatas = data.content;
				//刷新分页插件
				createPage($("#dataPager"),pageNum,page,11,search)
			}
		 });
	 }
	 
	 //search autocomplete
	 $( "#searchContent" )
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
          $( "#searchContent" ).val(ui.item.label);
          return false;
        }
      }); 
	 
	 //加载数据
	 $("#search").click()
	 
})