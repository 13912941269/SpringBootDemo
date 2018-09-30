var oppenid=$("#oppenid").val();
var tooppenid=$("#tooppenid").val();
var websocket=null;
var ws="ws://"+location.host+"/websocket/"+oppenid;
establishConn();
function send(){
    var message=$("#tx").val();
    var messagePack=JSON.stringify(
        {
            "type":"TYPE_USER_CHAT",
            "userid":oppenid,
            "touserid":tooppenid,
            "avatar":'',
            "content":message
        }
    );
    if(message!=""&&message!=null){
        websocket.send(messagePack);
    }
}


//close websockete when window closed
window.onbeforeunload=function(){
    alert("离开");
    websocket.close();
}


//Solve the message from server
function parseMessage(message){
    var m=JSON.parse(message);
    if(m.type=="TYPE_USER_CHAT"){
        setUserMessage(m);
    }else if(m.type=="TYPE_SERVER"){
        setSysMessage(m);
    }
}


function sendUserInfo(){
    var userInfo=JSON.stringify(
        {
            "type":"TYPE_USER_INFO",  // TYPE_USER_INFO
            "userid":oppenid,
            "avatar":'',
            "content":''
        }
    );
    websocket.send(userInfo);
}



function establishConn(){
    if(websocket==null){
        //judge whether support websocket
        if("websocket" in window){
            websocket=new WebSocket(ws);
        }else
            alert("Not Support websocket");
        //callback on establish connection
        websocket.onopen=function(){
            setMessageInnerHTML("connected ..");
            sendUserInfo();
        };
        //callback on error
        websocket.onerror=function(){
            setMessageInnerHTML("error ! !");
        };
        //callback on webSocket close
        websocket.onclose=function(){
            setMessageInnerHTML("websocket closed !");
        };
        //callback when receive message
        websocket.onmessage=function(event){
            parseMessage(event.data);
        };
    }else{
        alert("connection exists!")
    }
}



//display client sysmessage on page
function setMessageInnerHTML(message){
    $("#msg").append("<p>(<font color='red'>" + message + "</font>)</p>");
}

//clear
function clearMessageInnerHTML(){
    $("#msg").html("");
}

//display user message on page
function setUserMessage(anaMessage){
    var user=anaMessage.userid;
    var contents=anaMessage.content;
    var avatar=anaMessage.avatar;
    if(user==oppenid){
        setMessageInnerHTML("发："+contents);
    }else{
        setMessageInnerHTML("收："+contents);
    }
}


//show onlineList and server message
function setSysMessage(anaMessage){
    //var list=anaMessage.onlineList;
    var userParam=anaMessage.userParam;
    var content=anaMessage.content;
    var userItem="";
    clearMessageInnerHTML();
    //$("#msg").append("系统通知"+userItem);
    setMessageInnerHTML("用户"+userParam+content);
}



$('#conBtn').on('click', function () {
    establishConn();
});
$('#disconBtn').on('click', function () {
    closeWebSocket();
});


//close websocket
function closeWebSocket(){
    if(websocket!=null){
        websocket.close();
        websocket=null;
        $("#onlineList").html("");
    }else alert("already disconnected!");
}