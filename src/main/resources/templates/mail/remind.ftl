<html> 
<body> 
	<p>
		<h3>你好，${account.name}</h3>
	</p>
    <p>
    	设备:${ma.device.serialNo}的物料:${ma.name}已经到达预测性维护的预警要求
    	<br></br>
    	投运时间:${ma.beginTime}
    	<br></br>
    	维护周期:${ma.checkYear}年${ma.checkYear}月${ma.checkDay}日
    	<br></br>
    	提前预警:${ma.remindYear}年${ma.remindMonth}月${ma.remindDay}日
    </p>
    
</body>  