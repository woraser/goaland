/**
 * 
 */
$(document).ready(function() {
	
	var messageInfo = new Vue({
	  el: '#messageInfo',
	  data: {
		  message:{},
	  },
	  methods : {
		  viewDetail : function(url){
			  console.info("href url:"+url)
			  window.location.href = url
		  }
	  },
	})
	
	var loadMessageInfo = function(){
		$.ajax({
			url : "/messageInfo/management/data/one?id="+$("#messageInfoId").val(),
			data : {"showAttributes":"title,content,from.name,to.name,sendTime,readTime,url"},
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				messageInfo.message = data
			}
		 });
	}
	
	loadMessageInfo();
	
})