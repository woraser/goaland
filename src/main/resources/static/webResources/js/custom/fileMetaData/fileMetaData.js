/**
 * 
 */
$(document).ready(function() {
	
	var fileMetaData = new Vue({
	  el: '#fileMetaData',
	  data: {
		  metaData:{},
	  },
	  methods : {
		  
	  },
	})
	
	var loadFileMetaData = function(){
		$.ajax({
			url : "/fileMetaData/management/data/one?objectId="+$("#fileMetaDataId").val(),
			data : {"showAttributes":"fileName,fileSizeH,suffix,uploaderName,uploadTime"},
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				fileMetaData.metaData = data
			}
		 });
	}
	
	loadFileMetaData();
	
})