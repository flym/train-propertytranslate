<html>
<head>
    <title>订单信息展示</title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.bootcss.com/twitter-bootstrap/4.3.1/css/bootstrap-grid.min.css">
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.bootcss.com/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container-fluid" style="width: 80%">

    <br/>
    <br/>
    <br/>
    <div class="alert alert-info">主订单信息</div>

    <table class="table table-hover table-bordered table-striped table-sm">
        <tr>
            <th class="w-25">Title</th>
            <th>&nbsp;</th>
        </tr>
    <#list resultPropertyList as rp>
        <tr>
            <td class="font-weight-bold">${rp.title!""}:</td>
            <td>${resultMap[rp.name]!""}</td>
        </tr>
    </#list>
    </table>
</div>
</body>
</html>