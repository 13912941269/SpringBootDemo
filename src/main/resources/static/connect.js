/**
 * Created by gaowenfeng on 2017/7/13.
 */
function connect() {
    $.ajax({
        type: 'post',
        url: './code/connectDb',
        data: {
            ip: $('#ip').val(),
            db: $('#db').val(),
            port: $('#port').val(),
            dbName: $('#dbName').val(),
            username: $('#username').val(),
            password: $('#password').val()
        },
        success: function (result) {
            if(result.code==200){
                alert("连接成功");
                var str = '<br>';
                var list = result.data;
                for (var i = 0; i < list.length; i++){
                    str += '<br/>'+list[i].tableName+':<input type="checkbox" name="table" value="' + list[i].tableName + '"><br/>';
                    for(var s = 0; s < list[i].listColum.length; s++){
                        var obj=list[i].listColum[s];
                        str += obj.colName+':<input type="checkbox" name="'+list[i].tableName+'colum" value="' + obj.colName + '">';
                    }

                }
                $('#tableDiv').append(str);
            }
        },
        error: function (error) {
            alert(JSON.stringify(error));
        }
    })
}

function generate() {
    var str = "";
    for (var i = 0; i < document.getElementsByName('table').length; i++) {
        if (document.getElementsByName('table')[i].checked) {
            var tablename=document.getElementsByName('table')[i].value;
            var checkcolum="";
            for(var j = 0; j < document.getElementsByName(tablename+'colum').length; j++){
                if(document.getElementsByName(tablename+'colum')[j].checked){
                    if(checkcolum=="")
                        checkcolum+=document.getElementsByName(tablename+'colum')[j].value;
                    else
                        checkcolum+="$"+document.getElementsByName(tablename+'colum')[j].value;
                }

            }

            if (str == "")
                str += tablename+"-"+checkcolum;
            else
                str += "," +tablename+"-"+checkcolum;
        }
    }
    if (str == "") {
        alert("您没有选择任何数据");
    } else {
        alert(str);
        window.location.href = './code/constructCode?ip=' + $('#ip').val() + '&db=' + $('#db').val() + '&port=' + $('#port').val() + '&dbName=' + $('#dbName').val() + '&username=' + $('#username').val() + '&password=' + $('#password').val() + '&tables=' + str + '&basePackage=' + $('#basepackage').val()+'&author='+$('#author').val();
    }
}