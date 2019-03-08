<html>
<head>
    <title>订单详情信息</title>
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
    <div class="alert alert-info">主订单</div>

    <table class="table table-hover table-bordered table-striped table-sm">
        <tr>
            <th class="w-25">Title</th>
            <th>&nbsp;</th>
        </tr>
    <#list resultPropertyListorderInfo as rp>
        <tr>
            <td class="font-weight-bold">${rp.title!""}:</td>
            <td>${resultMaporderInfo[rp.name]!""}</td>
        </tr>
    </#list>
    </table>

    <div class="alert alert-success">子订单列表</div>

    <table class="table table-hover table-bordered table-striped table-sm">
        <tr>
            <th class="w-25">商品名</th>
            <th>单价</th>
            <th>数量</th>
            <th>总价</th>
        </tr>
    <#list resultListsubOrders as r>
        <tr>
            <td>${r['title']!""}</td>
            <td>${r['price']!''}元</td>
            <td>${r['sum']!''}${r['unit']!''}</td>
            <td>${r['totalMoney']!""}元</td>
        </tr>
    </#list>
    </table>
</div>
</body>
</html>