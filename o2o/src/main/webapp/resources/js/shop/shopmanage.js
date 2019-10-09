$(function(){
	/*goShop();*/
	var shopId = getQueryString('shopId');
	var shopInfoUrl = '/o2o/shopadmin/getshopmanagementinfo?shopId='+shopId;
	$.getJSON(shopInfoUrl, function(data) {
		if(data.redirect){
			window.location.href = data.url;
		}
		else{
			
				
			
			$('#shopInfo').attr('href','/o2o/shopadmin/shopoperation?shopId='+shopId)
		}
		
		/*function goShop() {
			$('#shopInfo').attr('href','/o2o/shopadmin/shopoperation?shopId='+shopId)
			
		}*/
	});
});