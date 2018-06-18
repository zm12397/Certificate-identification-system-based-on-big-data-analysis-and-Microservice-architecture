$(document).ready(function() {
	$("#uname").focus(function(){
		$("#l-uname").css('display','inline-block')
	}).blur(function(){
		$("#l-uname").css('display','none')
	})
	$("#password").focus(function(){
		$("#l-password").css('display','inline-block')
	}).blur(function(){
		$("#l-password").css('display','none')
	})
	/*
	 * $("#validate").focus(function(){
	 * $("#l-validate").css('display','inline-block'); })
	 * $("#validate").blur(function(){ if($(this).val() == ''){
	 * $("#l-validate").css('display','none'); } })
	 */

	/*
	 * $("#validateCode").click(function(){
	 * $(this).prop("src",$(this).prop("src") + "?d=" + new Date()); })
	 */
	/*$("#register").click(function(){
		window.location.href="register"; 
	})*/
	$("#login").click(function(){
		$.ajax({
			url : "login/login.action",
			data : {"username":$("#uname").val(),"password":$("#password").val()},
			method:"post",
			dataType:"json",
			success : function(result) {
				
				if(result.code == "0000"){
					layer.msg(result.message+"。。。正在前往主页",function(){
						window.location.href="index"; 
					})
					
					
				}else{
					layer.msg(result.message)
				}
				
			}
		})
	})
})