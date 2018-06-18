$(document).ready(function() {
	$("#file").change(function(){
		var fileId = [ 'file' ];
		var _this = this;
		$.ajaxFileUpload({
			type : 'post',
			dataType : 'json',
			url : "file/head/upload.action",
			secureuri : false,
			fileElementId : fileId,
			success : function(result) {
				console.log(result)
				if(result.code == "0000"){
					$("#img").val(result.data.id);
					layer.msg("头像上传成功")
					
				}else{
					layer.msg(result.message)
				}
				
			}
			
		});
	});
	$("#register").click(function(){
		if($("#username-info").html()=="用户名已存在"){
			layer.msg("用户名已经存在")
			$("#username").focus();
		}else if($("#validate-password").html()=="两次密码不一致"){
			layer.msg("两次密码不一致")
			$("#repasswd").focus();
		}else{
			if($("#username").val() == ''){
				layer.msg("请输入用户名")
				$("#username").focus();
			}else if($("#name").val() == ''){
				layer.msg("请输入姓名")
				$("#name").focus();
			}else if($("#certificateNumber").val() == ''){
				layer.msg("请输入身份证号")
				$("#certificateNumber").focus();
			}else if($("#email").val() == ''){
				layer.msg("请输入邮箱地址")
				$("#email").focus();
			}else{
				var objData = {"name":$("#name").val(),"username":$("#username").val(),"password":$("#password").val(),
						"certificateNumber":$("#certificateNumber").val(),"email":$("#email").val(),"tel":$("#tel").val(),
						"sex":$("#sex").val(),"img":$("#img").val()}
				$.ajax({
					url : "register/register.action",
					data : objData,
					method:"post",
					dataType:"json",
					success : function(result) {
						if(result.code == "0000"){
							layer.alert(result.message, {skin: 'layui-layer-lan',closeBtn: 0,anim: 4,btn:["点击前去登录"],btn1:function(){
								window.location.href="login"; 
							}})
							
						}else{
							layer.alert(result.message, {skin: 'layui-layer-molv',closeBtn: 0,anim: 4 })
						}
						
					}
				})	
			}
			
		}
		
		
	})
	$("#username").blur(function(){
		$.ajax({
			url : "register/validate.action",
			data : {"username":$("#username").val()},
			method:"post",
			dataType:"json",
			success : function(result) {
				$("#username-info").html(result.message)
				/*if(result.code == "0000"){
					
				}else{
					layer.alert(result.message, {skin: 'layui-layer-molv',closeBtn: 0,anim: 4 })
				}
				*/
			}
		})
	})
	$("#repasswd").blur(function(){
		if($("#password").val() != $("#repasswd").val()){
			$("#repasswd-info").html("两次密码不一致")
		}else{
			$("#repasswd-info").html("")
		}
	})
})