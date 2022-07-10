
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>



<!DOCTYPE html>
<html>
<head>

	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>


	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


<script type="text/javascript">

	$(function(){

		//日期插件
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});
		
		//点开之后需要走后台查询交易表有多少条信息
		showAllTransaction(1,3);

		//为了查询做绑定事件
		$("#getAllBtn").click(function (){

			/*
				在点及查询之后，将之前的信息保存到隐藏域中
					<input type="hidden" id="hidden-owner">
					<input type="hidden" id="hidden-name">
					<input type="hidden" id="hidden-customerId">
					<input type="hidden" id="hidden-type">
					<input type="hidden" id="hidden-source">
					<input type="hidden" id="hidden-contactsId">
					<input type="hidden" id="hidden-stage">

			*/

			$("#hidden-owner").val($.trim($("#create-owner").val()));
			$("#hidden-name").val($.trim($("#create-name").val()));
			$("#hidden-customerId").val($.trim($("#create-customerId").val()));
			$("#hidden-type").val($.trim($("#create-type").val()));
			$("#hidden-source").val($.trim($("#create-source").val()));
			$("#hidden-stage").val($.trim($("#create-stage").val()));
			$("#hidden-contactsId").val($.trim($("#create-contactsId").val()));


			showAllTransaction(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

		})

		//为了全选框做绑定操作
		$("#qx").click(function (){

			$("input[name=xz]").prop("checked",this.checked);

		})

		//为了点击选框做取消全选框，绑定事件
		$("#tranBody").on("click",$("input[name=xz]"),function (){

			$("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);

		})


	});

	//查询全部交易的信息
	function showAllTransaction(pageNo,pageSize){

		/*
			点击查询之后，要把隐藏域中的信息，先展示在前面
			$("#hidden-owner").val($.trim($("#create-owner").val()));
			$("#hidden-name").val($.trim($("#create-name").val()));
			$("#hidden-customerId").val($.trim($("#create-customerId").val()));
			$("#hidden-type").val($.trim($("#create-type").val()));
			$("#hidden-source").val($.trim($("#create-source").val()));
			$("#hidden-stage").val($.trim($("#create-stage").val()));
			$("#hidden-contactsId").val($.trim($("#create-contactsId").val()));

		*/

		$("#create-owner").val($.trim($("#hidden-owner").val()));
		$("#create-name").val($.trim($("#hidden-name").val()));
		$("#create-customerId").val($.trim($("#hidden-customerId").val()));
		$("#create-type").val($.trim($("#hidden-type").val()));
		$("#create-source").val($.trim($("#hidden-source").val()));
		$("#create-stage").val($.trim($("#hidden-stage").val()));
		$("#create-contactsId").val($.trim($("#hidden-contactsId").val()));

		$.ajax({
			url:"workbench/transaction/getAllTran.do",
			data:{

				"pageNo":pageNo,
				"pageSize":pageSize,
				"create-owner": $.trim($("#create-owner").val()),
				"create-name": $.trim($("#create-name").val()),
				"create-customerId": $.trim($("#create-customerId").val()),
				"create-stage": $.trim($("#create-stage").val()),
				"create-type": $.trim($("#create-type").val()),
				"create-source": $.trim($("#create-source").val()),
				"create-contactsId": $.trim($("#create-contactsId").val())
			},
			type:"get",
			dataType:"json",
			success:function (data){

				//返回来的就是一个tran集合 data ：[{交易列表1},{交易列表2},{交易列表3}，。。。。，{交易列表n}]
				//开始循环拼字符串
				var html = "";
				$.each(data.dataList,function (i,n){

					html += '<tr>';
					html += '<td><input type="checkbox" name="xz" id="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location,href=\'workbench/transaction/detail.do?id='+n.id+'\'">'+n.name+'</a></td>';
					html += '<td>'+n.customerId+'</td>';
					html += '<td>'+n.stage+'</td>';
					html += '<td>'+n.type+'</td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.source+'</td>';
					html += '<td>'+n.contactsId+'</td>';
					html += '</tr>';


				})

				//拼接好了之后，就要在下面展开
				$("#tranBody").html(html);

				//计算总页数
				var totalPages = data.total%pageSize == 0 ?data.total/pageSize : parseInt(data.total/pageSize) + 1;

				//数据处理完毕之后，结合分页查询，对前端展示分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数是在，点击分页组件的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}
		})

	}
	
</script>
</head>
<body>

	<%--隐藏域:在这个页面上都是可以加上隐藏域的，因为都是在同一个页面上的 搜索列表不需要隐藏域的id的--%>
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-customerId">
	<input type="hidden" id="hidden-type">
	<input type="hidden" id="hidden-source">
	<input type="hidden" id="hidden-contactsId">
	<input type="hidden" id="hidden-stage">

	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">

				<%--表单--%>
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="create-owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="create-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="create-customerId">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="create-stage">
					  	<option></option>

						  <c:forEach items="${stageList}" var="s"  >

							  <option value="${s.value}">${s.text}</option>

						  </c:forEach>

					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="create-type">
					  	<option></option>
					  	<c:forEach items="${transactionTypeList}" var="t">

							<option value="${t.value}">${t.text}</option>

						</c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="create-source">
						  <option></option>
						  <c:forEach items="${sourceList}" var="sc">

							  <option value="${sc.value}">${sc.text}</option>

						  </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="create-contactsId">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="getAllBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/add.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" onclick="window.location.href='edit.html';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="tranBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/transaction/detail.jsp';">动力节点-交易01</a></td>
							<td>动力节点</td>
							<td>谈判/复审</td>
							<td>新业务</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>李四</td>
						</tr>
                       --%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 20px;">


				<div id="activityPage">

				</div>


			</div>
			
		</div>
		
	</div>
</body>
</html>