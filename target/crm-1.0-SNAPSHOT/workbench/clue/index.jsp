
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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


	<%--<script type="text/javascript">

	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});

		//为创建按钮绑定事件
		$("#addBtn").click(function (){
			//需要获取所有者,需要走ajax
			$.ajax({
				url:"workbench/clue/getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data){
					/*
					* data
					*     [{用户1}，{用户2}，。。，。。，。。，{用户3}]
					* */
					var html = "<option></option>";


					$.each(data,function (i,n){

						html += "<option value='"+n.id+"'>"+n.name+"</option>";

					})

					$("#create-owner").html(html);

					//这个el表达式是登录的时候就已经存储好了
					var id = "${user.id}";

					//更改下拉框中 的默认值
					$("#create-owner").val(id);

					//处理完数据就打开模态窗口
					$("#createClueModal").modal("show");

				}
			})

		})

		//为保存按钮绑定事件，执行线索按钮的添加操作
		$("#saveBtn").click(function (){

			$.ajax({
				url:"workbench/clue/save.do",
				data:{

					"fullname" :$.trim($("#create-fullname").val()),
					"appellation" :$.trim($("#create-appellation").val()),
					"owner" :$.trim($("#create-owner").val()),
					"company" :$.trim($("#create-company").val()),
					"job" :$.trim($("#create-job").val()),
					"email" :$.trim($("#create-email").val()),
					"phone" :$.trim($("#create-phone").val()),
					"website" :$.trim($("#create-website").val()),
					"mphone" :$.trim($("#create-mphone").val()),
					"state" :$.trim($("#create-state").val()),
					"source" :$.trim($("#create-source").val()),
					"description" :$.trim($("#create-description").val()),
					"contactSummary" :$.trim($("#create-contactSummary").val()),
					"nextContactTime" :$.trim($("#create-nextContactTime").val()),
					"address" :$.trim($("#create-address").val())

				},
				type:"post",
				dataType:"json",
				success:function (data){

					/*
						data
							{"success" : true/false}

					* */
					if (data.success){

						//添加成功，
						//添加成功之后，就要刷新列表
						showList();

						//刷新完了之后就要关闭窗口
						$("#createClueModal").modal("hide");

					}else {

						//添加失败
						alert("添加线索失败")
					}

				}
			})


		})

		/*//刷新线索列表的函数
		showList();
		//showList(1,2);*/


	});

	function showList(pageNo,pageSize){

		$.ajax({
			url:"workbench/clue/getClueList.do",
			data:{

				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"fullname" : $.trim($("#clue-fullname").val()),
				"company" : $.trim($("#clue-company").val()),
				"phone" : $.trim($("#clue-phone").val()),
				"source" : $.trim($("#clue-source").val()),
				"owner" : $.trim($("#clue-owner").val()),
				"mphone" : $.trim($("#clue-mphone").val()),

			},
			type:"get",
			dataType:"json",
			success:function (data){
				/*
					data
						[{线索一}，{线索二}]
				* */
				html = "";
				/*<tr style="color: #B3B3B3;">
							<td><input type="checkbox" /></td>
							<td>名称</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
						</tr>*/

				$.each(data.cList,function (i,n){

					html += '<tr>'
					html += '"<td><input type="checkbox" /></td>'
					html += '"<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/clue/detail.jsp\';">'+n.fullname+'</a></td>'
					html += '"<td>'+n.company+'</td>'
					html += '"<td>'+n.phone+'</td>'
					html += '"<td>'+n.mphone+'</td>'
					html += '"<td>'+n.source+'</td>'
					html += '"<td>'+n.owner+'</td>'
					html += '"<td>'+n.state+'</td>'
					html += '"</tr>'

				})



				//计算总页数
				var totalPages = data.total%pageSize ==0?data.total/pageSize : (data.total/pageSize) +1;

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

</script>--%>

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

			//为创建按钮绑定事件
			$("#addBtn").click(function (){
				//需要获取所有者,需要走ajax
				$.ajax({
					url:"workbench/clue/getUserList.do",
					type:"get",
					dataType:"json",
					success:function (data){
						/*
                        * data
                        *     [{用户1}，{用户2}，。。，。。，。。，{用户3}]
                        * */
						var html = "<option></option>";
						$.each(data,function (i,n){
							html += "<option value='"+n.id+"'>"+n.name+"</option>";
						})
						$("#create-owner").html(html);
						//这个el表达式是登录的时候就已经存储好了
						var id = "${user.id}";
						//更改下拉框中 的默认值
						$("#create-owner").val(id);
						//处理完数据就打开模态窗口
						$("#createClueModal").modal("show");
					}
				})
			})

			//为保存按钮绑定事件，执行线索按钮的添加操作
			$("#saveBtn").click(function (){

				$.ajax({
					url:"workbench/clue/save.do",
					data:{

						"fullname" :$.trim($("#create-fullname").val()),
						"appellation" :$.trim($("#create-appellation").val()),
						"owner" :$.trim($("#create-owner").val()),
						"company" :$.trim($("#create-company").val()),
						"job" :$.trim($("#create-job").val()),
						"email" :$.trim($("#create-email").val()),
						"phone" :$.trim($("#create-phone").val()),
						"website" :$.trim($("#create-website").val()),
						"mphone" :$.trim($("#create-mphone").val()),
						"state" :$.trim($("#create-state").val()),
						"source" :$.trim($("#create-source").val()),
						"description" :$.trim($("#create-description").val()),
						"contactSummary" :$.trim($("#create-contactSummary").val()),
						"nextContactTime" :$.trim($("#create-nextContactTime").val()),
						"address" :$.trim($("#create-address").val())

					},
					type:"post",
					dataType:"json",
					success:function (data){

						/*
                            data
                                {"success" : true/false}

                        * */
						if (data.success){

							//添加成功，
							//添加成功之后，就要刷新列表
							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							//刷新完了之后就要关闭窗口
							$("#createClueModal").modal("hide");

						}else {

							//添加失败
							alert("添加线索失败")
						}

					}
				})


			})

			//为修改绑定事件，
			$("#editBtn").click(function (){

				//打开模态窗口前需要走ajax获取当前条数的数据,修改只能一条不能多条
				var $xz = $("input[name=xz]:checked");

				if ($xz.length == 0){
					alert("请选中需要修改的数据");
				}else if($xz.length > 1){
					alert("只能选择一条修改的数据");
				}else{

					var id = $xz.val();

					/*alert(id);*/
					$.ajax({
						url:"workbench/clue/getOneList.do",
						data:{
							"id":id
						},
						type:"get",
						dataType:"json",
						success:function (data){

							/*
								data
									一个全部用户列表的数据

									当前id线索列表数据
									[{},{},{},....,{}]
							* */
							var html = "<option></option>";

							//循环拿出全程user列表
							$.each(data.uList,function (i,n){

								html += "<option value='"+n.id+"'>"+n.name+"</option>";

							})

							//拼接这个html
							$("#edit-owner").html(html);

							//处理单条数据，将这些数据展示再前端
							$("#edit-id").val(data.c.id);
							$("#edit-owner").val(data.c.owner);
							$("#edit-company").val(data.c.company);
							$("#edit-appellation").val(data.c.appellation);
							$("#edit-fullname").val(data.c.fullname);
							$("#edit-job").val(data.c.job);
							$("#edit-email").val(data.c.email);
							$("#edit-phone").val(data.c.phone);
							$("#edit-mphone").val(data.c.mphone);
							$("#edit-state").val(data.c.state);
							$("#edit-source").val(data.c.source);
							$("#edit-website").val(data.c.website);
							$("#edit-description").val(data.c.description);
							$("#edit-contactSummary").val(data.c.contactSummary);
							$("#edit-nextContactTime").val(data.c.nextContactTime);
							$("#edit-address").val(data.c.address);



						}
					})

					//打开模态窗口
					$("#editClueModal").modal("show");



				}


			})

			//为更新按钮绑定事件，去后台修改数据
			$("#updateBtn").click(function (){

				/*alert("sdg")*/
				$.ajax({
					url:"workbench/clue/update.do",
					data:{

						//需要传一个id进来要不然我不知道更新那条
						"id" : $("#edit-id").val(),
						"fullname" : $.trim($("#edit-fullname").val()),
						"appellation" : $.trim($("#edit-appellation").val()),
						"owner" : $.trim($("#edit-owner").val()),
						"company" : $.trim($("#edit-company").val()),
						"job" : $.trim($("#edit-job").val()),
						"email" : $.trim($("#edit-email").val()),
						"phone" : $.trim($("#edit-phone").val()),
						"website" : $.trim($("#edit-website").val()),
						"mphone" : $.trim($("#edit-mphone").val()),
						"state" : $.trim($("#edit-state").val()),
						"source" : $.trim($("#edit-source").val()),
						"description" : $.trim($("#edit-description").val()),
						"contactSummary" : $.trim($("#edit-contactSummary").val()),
						"nextContactTime" : $.trim($("#edit-nextContactTime").val()),
						"address" : $.trim($("#edit-address").val())

					},
					type:"post",
					dataType:"json",
					success:function (data){

						//data {success : true/false}
						if (data.success){

							//添加成功,之后就会刷新列表
							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							//刷新列表之后就关闭模态窗口
							$("#editClueModal").modal("hide");

						}else {

							//添加失败
							alert("线索信息添加失败")

						}

					}
				})


			})

			//为删除绑定事件
			$("#deleteBtn").click(function (){

				//获取有多少个选框
				var $xz = $("input[name=xz]:checked");

				//当这个$xz没有选中的时候
				if ($xz.length == 0){

					alert("请选择需要删除的数据");
				}else {

					if(confirm("确认删除吗")){

						var param = "";
						//当有了选中的选框就要循环出来，获取要删除的id值
						for(var i = 0 ; i < $xz.length ; i++){

							param += "id=" + $($xz[i]).val();

							if(i < $xz.length-1){

								param += "&";

							}

						}


						//获取好了这些数据之后直接发送给ajax请求
						$.ajax({
							url:"workbench/clue/delete.do",
							data: param,
							type:"get",
							dataType:"json",
							success:function (data){

								//判断是否删除成功
								if (data.success){

									//删除成功，之后就要刷新页面
									pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


								}else {

									//删除失败
									alert("数据删除失败");
								}
							}
						})
					}


				}
			})


			//进入线索页面，刷新列表
			pageList(1,3);


			//为查询按钮绑定事件
			$("#searchBtn").click(function (){

				/*<input type="hidden" id="hidden-fullname"/>
					<input type="hidden" id="hidden-company"/>
					<input type="hidden" id="hidden-phone"/>
					<input type="hidden" id="hidden-source"/>
					<input type="hidden" id="hidden-owner"/>
					<input type="hidden" id="hidden-mphone"/>
					<input type="hidden" id="hidden-state"/>
					<input type="hidden" id="hidden-id"/>*/
				//点击查询按钮的时候，应该将搜索框中的信息保存起来，并保存到隐藏域中
				$("#hidden-fullname").val($.trim($("#search-fullname").val()));
				$("#hidden-company").val($.trim($("#search-company").val()));
				$("#hidden-phone").val($.trim($("#search-phone").val()));
				$("#hidden-source").val($.trim($("#search-source").val()));
				$("#hidden-owner").val($.trim($("#search-owner").val()));
				$("#hidden-mphone").val($.trim($("#search-mphone").val()));
				$("#hidden-state").val($.trim($("#search-state").val()));


				pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

			})


			//为全选选框绑定全选按钮
			$("#qx").click(function (){


				$("input[name=xz]").prop("checked",this.checked);


			})

			//为选框中全部不选的
			/*
				* 动态生成的元素，我们要以on方法的形式来触发事件
			语法:
			$(需要绑定元素的有效的外层元素). on(绑定事件的方式，需要绑定的元素的jquery对象,回调函

			* */
			$("#clueBody").on("click",$("input[name=xz]"),function (){

				$("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);

			})



		});

		function pageList(pageNo,pageSize){


			//将全选的复选框都取消掉
			$("#qx").prop("checked",false);

			//查询前，都要将隐藏域中保存的信息，重新再赋予到搜索框中
			/*
			$("#hidden-fullname").val($.trim($("#edit-fullname").val()));
				$("#hidden-company").val($.trim($("#edit-company").val()));
				$("#hidden-phone").val($.trim($("#edit-phone").val()));
				$("#hidden-source").val($.trim($("#edit-source").val()));
				$("#hidden-owner").val($.trim($("#edit-owner").val()));
				$("#hidden-mphone").val($.trim($("#edit-mphone").val()));
				$("#hidden-state").val($.trim($("#edit-state").val()));
				$("#hidden-id").val($.trim($("#edit-id").val()));
*/
			$("#search-fullname").val($.trim($("#hidden-fullname").val()));
			$("#search-company").val($.trim($("#hidden-company").val()));
			$("#search-phone").val($.trim($("#hidden-phone").val()));
			$("#search-source").val($.trim($("#hidden-source").val()));
			$("#search-owner").val($.trim($("#hidden-owner").val()));
			$("#search-mphone").val($.trim($("#hidden-mphone").val()));
			$("#search-state").val($.trim($("#hidden-state").val()));
			$("#search-owner").val($.trim($("#hidden-owner").val()));

			$.ajax({
				url:"workbench/clue/getAllList.do",
				data:{

					"pageNo":pageNo,
					"pageSize":pageSize,
					"fullname":$.trim($("#search-fullname").val()),
					"company":$.trim($("#search-company").val()),
					"phone":$.trim($("#search-phone").val()),
					"source":$.trim($("#search-source").val()),
					"owner":$.trim($("#search-owner").val()),
					"mphone":$.trim($("#search-mphone").val()),
					"state":$.trim($("#search-state").val()),

				},
				type:"get",
				dataType:"json",
				success:function (data){

					/*
					data
						[{线索一}，{线索二}]
				* */
					html = "";
					/*<tr style="color: #B3B3B3;">
                                <td><input type="checkbox" /></td>
                                <td>名称</td>
                                <td>公司</td>
                                <td>公司座机</td>
                                <td>手机</td>
                                <td>线索来源</td>
                                <td>所有者</td>
                                <td>线索状态</td>
                            </tr>*/
					$.each(data.dataList,function (i,n){

						html += '<tr class="active">';
						html += '"<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
						html += '"<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/clue/detail.do?id='+n.id+'\';">'+n.fullname+'</a></td>';
						html += '"<td>'+n.company+'</td>';
						html += '"<td>'+n.phone+'</td>';
						html += '"<td>'+n.mphone+'</td>';
						html += '"<td>'+n.source+'</td>';
						html += '"<td>'+n.owner+'</td>';
						html += '"<td>'+n.state+'</td>';
						html += '"</tr>';

					})

					$("#clueBody").html(html);

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
		/*function pageList(pageNo,pageSize){


			$.ajax({
				url:"workbench/clue/getAllList.do",
				type:"post",
				dataType:"json",
				success:function (data){

					/!*
					data
						[{线索一}，{线索二}]
				* *!/
					html = "";
					/!*<tr style="color: #B3B3B3;">
                                <td><input type="checkbox" /></td>
                                <td>名称</td>
                                <td>公司</td>
                                <td>公司座机</td>
                                <td>手机</td>
                                <td>线索来源</td>
                                <td>所有者</td>
                                <td>线索状态</td>
                            </tr>*!/
					$.each(data,function (i,n){

						html += '<tr>'
						html += '"<td><input type="checkbox" /></td>'
						html += '"<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/clue/detail.jsp\';">'+n.fullname+'</a></td>'
						html += '"<td>'+n.company+'</td>'
						html += '"<td>'+n.phone+'</td>'
						html += '"<td>'+n.mphone+'</td>'
						html += '"<td>'+n.source+'</td>'
						html += '"<td>'+n.owner+'</td>'
						html += '"<td>'+n.state+'</td>'
						html += '"</tr>'

					})

					$("#clueBody").html(html);

				}
			})

		}*/

</script>

</head>
<body>

	<input type="hidden" id="hidden-fullname"/>
	<input type="hidden" id="hidden-company"/>
	<input type="hidden" id="hidden-phone"/>
	<input type="hidden" id="hidden-source"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-mphone"/>
	<input type="hidden" id="hidden-state"/>

	<!-- 创建线索的模态窗口 -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
							<label for="create-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-appellation">
								  <option></option>
								  <c:forEach items="${appellationList}" var="a">

									  <option value="${a.value}">${a.text}</option>

								  </c:forEach>
								</select>
							</div>
							<label for="create-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-fullname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
							<label for="create-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
							<label for="create-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-state">
								  <option></option>
									<c:forEach items="${clueStateList}" var="c">

										<option value="${c.value}">${c.text}</option>

									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
								  <option></option>
									<c:forEach items="${sourceList}" var="s">

										<option value="${s.value}">${s.text}</option>

									</c:forEach>
								</select>
							</div>
						</div>
						

						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">线索描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control time" id="create-nextContactTime">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>
						
						<div style="position: relative;top: 20px;">
							<div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
							</div>
						</div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改线索的模态窗口 -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id">
					
						<div class="form-group">
							<label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-appellation">
								  <option></option>
									<c:forEach items="${appellationList}" var="a">

										<option value="${a.value}">${a.text}</option>

									</c:forEach>
								</select>
							</div>
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-fullname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job">
							</div>
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone">
							</div>
							<label for="edit-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" >
							</div>
							<label for="edit-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-state">
								  <option></option>

									<c:forEach items="${clueStateList}" var="c">

										<option value="${c.value}">${c.text}</option>

									</c:forEach>

								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
								  <option></option>

									<c:forEach items="${sourceList}" var="s">

										<option value="${s.value}">${s.text}</option>

									</c:forEach>

								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label ">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control time" id="edit-nextContactTime" >
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address"></textarea>
                                </div>
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

	<%--<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
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
				      <input class="form-control" type="text" >&lt;%&ndash;id="clue-fullname"&ndash;%&gt;
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司</div>
				      <input class="form-control" type="text" >&lt;%&ndash;id="clue-company"&ndash;%&gt;
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" >&lt;%&ndash;id="clue-phone"&ndash;%&gt;
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索来源</div>
					  <select class="form-control" >&lt;%&ndash;id="clue-source"&ndash;%&gt;
						 &lt;%&ndash; <c:forEach items="${sourceList}" var="s">

							  <option value="${s.value}">${s.text}</option>

						  </c:forEach>&ndash;%&gt;
					  </select>
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" >&lt;%&ndash;id="clue-owner"&ndash;%&gt;
				    </div>
				  </div>
				  
				  
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">手机</div>
				      <input class="form-control" type="text" >&lt;%&ndash;id="clue-mphone"&ndash;%&gt;
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索状态</div>
					  <select class="form-control" >&lt;%&ndash;id="clue-state"&ndash;%&gt;
						&lt;%&ndash;  <c:forEach items="${clueStateList}" var="c">

						  <option value="${c.value}">${c.text}</option>

						  </c:forEach>&ndash;%&gt;
				    </div>
				  </div>

				  <button type="submit" class="btn btn-default">查询</button>
				  
				</form>
			</div>--%>
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
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
							<input class="form-control" type="text" id="search-fullname">
						</div>
					</div>

					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">公司</div>
							<input class="form-control" type="text" id="search-company">
						</div>
					</div>

					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">公司座机</div>
							<input class="form-control" type="text" id="search-phone">
						</div>
					</div>

					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">线索来源</div>
							<select class="form-control" id="search-source">
								<option></option>
								<c:forEach items="${sourceList}" var="s">

									<option value="${s.value}">${s.text}</option>

								</c:forEach>
							</select>
						</div>
					</div>

					<br>

					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">所有者</div>
							<input class="form-control" type="text" id="search-owner">
						</div>
					</div>



					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">手机</div>
							<input class="form-control" type="text" id="search-mphone">
						</div>
					</div>

					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">线索状态</div>
							<select class="form-control" id="search-state">
								<option></option>
								<c:forEach items="${clueStateList}" var="c">

									<option value="${c.value}">${c.text}</option>

								</c:forEach>
							</select>
						</div>
					</div>
					<button type="button" class="btn btn-default" id="searchBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
						</tr>
					</thead >
					<tbody id="clueBody"><%----%>
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/clue/detail.jsp';">李四先生</a></td>
							<td>动力节点</td>
							<td>010-84846003</td>
							<td>12345678901</td>
							<td>广告</td>
							<td>zhangsan</td>
							<td>已联系</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>
                            <td>动力节点</td>
                            <td>010-84846003</td>
                            <td>12345678901</td>
                            <td>广告</td>
                            <td>zhangsan</td>
                            <td>已联系</td>
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