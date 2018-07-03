function encryptEmail(email){
    var aStr = email.split('@')
    var prev = aStr[0]
    var post = aStr[1]
    var newEmail = prev.substring(0,3) + '*******' + post;
    return newEmail;
}