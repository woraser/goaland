/**
 * 
 */

function createPage(pageDom,pageNum,page,showNum,func){
		//加载页码
		pageDom.createPage({
			//初始化参数
			pageNum: pageNum,//总页码
			current: page+1,//当前页
			shownum: showNum,//显示多少页
			//回调方法
			backfun: function(e) {
				page=e.current-1;//点中的页码
				//进行操作
				func(page)
			}
		});
		
}