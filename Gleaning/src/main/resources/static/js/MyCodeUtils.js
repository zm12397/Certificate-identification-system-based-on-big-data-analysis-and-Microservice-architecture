/* 使用方法：
             $("#name").html(encryptName(String($("#name").html()),4,'x'))
             $("#name1").html(encryptName(String($("#name1").html()),4,'x'))
             $("#number").html(encryptNumber(String($("#number").html()),4,4,10,'x'))
             $("#number1").html(encryptNumber(String($("#number1").html()),4,4,10,'x'))
             $("#number2").html(encryptNumber(String($("#number2").html()),4,4,10,'x'))*/
function encryptName(name,len,stuff){
        var prex;
        if(name.length < 1){
            prex = stuff
        }else{
            prex = name.substring(0,1);
        }
        for(var i = 1;i < len;i ++){
            prex = prex + stuff
        }
        return prex;
    }
    function encryptNumber(number,len1,len2,len3,stuff){
        var len = number.length
        var prex;
        var post;
        if(len < len1 || len < len2){
            prex =  number.substring(0,len);
            post = ''
        }else{
            prex = number.substring(0,len1);
            post = number.substring(len - len2,len)
        }
         
        for(var i = 1;i < len3;i ++){
            prex = prex + stuff
        }
        return prex + post;
    }