
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
		
		/*
		创建按钮 通过这个id “addBtn” 去打开模态窗口
		* */
		$("#addBtn").click(function (){

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			/*
			* 操作模态窗口的方式:
				需要操作的模态窗口的jquery对象，调用modal方法， 为该方法传递参数show:打开  模态窗口hide: 关闭模态窗口
			* */

			/*
			* 在打开模态窗口的时候，应该先到后台去查找所有者，并且把这些信息发送到前端，是用ajax
			* 走后台，目的是为了获取用户的信息
			* */
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data){

					var html = "<option></option>"

					//遍历出来的n就是每一个用户
					$.each(data,function (i,n){
						html += "<option value='"+ n.id +"'>"+ n.name +"</option>"
					})

					$("#create-owner").html(html);
					//获取当前登录的用户的id，在js中使用el表达式，需要用“”括起来
					var id = "${user.id}"
					$("#create-owner").val(id);

					//模态窗口掉出来的函数
					$("#createActivityModal").modal("show");
				}
			})

		})

		//为保存按钮绑定事件 ，执行添加操作
		$("#saveBtn").click(function (){

			$.ajax({
				url:"workbench/activity/save.do",
				data:{

					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())

				},
				type:"post",
				dataType:"json",
				success:function (data){

					/*
					* data : { "success" : true/false}
					*
					* */
					if (data.success){

						//添加成功之后
						//刷新市场活动列表的信息
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");


						//关闭后再清空模态窗口中的数据
						//$("#activityAddForm").submit(); 这个是提交表单的

						/*
						注意:
							我们拿到了form表单的jquery对象
							对于表单的jquery对象，提供了submit()方法让我们提交表单.
							但是表单的jquery对象，没有为我们提供reset()方法让我们重置表单(坑: idea为 我们提示了有reset()方法)
							虽然jquery对象没有为我们提供reset方法，但是原生js为我们提供了reset方法
							所以我们要将jquery对象转换为原生dom对象


							jquery对象转换为dom对象: .
							jquery对象[下标]

							dom对象转换为jquery对象:
							$(dom)

						 */
						$("#activityAddForm")[0].reset();

					}else {
						alert("添加市场活动失败的");
					}

				}
			})

		})

		//页面加载完之后触发一个函数
		pageList(1,2);

		//为查询按钮绑定事件 ， 触发pageList函数
		$("#searchBtn").click(function (){

			//点击查询按钮的时候，应该将搜索框中的信息保存起来，并保存到隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

		})

		//为全选的复选框来绑定事件,触发全选操作
		$("#qx").click(function () {

			//选全部
			$("input[name=xz]").prop("checked",this.checked);

		})

			/*$("input[name=xz]").prop("checked", this.checked);

		});*/
		/*
		* 动态生成的元素，我们要以on方法的形式来触发事件
			语法:
			$(需要绑定元素的有效的外层元素). on(绑定事件的方式，需要绑定的元素的jquery对象,回调函

		* */

		$("#activityBody").on("click",$("input[name=xz]"),function (){

			$("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);

		})

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function (){

			//找到复选框中所选中的复选框的jquery的对象，获取到多少个
			var $xz = $("input[name=xz]:checked");

			if ($xz.length == 0 ){
				alert("请选择需要删除的数据");

				//肯定选了，有可能是一条，有可能是多条
			}else {


				if(confirm("确定所删除的记录吗")){

					//url: workbench/activity/delete.do?id=xxx&name=xxxx

					//拼接字符串
					var param = "";

					//将$xz中的每一个dom对象都遍历出来，取出value值，就相当于取到需要删除的id
					for (var i = 0 ; i < $xz.length ;i++){

						//可以再前加上$符号，当作jquery对象
						param += "id=" + $($xz[i]).val();

						if (i<$xz.length -1){

							param += "&";
						}

					}

					//alert(param);
					$.ajax({

						url:"workbench/activity/delete.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function (data){

							/*
                            * data
                            * 		{"success": true/false}
                            * */
							if (data.success){

								//删除成功后
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


							}else {

								alert("删除市场活动失败")
							}

						}
					})

				}


			}


		})

		//为修改按钮绑定事件
		$("#editBtn").click(function (){

			//获取需要修改的选项,获取到多少个
			var $xz = $("input[name=xz]:checked");

			if($xz.length == 0){
				alert("请选择一条记录");
			}else if ($xz.length > 1){
				alert("只能选择一条记录");
			}else {

				//之选了一条，可以直接选val，不需要循环，获取到id
				var id = $xz.val();

				//获取完了之后就要发送ajax请求到后台查出这个id中的数据，并将这些数据展示在页面中
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						"id":id
					},
					type:"get",
					dataType:"json",
					success:function (data){

						//返回什么数据，
						//用户列表

						//市场活动 对象

						//{"uList" ：[{用户1}，{用户2}，{用户3 }]，“a” : {市场活动}}
						var html = "<option></option>"

						//循环取出uList用户名
						$.each(data.uList,function (i,n){
							html += "<option value='"+n.id+"'>"+n.name+"</option>"
						})

						$("#edit-owner").html(html);

						//处理单条数据
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);

						//所有值修改好了之后，打开模态窗口
						$("#editActivityModal").modal("show");

					}
				})

			}


		})

		//为更新按钮绑定事件，执行修改操作
		$("#updateBtn").click(function (){

			$.ajax({
				url:"workbench/activity/update.do",
				data:{

					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-owner").val()),
					"name" : $.trim($("#edit-name").val()),
					"startDate" : $.trim($("#edit-startDate").val()),
					"endDate" : $.trim($("#edit-endDate").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val())

				},
				type:"post",
				dataType:"json",
				success:function (data){

					/*
					* data : { "success" : true/false}
					*
					* */
					if (data.success){

						//添加成功之后
						//刷新市场活动列表的信息
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//关闭添加操作的模态窗口
						$("#editActivityModal").modal("hide");


					}else {
						alert("修改市场活动失败的");
					}

				}
			})
		})

	});

	/*
	* 	对于所有的关系型数据库，做前端的分页相关操作的基础组件
		就是pageNo和pageSize
		pageNo:页码
		pageSizer每页展现的记录数

		 pageList函数通过ajax请求，查询市场活动的列表
		 并局部刷新展示列表（什么情况需要展示新的列表呢）
		 （1）点解左侧的菜单栏刷新
		 （2）创建，修改，删除后，都需要刷新列表
		 （3）点击上方的查询按钮需要刷新列表
		 （4）点击下边分页主键的时候都需要刷新


	* */
	function pageList(pageNo,pageSize){

		//将全选的复选框都取消掉
		$("#qx").prop("checked",false);

		//查询前，都要将隐藏域中保存的信息，重新再赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));


		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{

				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val()),

			},
			type:"get",
			dataType:"json",
			success:function (data){

				/*
				*  data
						我们需要的，市场活动信息列表
						[{市场活动1},{2},{3}]
						一会分页插件需要的:查询出来的总记录数
						{ "total":100}

				* */

				var html = "";

				$.each(data.dataList,function (i,n){
					html += '<tr class="active">';
					html += '<td><input type="checkbox"  name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';
				})

				$("#activityBody").html(html);

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


<!-- 修改市场活动的模态窗口
    data-target="#editActivityModal"
-->
<div class="modal fade" id="editActivityModal" role="dialog">
	<div class="modal-dialog" role="document" style="width: 85%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal" role="form">

					<input type="hidden" id="edit-id" >

					<div class="form-group">
						<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<select class="form-control" id="edit-owner">
								<%--<option>zhangsan</option>
								<option>lisi</option>
								<option>wangwu</option>--%>
							</select>
						</div>
						<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-name">
						</div>
					</div>

					<div class="form-group">
						<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control time" id="edit-startDate"">
						</div>
						<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control time" id="edit-endDate">
						</div>
					</div>

					<div class="form-group">
						<label for="edit-cost" class="col-sm-2 control-label">成本</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-cost">
						</div>
					</div>

					<div class="form-group">
						<label for="edit-describe" class="col-sm-2 control-label">描述</label>
						<div class="col-sm-10" style="width: 81%;">
							<textarea class="form-control" rows="3" id="edit-description"></textarea>
						</div>
					</div>

				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
			</div>
		</div>
	</div>
</div>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">

					<%--
						表示关闭模态窗口
						data-dismiss="modal"

					--%>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	

	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表11</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">

					<%--

						data-toggle="modal"
						表示触发按钮，将会打开这个模态窗口

						data-target="#editActivityModal"
						表示要打开那个模态窗口，通过#id的方式找到这个位置

					--%>


				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage">

				</div>


			</div>
			
		</div>
		
	</div>
</body>
</html>