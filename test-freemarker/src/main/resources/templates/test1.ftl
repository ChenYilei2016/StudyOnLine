<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
    name:${name+'1'} <br>
    <#if name??>name != null</#if> <br>
    map: ${stuMap.stu1.name}<br>
    list:
    <#list stus as stu>
        ${stu.name}
    </#list><br>

    date:${stu1.birthday?string("yyyy-MM-dd hh:mm:ss")} <br>

    <#list 1..10 as index>
        <#--当前index 从0开始-->
        ${index_index}
    </#list>

    ${ (stuMap.stu12.name )! 'da'}<br>

    ${123}
</body>
</html>