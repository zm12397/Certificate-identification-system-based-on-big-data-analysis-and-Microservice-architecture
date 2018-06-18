/*author:zfm
note:需要先引jquery*/
function getSendBackState(state){
	switch(state){
		case 0:
			return "已失效";
		case 1:
			return "已完成"
		case 2:
			return "已确认认领";
		case 3:
			return "已发布招领";
	}
}
function getSendBackType(type){
	switch(type){
		case 0:
			return "公共招领";
		case 1:
			return "校园招领";
	}
}
function getCertificateType(type){
	switch(type){
		case 0:
			return "身份证";
		case 1:
			return "学生证"
		case 2:
			return "驾驶证";
		case 3:
			return "一卡通";
		default:
			return "其他";
	}
}